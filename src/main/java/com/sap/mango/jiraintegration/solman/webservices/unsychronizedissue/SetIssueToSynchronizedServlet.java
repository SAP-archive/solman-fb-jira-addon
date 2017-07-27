package com.sap.mango.jiraintegration.solman.webservices.unsychronizedissue;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.sap.mango.jiraintegration.core.JsonServlet;
import com.sap.mango.jiraintegration.solman.entities.unsynchronizedissue.IssueTransition;
import com.sap.mango.jiraintegration.solman.entities.unsynchronizedissue.IssueTransitionDAO;
import com.sap.mango.jiraintegration.solman.entities.unsynchronizedissue.UnsynchronizedIssue;
import com.sap.mango.jiraintegration.solman.entities.unsynchronizedissue.UnsynchronizedIssueDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Set issue to successfully synchonized.
 */
public class SetIssueToSynchronizedServlet extends JsonServlet {

    private IssueTransitionDAO issueTransitionDAO;

    private JiraAuthenticationContext authenticationContext;

    public SetIssueToSynchronizedServlet(IssueTransitionDAO issueTransitionDAO, JiraAuthenticationContext authenticationContext) {
        this.issueTransitionDAO = issueTransitionDAO;
        this.authenticationContext = authenticationContext;
    }

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        IssueTransition
                issueTransition = this.issueTransitionDAO.getIssueTransition(Integer.valueOf(id));
        if (issueTransition == null) {
            sendError_WrongParams(resp, "Not existing issue transition record with id = " + id);
            return;
        }
        issueTransitionDAO.setIssueTransitionToSynchronized(issueTransition, authenticationContext.getLoggedInUser().getDisplayName());
        sendOK(resp, "nice");
    }

    @Override
    public String requiredRoles() {
        return "jira-administrators";
    }
}
