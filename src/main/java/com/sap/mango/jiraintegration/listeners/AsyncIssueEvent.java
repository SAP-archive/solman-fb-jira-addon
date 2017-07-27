package com.sap.mango.jiraintegration.listeners;


import com.atlassian.event.api.AsynchronousPreferred;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.user.ApplicationUser;

/**
 * An asynch object, that we publish (storing the issue, user and eventType)
 */
@AsynchronousPreferred
public class AsyncIssueEvent {
    private final Issue issue;
    private final ApplicationUser user;
    private final Long issueEventType;

    public AsyncIssueEvent(Issue issue, ApplicationUser user, Long issueEventType) {
        this.issue = issue;
        this.user = user;
        this.issueEventType = issueEventType;
    }

    public Issue getIssue() {
        return issue;
    }

    public ApplicationUser getUser() {
        return user;
    }

    public Long getIssueEventType() {
        return issueEventType;
    }
}
