package com.sap.mango.jiraintegration.solman.webservices.solmanconnections;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.sap.mango.jiraintegration.core.JsonServlet;
import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsDAO;
import org.apache.http.HttpStatus;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Web service, that gets all Solution Manager connections from jira database. <br/>
 * Url: /spDelete <br/>
 * Request Parameters <p>
 *     customerGuid : the Solution Manager Customer GuiD
 * </p>
 */
public class DeleteSolmanParamsWS extends JsonServlet {

    private final SolmanParamsDAO solmanParamsDAO;

    public DeleteSolmanParamsWS(ActiveObjects ao, SolmanParamsDAO solmanParamsDAO) {
        this.solmanParamsDAO = solmanParamsDAO;
    }

    @Override
    public void process(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        final String customerGuid = req.getParameter("customerGuid");
        if (customerGuid == null || customerGuid.isEmpty()) {
            sendError_WrongParams(response, "Parameter SolmanParams must not be empty");
            return;
        }
        Boolean isDeleted = solmanParamsDAO.deleteSolmanParam(customerGuid);
        if (isDeleted) {
            sendOK(response, true);
        } else {
            sendError(HttpStatus.SC_NOT_FOUND, response, false);
        }
    }

    @Override
    public String requiredRoles() {
        return "jira-administrators";
    }
}
