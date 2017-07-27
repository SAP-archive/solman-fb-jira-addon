package com.sap.mango.jiraintegration.solman.panels;

import com.atlassian.crowd.embedded.api.User;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.plugin.webfragment.contextproviders.AbstractJiraContextProvider;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.PluginParseException;
import com.sap.mango.jiraintegration.solman.entities.unsynchronizedissue.IssueTransitionDAO;
import com.sap.mango.jiraintegration.solman.entities.unsynchronizedissue.UnsynchronizedIssueDAO;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class SolmanSynchronizationIndicator extends AbstractJiraContextProvider {

    private IssueTransitionDAO issueTransitionDAO;

    public SolmanSynchronizationIndicator(IssueTransitionDAO issueTransitionDAO) {
        this.issueTransitionDAO = issueTransitionDAO;
    }

    @Override
    public Map getContextMap(ApplicationUser applicationUser, JiraHelper jiraHelper) {
        Map contextMap = new HashMap<>();
        Issue currentIssue = (Issue) jiraHelper.getContextParams().get("issue");
        boolean isIssueUnsynchronizedAppError = issueTransitionDAO.isIssueUnsynchronizedAppError(currentIssue.getId());
        boolean isIssueUnsynchronizedCommError = issueTransitionDAO.isIssueUnsynchronizedCommError(currentIssue.getId());
        contextMap.put("isIssueUnsynchronizedAppError", isIssueUnsynchronizedAppError);
        contextMap.put("isIssueUnsynchronizedCommError", isIssueUnsynchronizedCommError);
        return contextMap;

    }

    @Override
    public void init(Map params) throws PluginParseException {
        super.init(params);
    }

    @Override
    public Map getContextMap(Map context) {
       return super.getContextMap(context);
    }
}
