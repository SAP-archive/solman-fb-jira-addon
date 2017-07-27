package com.sap.mango.jiraintegration.solman.webservices;

import com.sap.mango.jiraintegration.core.JsonServlet;
import com.sap.mango.jiraintegration.core.data.Either;
import com.sap.mango.jiraintegration.core.httpclient.RestClient;
import com.sap.mango.jiraintegration.core.httpclient.SolmanClient;
import com.sap.mango.jiraintegration.solman.beans.SolmanParams;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Emo on 3.1.2016 г..
 */
public class IssueStatusChangeServlet extends JsonServlet
{
    @Override public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String solManGuiD = req.getParameter("solManGuiDCFName");
        String status = req.getParameter("status");
        final Either<RestClient.ServiceError, String> serviceErrorStringEither = SolmanClient.solmanSendStatus(
                new SolmanParams("https://ldcisr7.wdf.sap.corp:44311/sap/opu/odata/SALM/EXT_INTEGRATION_SRV/StatusSet",
                        "C5236055", "demo1234", "0123456789", "Customer Description", "410", "", ""), solManGuiD, status, null);
        if (serviceErrorStringEither.isLeft())
        {
            sendError(resp, serviceErrorStringEither.toLeft());
        }
        else
        {
            sendOK(resp, serviceErrorStringEither.toRight());
        }
    }

    public String requiredRoles()
    {
        return "jira-administrators";
    }
}
