package com.sap.mango.jiraintegration.solman.webservices.unsychronizedissue;

import com.sap.mango.jiraintegration.core.JsonServlet;
import com.sap.mango.jiraintegration.solman.entities.unsynchronizedissue.IssueTransitionDAO;
import com.sap.mango.jiraintegration.solman.entities.unsynchronizedissue.UnsynchronizedIssueDAO;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet, that deletes all successful entries
 */
public class DeleteSynchronizedIssuesServlet extends JsonServlet {

    private IssueTransitionDAO issueTransitionDAO;

    public DeleteSynchronizedIssuesServlet(IssueTransitionDAO issueTransitionDAO) {
        this.issueTransitionDAO = issueTransitionDAO;
    }

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String solManCustGuiD = req.getParameter("solManCustGuiD");
        if (StringUtils.isEmpty(solManCustGuiD)) {
            sendError_WrongParams(resp, "Parameter solManCustGuiD should not be empty.");
            return;
        }
        boolean isSuccessful = issueTransitionDAO.deleteAllSuccessfulEntries(solManCustGuiD);
        sendOK(resp, isSuccessful);
    }

    @Override
    public String requiredRoles() {
        return "jira-administrators";
    }
}
