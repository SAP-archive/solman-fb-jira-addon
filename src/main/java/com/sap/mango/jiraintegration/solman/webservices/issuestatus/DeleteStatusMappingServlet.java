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
 * Servlet, that deletes IssueStatus Mapping
 */
public class DeleteStatusMappingServlet extends JsonServlet {

    private SolmanParamsDAO solmanParamsDAO;

    private IssueStatusMappingDAO issueStatusMappingDAO;

    public DeleteStatusMappingServlet(SolmanParamsDAO solmanParamsDAO, IssueStatusMappingDAO issueStatusMappingDAO) {
        this.solmanParamsDAO = solmanParamsDAO;
        this.issueStatusMappingDAO = issueStatusMappingDAO;
    }

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DeleteIssueMappingRequestBean deleteProjectMappingRequestBean = JsonEncoder.mapper.readValue(req.getParameter("issueMapping"), DeleteIssueMappingRequestBean.class);

        String solManCustGuiD = deleteProjectMappingRequestBean.getSolManCustGuiD();
        if (solManCustGuiD == null) {
            sendError_WrongParams(resp, "Parameter solManCustGuiD should not be empty.");
            return;
        }

        String id = deleteProjectMappingRequestBean.getId();
        if (id == null) {
            sendError_WrongParams(resp, "Parameter id should not be empty.");
            return;
        }

        List<SolmanParamsAO> solmanParams = solmanParamsDAO.getSolmanParamsByGuid(solManCustGuiD);

        if (solmanParams == null || solmanParams.isEmpty()) {
            sendError_WrongParams(resp, "Not existing Solman Connection with GuiD = " + solManCustGuiD);
            return;
        } else {
            sendOK(resp, issueStatusMappingDAO.deleteIssueStatusMapping(solManCustGuiD, id));
        }
    }

    @Override
    public String requiredRoles() {
        return "jira-administrators";
    }
}
