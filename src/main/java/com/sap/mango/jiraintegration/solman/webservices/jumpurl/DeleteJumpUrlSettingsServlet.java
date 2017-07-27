package com.sap.mango.jiraintegration.solman.webservices.jumpurl;

import com.sap.mango.jiraintegration.core.JsonServlet;
import com.sap.mango.jiraintegration.solman.entities.jumpurl.JumpUrlSettingsDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Deletes the Jump Url Settings
 */
public class DeleteJumpUrlSettingsServlet extends JsonServlet{

    private JumpUrlSettingsDAO jumpUrlSettingsDAO;

    public DeleteJumpUrlSettingsServlet(JumpUrlSettingsDAO jumpUrlSettingsDAO) {
        this.jumpUrlSettingsDAO = jumpUrlSettingsDAO;
    }

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        if (id == null) {
            sendError_WrongParams(resp, "Parameter id should not be empty.");
            return;
        }
        if (jumpUrlSettingsDAO.deleteJumpUrlSettings(Integer.parseInt(id))){
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
