package com.sap.mango.jiraintegration.solman.entities.unsynchronizedissue;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.config.properties.APKeys;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.mail.Email;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.UserUtils;
import com.atlassian.mail.queue.SingleMailQueueItem;
import com.atlassian.mail.server.MailServerManager;
import com.atlassian.mail.server.SMTPMailServer;
import com.sap.mango.jiraintegration.core.data.Either;
import com.sap.mango.jiraintegration.core.httpclient.RestClient;
import com.sap.mango.jiraintegration.core.httpclient.SolmanClient;
import com.sap.mango.jiraintegration.solman.beans.SolmanParams;
import com.sap.mango.jiraintegration.solman.entities.jumpurl.JumpUrlSettings;
import com.sap.mango.jiraintegration.solman.entities.jumpurl.JumpUrlSettingsDAO;
import com.sap.mango.jiraintegration.solman.entities.proxysettings.ProxySettings;
import com.sap.mango.jiraintegration.solman.entities.proxysettings.ProxySettingsDAO;
import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsDAO;
import com.sap.mango.jiraintegration.utils.HttpUtils;
import com.sap.mango.jiraintegration.utils.PropertiesUtils;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

/**
 * Storing service methods for UnsynchroizedIssue Entity
 */
public class IssueTransitionService {

    private SolmanParamsDAO solmanParamsDAO;

    private ProxySettingsDAO proxySettingsDAO;

    private JiraAuthenticationContext authenticationContext;

    private IssueManager issueManager;

    private MailServerManager mailServerManager;

    private JumpUrlSettingsDAO jumpUrlSettingsDAO;

    private IssueTransitionDAO issueTransitionDAO;

    private static final String solManGuidCustField = "Solman guid";

    private static final Logger LOG = LoggerFactory.getLogger(IssueTransitionService.class);


    public IssueTransitionService(JiraAuthenticationContext authenticationContext, IssueTransitionDAO issueTransitionDAO, SolmanParamsDAO solmanParamsDAO, ProxySettingsDAO proxySettingsDAO,
                                  IssueManager issueManager, MailServerManager mailServerManager, JumpUrlSettingsDAO jumpUrlSettingsDAO) {
        this.solmanParamsDAO = solmanParamsDAO;
        this.proxySettingsDAO = proxySettingsDAO;
        this.authenticationContext = authenticationContext;
        this.issueManager = issueManager;
        this.mailServerManager = mailServerManager;
        this.jumpUrlSettingsDAO = jumpUrlSettingsDAO;
        this.issueTransitionDAO = issueTransitionDAO;
    }

