package com.sap.mango.jiraintegration.listeners;

/**
 * Asynchronous listener, that executes all attachments/documents processing
 */
public interface AsyncEventListener {

    void onIssueEvent(AsyncIssueEvent issueEvent);
}
