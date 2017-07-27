package com.sap.mango.jiraintegration.solman.webservices.proxysettings;

import com.sap.mango.jiraintegration.core.JsonServlet;
import com.sap.mango.jiraintegration.solman.entities.proxysettings.ProxySettingsDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet, that deletes a proxy settings.
 */
public class DeleteSolmanConnProxySettingsServlet extends JsonServlet{

    private ProxySettingsDAO proxySettingsDAO;

    public DeleteSolmanConnProxySettingsServlet(ProxySettingsDAO proxySettingsDAO) {
        this.proxySettingsDAO = proxySettingsDAO;
    }

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        if (id == null) {
            sendError_WrongParams(resp, "Parameter id should not be empty.");
            return;
        }

        if (proxySettingsDAO.deleteProxySettings(Integer.parseInt(id))){
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
