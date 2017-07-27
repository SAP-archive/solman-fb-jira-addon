package com.sap.mango.jiraintegration.solman.webservices.unsychronizedissue;

import com.atlassian.scheduler.status.RunDetails;
import com.sap.mango.jiraintegration.core.JsonServlet;
import com.sap.mango.jiraintegration.solman.beans.SolmanParams;
import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsDAO;
import com.sap.mango.jiraintegration.solman.scheduler.unsynchronizedissue.SolmanPluginSchedulerManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Returns the last execution of specific Solman Synchronization Job Runner
 */
public class GetSolmanPluginJobRunnerInfoServlet extends JsonServlet {

    private SolmanPluginSchedulerManager solmanPluginSchedulerManager;

    private SolmanParamsDAO solmanParamsDAO;

    public GetSolmanPluginJobRunnerInfoServlet(SolmanPluginSchedulerManager solmanPluginSchedulerManager,
                                               SolmanParamsDAO solmanParamsDAO) {
        this.solmanPluginSchedulerManager = solmanPluginSchedulerManager;
        this.solmanParamsDAO = solmanParamsDAO;
    }

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("solManCustGuiD") == null) {
            sendError_WrongParams(resp, "Parameter solManCustGuiD should not be empty.");
            return;
        }

        List<SolmanParams> solmanParams = solmanParamsDAO.getSolmanParamsByGuid(req.getParameter("solManCustGuiD"), false);
        RunDetails runDetails = solmanPluginSchedulerManager.getSolmanIssueSynchronizationRunDetails(solmanParams.get(0));
        Long lastExecutionTimestamp = runDetails.getStartTime().getTime();
        resp.addHeader("lastExecutionTimestamp", lastExecutionTimestamp.toString());
        sendOK(resp, "ok");
    }

    @Override
    public String requiredRoles() {
        return "jira-administrators";
    }
}
