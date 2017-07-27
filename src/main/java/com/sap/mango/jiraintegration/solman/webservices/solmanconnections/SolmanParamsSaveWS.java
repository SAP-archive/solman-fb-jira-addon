package com.sap.mango.jiraintegration.solman.webservices.solmanconnections;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sap.mango.jiraintegration.core.JsonServlet;
import com.sap.mango.jiraintegration.solman.beans.SolmanParams;
import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsDAO;
import com.sap.mango.jiraintegration.utils.JsonEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/*
 * Web service, that saves a Solution Manager connection information in the jira database.
 * in the jira database.<br/>
 * Url: /spSave<br/>
 * Request Parameters <p>
 *     solmanUrl : the url of Solution Manager  <br/>
 *     userName : username, that the plugin use to authentication a call to Solution Manager <br/>
 *     password : password, that the plugin use to authentication a call to Solution Manager <br/>
 *     customerGuid : the Solution Manager Customer GuiD <br/>
 *     jumpUrl : The url for solution manager issues <br/>
 *     customerDescription : Description for the Solution Manager customer <br/>
 * </p>
 */
public class
SolmanParamsSaveWS extends JsonServlet {

    private SolmanParamsDAO solmanParamsDAO;

    public SolmanParamsSaveWS(SolmanParamsDAO solmanParamsDAO) {
        this.solmanParamsDAO = checkNotNull(solmanParamsDAO);
    }


    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String sp = req.getParameter("SolmanParams");
        if (sp == null || sp.isEmpty()) {
            sendError_WrongParams(resp, "Parameter SolmanParams must not be empty");
            return;
        }
        final List<SolmanParams> solmanParams = JsonEncoder.mapper.
                readValue(sp, new TypeReference<List<SolmanParams>>() {
                });

        solmanParamsDAO.saveSolmanParams(solmanParams);
        sendOK(resp, "nice");
    }


    @Override
    public String requiredRoles() {
        return "jira-administrators";
    }

}
