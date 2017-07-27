package com.sap.mango.jiraintegration.solman.webservices.solmanconnections;

import com.sap.mango.jiraintegration.core.JsonServlet;
import com.sap.mango.jiraintegration.solman.beans.SolmanParams;
import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by c5118782 on 5/19/16.
 */
public class GetSolmanParamServlet extends JsonServlet {

    private SolmanParamsDAO solmanParamsDAO;

    public GetSolmanParamServlet(SolmanParamsDAO solmanParamsDAO) {
        this.solmanParamsDAO = solmanParamsDAO;
    }

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String solManCustGuid = req.getParameter("solManCustGuid");
        List<SolmanParams> solmanParamsAOList = solmanParamsDAO.getSolmanParamsByGuid(solManCustGuid, true);
        sendOK(resp, solmanParamsAOList);
    }

    @Override
    public String requiredRoles() {
        return "jira-administrators";
    }
}