    public Either<RestClient.ServiceError, String> executeIssueStatusSynchronization(Integer id, Boolean isManual) throws IssueSynchronizationException {
        IssueTransition issueTransition = this.issueTransitionDAO.getIssueTransition(id);
        if (issueTransition == null) {
            throw new IssueSynchronizationException("Not existing issue transition with id = " + id, HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
        }
        if (issueTransitionDAO.existsOlderUnsynchronizedIssue(issueTransition.getID(), issueTransition.getUnsynchronizedIssue().getIssueId())) {
            throw new IssueSynchronizationException("There is existing older unsynchronized status update, that should be executed first! ", HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
        }
        List<SolmanParams> solmanParams = solmanParamsDAO.getSolmanParamsByGuid(issueTransition.getUnsynchronizedIssue().getSolmanParams().getCustomerGiud(), true);
        if (solmanParams.isEmpty()) {
            throw new IssueSynchronizationException("Not existing Solman Customer with guid = " + issueTransition.getUnsynchronizedIssue().getSolmanParams().getCustomerGiud(), null);
        }

        MutableIssue issueResult = issueManager.getIssueObject(issueTransition.getUnsynchronizedIssue().getIssueId());

        CustomFieldManager customFieldManager = ComponentAccessor.getCustomFieldManager();

        CustomField solManGuidCF = customFieldManager.getCustomFieldObjectByName(solManGuidCustField);

        String solManGuid = (String) issueResult.getCustomFieldValue(solManGuidCF);

        SolmanParams solmanParam = solmanParams.get(0);

        String transitionName = issueTransition.getNewValue();
        ProxySettings proxySettings = proxySettingsDAO.getProxySettings(solmanParam.customerGuid);

        final Either<RestClient.ServiceError, String> issueTransitionResult = SolmanClient.solmanSendStatus(
                solmanParam, solManGuid, transitionName, proxySettings);
        if (issueTransitionResult.isLeft()) {
            issueTransition.setLastProcessor(authenticationContext.getLoggedInUser() != null ? authenticationContext.getLoggedInUser().getDisplayName() : "queueprocessor");
            if (!isManual) {
                issueTransition.setLastProcessingDate(new Date());
            }
            if (HttpUtils.isCommunicationError(issueTransitionResult.toLeft().left_value.code)) {
                issueTransition.setProcessingStatus(ProcessingStatus.CommError.getValue());
            } else {
                //send notification email to the user
                if (isValidForEmailNotification(issueTransition.getProcessingStatus(), isManual)) {
                    sendEmailForApplicationError(issueTransitionResult, issueTransition, issueResult, solManGuid);
                }
                issueTransition.setProcessingStatus(ProcessingStatus.AppError.getValue());
            }
            issueTransitionDAO.updateIssueTransition(issueTransition);
        } else {
            issueTransitionDAO.setIssueTransitionToSynchronized(issueTransition, authenticationContext.getLoggedInUser() != null ? authenticationContext.getLoggedInUser().getDisplayName() : "queueprocessor");
        }
        return issueTransitionResult;
    }

    public void sendEmailForApplicationError(Either<RestClient.ServiceError, String> issueTransitionResult, IssueTransition issueTransition, MutableIssue mutableIssue, String solManGuid) {
        SMTPMailServer mailServer = mailServerManager.getDefaultSMTPMailServer();
        ApplicationUser processingUser = UserUtils.getUser(issueTransition.getProcessingUser());
            if (processingUser == null) {
            LOG.error("Error while sending email for issue synchronization application error. Not existing user with username " + issueTransition.getProcessingUser());
            return;
        }
        //read all text templates
        String subject = PropertiesUtils.getValue("solman.admin.jiraqueueprocessing.unsynchronizedissue.email.subject");
        String body = PropertiesUtils.getValue("solman.admin.jiraqueueprocessing.unsynchronizedissue.email.body");
        String errorDetails = PropertiesUtils.getValue("solman.admin.jiraqueueprocessing.unsynchronizedissue.email.body.errordetails");
        String footer = PropertiesUtils.getValue("solman.admin.jiraqueueprocessing.unsynchronizedissue.email.footer");
        String issueLink = ComponentAccessor.getApplicationProperties().getString(APKeys.JIRA_BASEURL) + "/browse/" + mutableIssue.getKey();

        //generating the url of WP/WI in Solution Manager
        JumpUrlSettings jumpUrlSettings = jumpUrlSettingsDAO.getJumpUrlSettings(issueTransition.getUnsynchronizedIssue().getSolmanParams().getCustomerGiud());
        if (jumpUrlSettings == null) {
            LOG.error("Error while generating link of WP/WI in email template for issue " + mutableIssue.getKey());
            return;
        }
        String solmanLink = null;
        if (mutableIssue.getIssueTypeObject().getName().toUpperCase().contains("EPIC")) {
            solmanLink = jumpUrlSettings.getWorkPackageAppJumpUrl() +  solManGuid;
        } else {
            solmanLink = jumpUrlSettings.getWorkItemAppJumpUrl() +  solManGuid;
        }
        Email email = new Email(processingUser.getEmailAddress());
        email.setSubject(MessageFormat.format(subject, mutableIssue.getKey()));
        //generating the body of the email
        StringBuffer emailBodyBuffer = new StringBuffer();
        emailBodyBuffer.append(MessageFormat.format(body, issueTransition.getProcessingUser(), issueLink, mutableIssue.getKey(), solmanLink));
        emailBodyBuffer.append(MessageFormat.format(errorDetails, issueTransitionResult.toLeft().left_value.detail));
        emailBodyBuffer.append(footer);
        email.setBody(emailBodyBuffer.toString());
        //mime type
        email.setMimeType("text/html");
        SingleMailQueueItem singleMailQueueItem = new SingleMailQueueItem(email);
        ComponentAccessor.getMailQueue().addItem(singleMailQueueItem);
    }

    private boolean isValidForEmailNotification(Integer processingStatus, Boolean isManual) {
        return !processingStatus.equals(ProcessingStatus.AppError.getValue()) && !isManual;
    }
}
