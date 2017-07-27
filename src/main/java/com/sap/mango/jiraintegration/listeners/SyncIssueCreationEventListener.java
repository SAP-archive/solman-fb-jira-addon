package com.sap.mango.jiraintegration.listeners;

import com.atlassian.event.api.EventListener;
import com.atlassian.event.api.EventPublisher;
import com.atlassian.jira.event.issue.IssueEvent;
import com.atlassian.jira.issue.CustomFieldManager;
import com.sap.mango.jiraintegration.solman.entities.fileinfo.FileInfoDAO;
import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsDAO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Listener, that publish asynchonous event for the attachments/documents downloading process
 */
public class SyncIssueCreationEventListener implements SyncEventListener, InitializingBean, DisposableBean {

    private static final Logger LOG = Logger.getLogger(SyncIssueCreationEventListener.class);

    private FileInfoDAO fileInfoDAO;

    private SolmanParamsDAO solmanParamsDAO;

    private CustomFieldManager customFieldManager;

    private final EventPublisher eventPublisher;


    public SyncIssueCreationEventListener(EventPublisher eventPublisher, FileInfoDAO fileInfoDAO, CustomFieldManager customFieldManager, SolmanParamsDAO solmanParamsDAO) {
        this.fileInfoDAO = fileInfoDAO;
        this.customFieldManager = customFieldManager;
        this.solmanParamsDAO = solmanParamsDAO;
        this.eventPublisher = eventPublisher;
    }

    @EventListener
    public void onIssueEvent(IssueEvent event) {
       eventPublisher.publish(new AsyncIssueEvent(event.getIssue(), event.getUser(), event.getEventTypeId()));
    }

    @Override
    public void destroy() throws Exception {
        eventPublisher.unregister(this);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        eventPublisher.register(this);
    }
}
