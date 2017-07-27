package com.sap.mango.jiraintegration.solman.webservices.projectmapping;

import com.sap.mango.jiraintegration.core.JsonServlet;
import com.sap.mango.jiraintegration.solman.entities.projecttype.ProjectMappingDAO;
import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsAO;
import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsDAO;
import com.sap.mango.jiraintegration.utils.JsonEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet, that deletes a project mapping from the database
 */
public class DeleteProjectMappingServlet extends JsonServlet {

    private final SolmanParamsDAO solmanParamsDAO;

    private final ProjectMappingDAO projectMappingDAO;

    public DeleteProjectMappingServlet(final SolmanParamsDAO solmanParamsDAO, final ProjectMappingDAO projectMappingDAO) {
        this.solmanParamsDAO = solmanParamsDAO;
        this.projectMappingDAO = projectMappingDAO;
    }

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        DeleteProjectMappingRequestBean deleteProjectMappingRequestBean = JsonEncoder.mapper.readValue(req.getParameter("projectMapping"), DeleteProjectMappingRequestBean.class);

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
            sendOK(resp, projectMappingDAO.deleteProjectMapping(id, solManCustGuiD));
        }
    }

    @Override
    public String requiredRoles() {
        return "jira-administrators";
    }
}
