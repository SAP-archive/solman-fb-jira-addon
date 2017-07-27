package com.sap.mango.jiraintegration.solman.webservices.solmanconnections;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.sap.mango.jiraintegration.core.JsonServlet;
import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Web service, that gets all Solution Manager connections from jira database.<br/>
 * Url: /spGet
 */
public class SolmanParamsGetWS extends JsonServlet {


    private SolmanParamsDAO solmanParamsDAO;

    public SolmanParamsGetWS(ActiveObjects ao, SolmanParamsDAO solmanParamsDAO){
        this.solmanParamsDAO = solmanParamsDAO;
    }

    @Override
    public void process(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        sendOK(response, solmanParamsDAO.getSolmanParamsAll(false));
    }

    @Override
    public String requiredRoles() {
        return "jira-administrators";
    }

}
