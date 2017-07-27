package com.sap.mango.jiraintegration.solman.webservices.proxysettings;

import com.sap.mango.jiraintegration.core.JsonServlet;
import com.sap.mango.jiraintegration.solman.entities.proxysettings.ProxySettings;
import com.sap.mango.jiraintegration.solman.entities.proxysettings.ProxySettingsDAO;
import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet, that gets Sol Man Proxy Settings
 */
public class GetSolmanConnProxySettings extends JsonServlet {

    private final SolmanParamsDAO solmanParamsDAO;

    private final ProxySettingsDAO proxySettingsDAO;

    public GetSolmanConnProxySettings(final SolmanParamsDAO solmanParamsDAO, final ProxySettingsDAO proxySettingsDAO) {
        this.solmanParamsDAO = solmanParamsDAO;
        this.proxySettingsDAO = proxySettingsDAO;
    }

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<ProxySettings> proxySettings = proxySettingsDAO.getListProxySettings();
        sendOK(resp, tranformListProxySettings(proxySettings));
    }

    private List<SolmanProxySettingsBean> tranformListProxySettings(List<ProxySettings> listProxySettings) {
        List<SolmanProxySettingsBean> solmanProxySettingsBeans = new ArrayList<>();
        for (ProxySettings proxySettings : listProxySettings) {
            solmanProxySettingsBeans.add(new SolmanProxySettingsBean(proxySettings.getID(), proxySettings.getSolmanParams().getCustomerGiud(), proxySettings.getProxyHost(), proxySettings.getPort()));
        }
        return solmanProxySettingsBeans;
    }

    @Override
    public String requiredRoles() {
        return "jira-administrators";
    }
}
