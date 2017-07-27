package com.sap.mango.jiraintegration.solman.webservices.issuetypemapping;

import com.sap.mango.jiraintegration.core.JsonServlet;
import com.sap.mango.jiraintegration.solman.entities.issuetype.IssueTypeMapping;
import com.sap.mango.jiraintegration.solman.entities.issuetype.IssueTypeMappingDAO;
import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsAO;
import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet, that deletes an issue type mapping from the database
 */
public class DeleteIssueTypeMappingServlet extends JsonServlet {

    private final SolmanParamsDAO solmanParamsDAO;

    private final IssueTypeMappingDAO issueTypeMappingDAO;

    public DeleteIssueTypeMappingServlet(final SolmanParamsDAO solmanParamsDAO, IssueTypeMappingDAO issueTypeMappingDAO) {
        this.solmanParamsDAO = solmanParamsDAO;
        this.issueTypeMappingDAO = issueTypeMappingDAO;
    }

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String solManCustGuiD = req.getParameter("solManCustGuiD");
        if (solManCustGuiD == null) {
            sendError_WrongParams(resp, "Parameter solManCustGuiD should not be empty.");
            return;
        }

        List<SolmanParamsAO> solmanParams = solmanParamsDAO.getSolmanParamsByGuid(solManCustGuiD);

        if (solmanParams == null || solmanParams.isEmpty()) {
            sendError_WrongParams(resp, "Not existing Solman Connection with GuiD = " + solManCustGuiD);
            return;
        }

        String solManProcessType  = req.getParameter("solManProcessType");
        if (solManProcessType == null) {
            sendError_WrongParams(resp, "Parameter solManProcessType should not be empty.");
            return;
        }

        final IssueTypeMapping issueTypeMapping = issueTypeMappingDAO.getIssueTypeMapping(solManCustGuiD, solManProcessType);
        if (issueTypeMapping == null){
            sendError_WrongParams(resp, "No entry found.");
            return;
        }
        if (issueTypeMappingDAO.deleteIssueTypeMapping(issueTypeMapping.getID())){
            sendOK(resp, "nice");
        }else{
            sendError_WrongParams(resp, "Problem while deleting id: " + issueTypeMapping.getID());
        }
    }

    @Override
    public String requiredRoles() {
        return "jira-administrators";
    }
}
