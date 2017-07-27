package com.sap.mango.jiraintegration.solman.webservices.prioritymapping;

import com.sap.mango.jiraintegration.core.JsonServlet;
import com.sap.mango.jiraintegration.solman.entities.priority.PriorityMappingDAO;
import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsAO;
import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * * Web service, that adds a mapper (SolMan Priority type <-> Jira Priority)
 * in the jira database.</br>
 * Url: /addIssueTypeMapping</br>
 * Request Parameters <p>
 *     solManCustGuiD : the Solution Manager Customer GuiD <br/>
 *     solmanPriority : the Solution Manager Priority <br/>
 *     jiraPriority : Jira Priority<br/>
 * </p>
 *
 */
public class AddPriorityMappingServlet extends JsonServlet {

    private SolmanParamsDAO solmanParamsDAO;

    private PriorityMappingDAO priorityMappingDAO;

    public AddPriorityMappingServlet(SolmanParamsDAO solmanParamsDAO, PriorityMappingDAO priorityMappingDAO) {
        this.solmanParamsDAO = solmanParamsDAO;
        this.priorityMappingDAO = priorityMappingDAO;
    }

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String solManCustGuiD = req.getParameter("solManCustGuiD");
        if (solManCustGuiD == null || solManCustGuiD.isEmpty()) {
            sendError_WrongParams(resp, "Parameter solManCustGuiD should not be empty.");
            return;
        }

        String solmanPriority  = req.getParameter("solmanPriority");
        if (solmanPriority == null) {
            sendError_WrongParams(resp, "Parameter solmanPriority should not be empty.");
            return;
        }
        String jiraPriority = req.getParameter("jiraPriority");
        if (jiraPriority == null) {
            sendError_WrongParams(resp, "Parameter jiraPriority should not be empty.");
            return;
        }
        List<SolmanParamsAO> solmanParams = solmanParamsDAO.getSolmanParamsByGuid(solManCustGuiD);

        if (solmanParams == null || solmanParams.isEmpty()) {
            sendError_WrongParams(resp, "Not existing SolmanConnection with GuiD = " + solManCustGuiD);
            return;
        }

        Integer priorityMappingID = priorityMappingDAO.saveUpdatePriorityMapping(solmanParams.get(0), solmanPriority, jiraPriority);

        resp.setHeader("priorityMappingID", priorityMappingID.toString());

        sendOK(resp, "nice");
    }

    @Override
    public String requiredRoles() {
        return "jira-administrators";
    }
}
