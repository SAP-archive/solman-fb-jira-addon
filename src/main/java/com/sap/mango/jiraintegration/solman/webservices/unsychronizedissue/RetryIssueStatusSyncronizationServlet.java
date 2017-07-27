package com.sap.mango.jiraintegration.solman.webservices.unsychronizedissue;

import com.sap.mango.jiraintegration.core.JsonServlet;
import com.sap.mango.jiraintegration.core.data.Either;
import com.sap.mango.jiraintegration.core.httpclient.RestClient;
import com.sap.mango.jiraintegration.solman.entities.unsynchronizedissue.IssueSynchronizationException;
import com.sap.mango.jiraintegration.solman.entities.unsynchronizedissue.IssueTransitionService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Retry to synchronize issue manually
 */
public class RetryIssueStatusSyncronizationServlet extends JsonServlet {

    private IssueTransitionService issueTransitionService;

    public RetryIssueStatusSyncronizationServlet(IssueTransitionService issueTransitionService) {
        this.issueTransitionService = issueTransitionService;
    }

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        if (id == null) {
            sendError_WrongParams(resp, "missing id of the unsynchronized issue");
        }
        Either<RestClient.ServiceError, String> issueTransition;
        try {
           issueTransition = issueTransitionService.executeIssueStatusSynchronization(Integer.valueOf(id), true);
        }

        catch(IssueSynchronizationException e) {
            sendError(resp, new IssueSynchronizationStatusResultBean(e.getCode(), e.getErrorMessage()));
            return;
        }
        if (issueTransition.isLeft()) {
            sendError(resp, new IssueSynchronizationStatusResultBean(issueTransition.toLeft().left_value.code, issueTransition.toLeft().left_value.detail));
            return;
        }
        sendOK(resp, issueTransition.toRight().right_value);
    }

    @Override
    public String requiredRoles() {
        return "jira-administrators";
    }
}
