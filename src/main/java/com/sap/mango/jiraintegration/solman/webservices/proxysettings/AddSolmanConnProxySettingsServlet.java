package com.sap.mango.jiraintegration.solman.webservices.proxysettings;

import com.sap.mango.jiraintegration.core.JsonServlet;
import com.sap.mango.jiraintegration.solman.entities.proxysettings.ProxySettingsDAO;
import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsAO;
import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsDAO;
import com.sap.mango.jiraintegration.utils.JsonEncoder;
import org.apache.commons.httpclient.HttpStatus;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;
import java.io.IOException;
import java.util.List;

/**
 * Web service, that adds a ProxySettings to SolutionManager Connection
 */
public class AddSolmanConnProxySettingsServlet extends JsonServlet {

    private final SolmanParamsDAO solmanParamsDAO;

    private final ProxySettingsDAO proxySettingsDAO;

    public AddSolmanConnProxySettingsServlet(final SolmanParamsDAO solmanParamsDAO, final ProxySettingsDAO proxySettingsDAO) {
        this.solmanParamsDAO = solmanParamsDAO;
        this.proxySettingsDAO = proxySettingsDAO;
    }

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SolmanProxySettingsBean solmanProxySettingsBean = JsonEncoder.mapper.readValue(req.getParameter("proxySettings"), SolmanProxySettingsBean.class);

        if (!req.getMethod().equals(HttpMethod.POST)) {
            sendError(HttpStatus.SC_METHOD_NOT_ALLOWED, resp, "Expected POST request");
            return;
        }

        String solManCustGuiD = solmanProxySettingsBean.getSolManCustGuiD();
        if (solManCustGuiD == null) {
            sendError_WrongParams(resp, "Parameter solManCustGuiD should not be empty.");
            return;
        }
        String proxyHost  = solmanProxySettingsBean.getProxyHost();
        if (proxyHost == null) {
            sendError_WrongParams(resp, "Parameter proxyHost should not be empty.");
            return;
        }

        List<SolmanParamsAO> solmanParams = solmanParamsDAO.getSolmanParamsByGuid(solManCustGuiD);

        if (solmanParams == null || solmanParams.isEmpty()) {
            sendError_WrongParams(resp, "Not existing SolmanConnection with GuiD = " + solManCustGuiD);
            return;
        }

        Integer proxySettingsId = proxySettingsDAO.saveUpdateProxySettings(solmanParams.get(0), proxyHost, solmanProxySettingsBean.getPort());

        resp.setHeader("proxySettingsID", proxySettingsId.toString());

        sendOK(resp, "nice");
    }

    @Override
    public String requiredRoles() {
        return "jira-administrators";
    }

    @Override
    protected boolean isLogged(HttpServletRequest request) {
        return super.isLogged(request);
    }

    @Override
    protected String getUserName(HttpServletRequest request) {
        return super.getUserName(request);
    }

    @Override
    protected String[] getUserRoles(HttpServletRequest request) {
        return super.getUserRoles(request);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.service(req, resp);
    }
}
