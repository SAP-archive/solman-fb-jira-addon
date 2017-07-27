package com.sap.mango.jiraintegration.solman.webservices.issuestatus;

import com.sap.mango.jiraintegration.core.JsonServlet;
import com.sap.mango.jiraintegration.solman.entities.issuestatus.IssueStatusMappingDAO;
import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsAO;
import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsDAO;
import com.sap.mango.jiraintegration.utils.JsonEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet, that adds IssueStatus Mapping
 */
public class AddIssueStatusMappingServlet extends JsonServlet {

    private final SolmanParamsDAO solmanParamsDAO;

    private final IssueStatusMappingDAO issueStatusMappingDAO;

    public AddIssueStatusMappingServlet(SolmanParamsDAO solmanParamsDAO, IssueStatusMappingDAO issueStatusMappingDAO) {
        this.solmanParamsDAO = solmanParamsDAO;
        this.issueStatusMappingDAO = issueStatusMappingDAO;
    }

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        AddIssueStatusMappingRequestBean addIssueStatusMappingRequestBean = JsonEncoder.mapper.readValue(req.getParameter("issueMapping"), AddIssueStatusMappingRequestBean.class);
       /* if (!req.getMethod().equals(HttpMethod.POST)) {
            sendError(HttpStatus.SC_METHOD_NOT_ALLOWED, resp, "Expected POST request");
            return;
        }*/

        String solmanProcessType = addIssueStatusMappingRequestBean.getSolmanProcessType();

        String jiraTransition = addIssueStatusMappingRequestBean.getJiraTransition();

        String solmanStatus = addIssueStatusMappingRequestBean.getSolmanStatus();

        String solManCustGuiD = addIssueStatusMappingRequestBean.getSolManCustGuiD();
        if (solManCustGuiD == null) {
            sendError_WrongParams(resp, "Parameter solManCustGuiD should not be empty.");
            return;
        }

        List<SolmanParamsAO> solmanParams = solmanParamsDAO.getSolmanParamsByGuid(solManCustGuiD);

        if (solmanParams == null || solmanParams.isEmpty()) {
            sendError_WrongParams(resp, "Not existing SolmanConnection with GuiD = " + solManCustGuiD);
            return;
        }

        Integer issueMappingID = issueStatusMappingDAO.saveIssueStatusMapping(solmanParams.get(0), solmanProcessType, solmanStatus, jiraTransition);
        resp.setHeader("issueMappingID", issueMappingID.toString());
        sendOK(resp, "nice");
    }

    @Override
    public String requiredRoles() {
        return "jira-administrators";
    }
}
