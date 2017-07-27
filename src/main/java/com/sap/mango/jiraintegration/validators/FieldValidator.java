package com.sap.mango.jiraintegration.validators;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.ofbiz.OfBizDelegator;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.workflow.JiraWorkflow;
import com.atlassian.jira.workflow.WorkflowManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.Validator;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.sap.mango.jiraintegration.core.data.Either;
import com.sap.mango.jiraintegration.core.httpclient.RestClient;
import com.sap.mango.jiraintegration.core.httpclient.SolmanClient;
import com.sap.mango.jiraintegration.solman.beans.SolmanParams;
import com.sap.mango.jiraintegration.solman.entities.proxysettings.ProxySettings;
import com.sap.mango.jiraintegration.solman.entities.proxysettings.ProxySettingsDAO;
import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsDAO;
import com.sap.mango.jiraintegration.solman.entities.unsynchronizedissue.*;
import com.sap.mango.jiraintegration.utils.HttpUtils;
import com.sap.mango.jiraintegration.utils.JsonEncoder;
import com.sap.mango.jiraintegration.utils.PropertiesUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.ofbiz.core.entity.EntityExpr;
import org.ofbiz.core.entity.EntityOperator;
import org.ofbiz.core.entity.GenericEntityException;
import org.ofbiz.core.entity.GenericValue;
import org.ofbiz.core.entity.jdbc.SQLProcessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Field Validator, that executes a call to Solution Manager for status change.
 */
public class FieldValidator implements Validator {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(FieldValidator.class);

    private static final String solManGuidCustField = "Solman guid";
    private static final String solManCustomerGuidCustField = "Solman customer guid";
    private final SolmanParamsDAO solmanParamsDAO;
    private final WorkflowManager workflowManager;
    private final ProxySettingsDAO proxySettingsDAO;
    private final UnsynchronizedIssueDAO unsynchronizedIssueDAO;
    private final IssueTransitionDAO issueTransitionDAO;
    private JiraAuthenticationContext authenticationContext;


    public FieldValidator(WorkflowManager workflowManager, SolmanParamsDAO solmanParamsDAO, ProxySettingsDAO proxySettingsDAO, UnsynchronizedIssueDAO unsynchronizedIssueDAO,
                          IssueTransitionDAO issueTransitionDAO, JiraAuthenticationContext authenticationContext) {
        this.workflowManager = workflowManager;
        this.solmanParamsDAO = solmanParamsDAO;
        this.proxySettingsDAO = proxySettingsDAO;
        this.unsynchronizedIssueDAO = unsynchronizedIssueDAO;
        this.authenticationContext = authenticationContext;
        this.issueTransitionDAO = issueTransitionDAO;
    }

