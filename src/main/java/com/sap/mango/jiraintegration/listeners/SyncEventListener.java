package com.sap.mango.jiraintegration.listeners;

import com.atlassian.jira.event.issue.IssueEvent;

/**
 * Listener, that publish asynchonous event for the attachments/documents downloading process
 */
public interface SyncEventListener {
    void onIssueEvent(IssueEvent issueEvent);
}
