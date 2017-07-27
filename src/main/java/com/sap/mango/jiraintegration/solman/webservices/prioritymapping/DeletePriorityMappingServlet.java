package com.sap.mango.jiraintegration.solman.webservices.prioritymapping;

import com.sap.mango.jiraintegration.core.JsonServlet;
import com.sap.mango.jiraintegration.solman.entities.priority.PriorityMappingDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet, that deletes an priority mapping from the database
 */
public class DeletePriorityMappingServlet extends JsonServlet {

    private PriorityMappingDAO priorityMappingDAO;

    public DeletePriorityMappingServlet(PriorityMappingDAO priorityMappingDAO) {
        this.priorityMappingDAO = priorityMappingDAO;
    }

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        if (id == null) {
            sendError_WrongParams(resp, "Parameter id should not be empty.");
            return;
        }
        if (priorityMappingDAO.deletePriorityMapping(Integer.valueOf(id))) {
            sendOK(resp, "nice");
        } else {
            sendError_WrongParams(resp, "Problem while deleting id: " + id);
        }
    }

    @Override
    public String requiredRoles() {
        return "jira-administrators";
    }
}
