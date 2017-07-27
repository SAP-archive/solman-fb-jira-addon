package com.sap.mango.jiraintegration.listeners;

import com.atlassian.event.api.EventListener;
import com.atlassian.event.api.EventPublisher;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.event.type.EventType;
import com.atlassian.jira.exception.CreateException;
import com.atlassian.jira.issue.AttachmentManager;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.attachment.CreateAttachmentParamsBean;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.link.RemoteIssueLink;
import com.atlassian.jira.issue.link.RemoteIssueLinkBuilder;
import com.atlassian.jira.issue.link.RemoteIssueLinkManager;
import com.atlassian.jira.web.util.AttachmentException;
import com.sap.mango.jiraintegration.solman.beans.SolmanParams;
import com.sap.mango.jiraintegration.solman.entities.fileinfo.FileInfo;
import com.sap.mango.jiraintegration.solman.entities.fileinfo.FileInfoDAO;
import com.sap.mango.jiraintegration.solman.entities.proxysettings.ProxySettings;
import com.sap.mango.jiraintegration.solman.entities.proxysettings.ProxySettingsDAO;
import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsDAO;
import com.sap.mango.jiraintegration.solman.webservices.odata.userstory.IssueCustomFieldsNames;
import com.sap.mango.jiraintegration.utils.FileManager;
import com.sap.mango.jiraintegration.utils.UrlUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Asynchronous listener, that executes all attachments/documents processing
 */
public class AsyncIssueCreationEventListener implements AsyncEventListener, DisposableBean, InitializingBean {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(AsyncIssueCreationEventListener.class);

    private FileInfoDAO fileInfoDAO;

    private SolmanParamsDAO solmanParamsDAO;

    private CustomFieldManager customFieldManager;

    private ProxySettingsDAO proxySettingsDAO;

    private final EventPublisher eventPublisher;

    public AsyncIssueCreationEventListener(EventPublisher eventPublisher, FileInfoDAO fileInfoDAO, CustomFieldManager customFieldManager, SolmanParamsDAO solmanParamsDAO, ProxySettingsDAO proxySettingsDAO) {
        this.fileInfoDAO = fileInfoDAO;
        this.customFieldManager = customFieldManager;
        this.solmanParamsDAO = solmanParamsDAO;
        this.eventPublisher = eventPublisher;
        this.proxySettingsDAO = proxySettingsDAO;
    }

    @Override
    public void destroy() throws Exception {
        eventPublisher.unregister(this);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        eventPublisher.register(this);
    }

    @EventListener
    public void onIssueEvent(AsyncIssueEvent event) {
        if (event.getIssueEventType().equals(EventType.ISSUE_CREATED_ID)) {
            Issue issue = event.getIssue();
            CustomField ticketGuidCF = customFieldManager.getCustomFieldObjectByName(IssueCustomFieldsNames.solManGuiDCFName);
            List<FileInfo> files = fileInfoDAO.getFilesInfo(ticketGuidCF.getValue(issue).toString());

            URL fileUrl = null;
            for (FileInfo fileInfo : files) {
                SolmanParams solmanParams = solmanParamsDAO.getSolmanParamsByGuid(fileInfo.getSolmanParams().getCustomerGiud(), true).get(0);
                ProxySettings proxySettings = proxySettingsDAO.getProxySettings(solmanParams.customerGuid);

                if (fileInfo.getExtension().equals("URL")) {
                    //we store the url as a Web Link
                    String url = fileInfo.getUrl();
                    String technFileName = fileInfo.getTechnFilename();

                    RemoteIssueLink remoteIssueLink = new RemoteIssueLinkBuilder().issueId(issue.getId()).url(url).title(technFileName).build();
                    try {
                        ComponentAccessor.getComponent(RemoteIssueLinkManager.class).createRemoteIssueLink(remoteIssueLink, ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser());
                    } catch (CreateException e) {
                        LOG.error("Error while creating remote issue link");
                        continue;
                    }
                } else {
                    try {
                        fileUrl = UrlUtils.generateUrl(fileInfo.getUrl(), solmanParams.solmanUrl);
                    } catch (MalformedURLException e) {
                        LOG.error("Error while generating url " + e.fillInStackTrace());
                        continue;
                    }
                    LOG.debug("Generated File Url " + fileUrl.toString() + " For issue " + issue.getKey());
                    File savedFile = null;
                    try {
                        savedFile = FileManager.saveFile(proxySettings, solmanParams, issue, fileUrl.toString(), fileInfo.getTechnFilename(), fileInfo.getAttachmentType());
                    } catch (IOException e) {
                        LOG.error("Error while saving file into temp folder; " + e.getMessage());
                        continue;
                    }
                    AttachmentManager attachmentManager = ComponentAccessor.getAttachmentManager();
                    CreateAttachmentParamsBean createAttachmentParamsBean = new CreateAttachmentParamsBean(savedFile, savedFile.getName(),
                            MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(savedFile), ComponentAccessor.getJiraAuthenticationContext().getUser(),
                            issue, null, null, null, new Timestamp(new Date().getTime()), true);
                    try {
                        attachmentManager.createAttachment(createAttachmentParamsBean);
                    } catch (AttachmentException e) {
                        LOG.error("Error while creating attachment for jira " + issue.getKey());
                        continue;
                    }

                }
                this.fileInfoDAO.updateFilesInfo(fileInfo.getID(), true);
                LOG.info("Succesfully attached file " + fileInfo.getUrl() + " to issue " + issue.getKey());
            }
            try {
                FileManager.deleteIssueTempDirectory(issue);
            } catch (IOException e) {
                LOG.error("Problem while deleting attachments temp folder. ");
            }
        }
    }
}
