package com.sap.mango.jiraintegration.solman.webservices.textfieldmapping;

import com.sap.mango.jiraintegration.core.JsonServlet;
import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsDAO;
import com.sap.mango.jiraintegration.solman.entities.textfieldmapping.TextFieldMappingDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet, that deletes an field mapping from the database
 */
public class DeleteTextFieldMappingServlet extends JsonServlet {

    private final SolmanParamsDAO solmanParamsDAO;

    private final TextFieldMappingDAO fieldMappingDAO;

    public DeleteTextFieldMappingServlet(final SolmanParamsDAO solmanParamsDAO, TextFieldMappingDAO fieldMappingDAO) {
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
