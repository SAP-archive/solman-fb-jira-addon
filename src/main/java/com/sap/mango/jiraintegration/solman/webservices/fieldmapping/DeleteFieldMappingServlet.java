package com.sap.mango.jiraintegration.solman.webservices.fieldmapping;

import com.sap.mango.jiraintegration.core.JsonServlet;
import com.sap.mango.jiraintegration.solman.entities.fieldmapping.FieldMappingDAO;
import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet, that deletes an field mapping from the database
 */
public class DeleteFieldMappingServlet extends JsonServlet {

    private final SolmanParamsDAO solmanParamsDAO;

    private final FieldMappingDAO fieldMappingDAO;

    public DeleteFieldMappingServlet(final SolmanParamsDAO solmanParamsDAO, FieldMappingDAO fieldMappingDAO) {
        this.solmanParamsDAO = solmanParamsDAO;
        this.fieldMappingDAO = fieldMappingDAO;
    }

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        if (id == null) {
            sendError_WrongParams(resp, "Parameter id should not be empty.");
            return;
        }

        if (fieldMappingDAO.deleteFieldMapping(Integer.parseInt(id))){
            sendOK(resp, "nice");
        }else{
            sendError_WrongParams(resp, "Problem while deleting id: " + id);
        }
    }

    @Override
    public String requiredRoles() {
        return "jira-administrators";
    }
}
