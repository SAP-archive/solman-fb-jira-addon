package com.sap.mango.jiraintegration.solman.webservices.jumpurl;

import com.sap.mango.jiraintegration.core.JsonServlet;
import com.sap.mango.jiraintegration.solman.entities.jumpurl.JumpUrlSettings;
import com.sap.mango.jiraintegration.solman.entities.jumpurl.JumpUrlSettingsBean;
import com.sap.mango.jiraintegration.solman.entities.jumpurl.JumpUrlSettingsDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet, that gets JumpUrl Settings for specific SolMan Customer
 */
public class GetJumpUrlSettingsServlet extends JsonServlet{

    private JumpUrlSettingsDAO jumpUrlSettingsDAO;

    public GetJumpUrlSettingsServlet(JumpUrlSettingsDAO jumpUrlSettingsDAO) {
        this.jumpUrlSettingsDAO = jumpUrlSettingsDAO;
    }

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<JumpUrlSettingsBean> jumpUrlSettingsBeanList = new ArrayList<>();
        List<JumpUrlSettings> jumpUrlSettingsList = jumpUrlSettingsDAO.getJumpUrlSettings();
        for (JumpUrlSettings jumpUrlSettings : jumpUrlSettingsList) {
            jumpUrlSettingsBeanList.add(new JumpUrlSettingsBean(jumpUrlSettings.getID(), jumpUrlSettings.getSolmanParams().getCustomerGiud(),
                    jumpUrlSettings.getWorkPackageAppJumpUrl(), jumpUrlSettings.getWorkItemAppJumpUrl()));
        }
        sendOK(resp, jumpUrlSettingsBeanList);
    }

    @Override
    public String requiredRoles() {
        return "jira-administrators";
    }
}