    public void validate(Map transientVars, Map args, PropertySet ps) throws InvalidInputException, WorkflowException {
        Issue issue = (Issue) transientVars.get("issue");

        //get the action id
        Integer actionId = (Integer) transientVars.get("actionId");

        ActionDescriptor actionDescriptor = getActionDescriptor(actionId, issue);

        //get the value of the custom fields
        CustomFieldManager customFieldManager = ComponentAccessor.getCustomFieldManager();

        CustomField solManGuidCF = customFieldManager.getCustomFieldObjectByName(solManGuidCustField);
        CustomField customerGuidCF = customFieldManager.getCustomFieldObjectByName(solManCustomerGuidCustField);

        String solManGuid = (String) issue.getCustomFieldValue(solManGuidCF);
        String customerGuid = (String) issue.getCustomFieldValue(customerGuidCF);
        String defaultCustomerGuiD = PropertiesUtils.getValue("solman.admin.defaultCustomerGuiD");

        LOG.debug("----Executing Status Change Validator----");
        LOG.debug("Solman Guid: " + solManGuid);
        LOG.debug("Customer Guid: " + customerGuid);
        LOG.debug("Transition Name: " + actionDescriptor.getName());

        //if the customer guid is not the default
        if (customerGuid != null && solManGuid != null) {
            List<SolmanParams> solmanParams = solmanParamsDAO.getSolmanParamsByGuid(customerGuid, true);

            if (solmanParams.isEmpty()) {
                throw new InvalidInputException("There is not registered user with SolMan Customer Id '" +
                        customerGuid + "'.");
            }

            ProxySettings proxySettings = proxySettingsDAO.getProxySettings(solmanParams.get(0).customerGuid);

            final Either<RestClient.ServiceError, String> serviceErrorStringEither = SolmanClient.solmanSendStatus(
                    solmanParams.get(0), solManGuid, actionDescriptor.getName(), proxySettings);

            if (serviceErrorStringEither.isLeft()) {
                if (HttpUtils.isCommunicationError(serviceErrorStringEither.toLeft().left_value.code)) {
                    //unsynchronized issue info
                    UnsynchronizedIssueBean unsynchronizedIssueBean = new UnsynchronizedIssueBean();
                    unsynchronizedIssueBean.setSolManParamsId(solmanParamsDAO.getSolmanParamsByGuid(customerGuid).get(0).getID());
                    unsynchronizedIssueBean.setCreationDate(new Date());
                    unsynchronizedIssueBean.setIssueId(issue.getId());
                    unsynchronizedIssueBean.setIssueKey(issue.getKey());
                    //transition info
                    IssueTransitionBean issueTransitionBean = new IssueTransitionBean();
                    issueTransitionBean.setProcessingStatus(ProcessingStatus.CommError.getValue());
                    issueTransitionBean.setNewValue(actionDescriptor.getName());
                    issueTransitionBean.setSendType(SendType.STATUS_UPDATE.getValue());
                    issueTransitionBean.setSolmanGuid(solManGuid);
                    issueTransitionBean.setProcessingUser(authenticationContext.getLoggedInUser().getName());

                    if (unsynchronizedIssueDAO.getUnsynchronizedIssue(issue.getId()) != null) {
                         issueTransitionDAO.saveIssueTransition(unsynchronizedIssueDAO.getUnsynchronizedIssue(issue.getId()),
                                issueTransitionBean);
                        return;
                    }
                    unsynchronizedIssueDAO.saveUnsychronizedIssue(unsynchronizedIssueBean);
                    issueTransitionDAO.saveIssueTransition(unsynchronizedIssueDAO.getUnsynchronizedIssue(issue.getId()),
                            issueTransitionBean);
                } else {
                    InvalidInputException invalidInputException = new InvalidInputException(generateErrorMessage(serviceErrorStringEither).toArray(new String[0]));
                    throw invalidInputException;
                }

            }
        }
    }

    public ActionDescriptor getActionDescriptor(int actionId, Issue issue) {
        JiraWorkflow workflow = workflowManager.getWorkflow(issue);
        final WorkflowDescriptor wd = workflow.getDescriptor();

        final ActionDescriptor action = wd.getAction(actionId);

        if (action != null) {
            return action;
        }
        return null;
    }

    private List<String> generateErrorMessage(final Either<RestClient.ServiceError, String> serviceErrorStringEither) {
        List<String> errorMessages = new ArrayList<>();
        final ObjectMapper mapper = JsonEncoder.mapper;
        try {
            if (serviceErrorStringEither.toLeft().left_value.code == HttpStatusCodes.INTERNAL_SERVER_ERROR.getStatusCode()) {
                JsonNode root = mapper.readTree(StringUtils.substringAfter(((RestClient.ServiceError) ((Either.Left) serviceErrorStringEither).left_value).detail, "entity:"));

                //main error message
                JsonNode errorMessage = root.path("error");

                //error details
                JsonNode innererror = root.path("error").get("innererror");

                if (errorMessage.hasNonNull("message")) {
                    errorMessages.add("Error ");
                    errorMessages.add(errorMessage.findPath("value").textValue());
                }

                if (innererror.hasNonNull("errordetails")) {
                    errorMessages.add("Error Details ");
                    for (JsonNode errorDetail : innererror.get("errordetails")) {
                        String severity = StringUtils.upperCase(errorDetail.get("severity").textValue().substring(0, 1)) + errorDetail.get("severity").textValue().substring(1);
                        errorMessages.add(severity + ": " + errorDetail.get("message").textValue() + "(" + errorDetail.get("code") + ")");
                    }
                }

            } else {
                errorMessages.add(serviceErrorStringEither.toLeft().left_value.detail);
            }
        } catch (IOException e) {
            return null;
        }
        return errorMessages;
    }

}