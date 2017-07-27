package com.sap.mango.jiraintegration.solman.webservices.issuetypemapping;

import com.atlassian.jira.config.IssueTypeManager;
import com.atlassian.jira.issue.issuetype.IssueType;
import com.sap.mango.jiraintegration.core.JsonServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Returns 'Epic' and 'Story' issue type informations
 */
public class GetJiraIssueTypesServlet extends JsonServlet {

    private IssueTypeManager issueTypeManager;

    public GetJiraIssueTypesServlet(IssueTypeManager issueTypeManager) {
        this.issueTypeManager = issueTypeManager;
    }

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Collection<IssueType> issueTypes = issueTypeManager.getIssueTypes();
        Collection<JiraIssueTypeBean> issueTypeBeans = new ArrayList<>();
        for (IssueType issueType : issueTypes) {
            if (issueType.getName().equals("Epic") || issueType.getName().equals("Story")) {
                issueTypeBeans.add(new JiraIssueTypeBean(issueType.getId(), issueType.getName()));
            }
        }
        sendOK(resp, issueTypeBeans);
    }

    @Override
    public String requiredRoles() {
        return "jira-administrators";
    }
}
