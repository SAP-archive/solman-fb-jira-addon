package com.sap.mango.jiraintegration.solman.webservices.jumpurl;

import com.sap.mango.jiraintegration.core.JsonServlet;
import com.sap.mango.jiraintegration.solman.entities.jumpurl.JumpUrlSettingsBean;
import com.sap.mango.jiraintegration.solman.entities.jumpurl.JumpUrlSettingsDAO;
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
 * Servlet, that adds Jump Url Settings
 */
public class AddJumpUrlSettingsServlet extends JsonServlet {

    private SolmanParamsDAO solmanParamsDAO;

    private JumpUrlSettingsDAO jumpUrlSettingsDAO;

    public AddJumpUrlSettingsServlet(SolmanParamsDAO solmanParamsDAO, JumpUrlSettingsDAO jumpUrlSettingsDAO) {
        this.solmanParamsDAO = solmanParamsDAO;
        this.jumpUrlSettingsDAO = jumpUrlSettingsDAO;
    }

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JumpUrlSettingsBean jumpUrlSettingsBean = JsonEncoder.mapper.readValue(req.getParameter("jumpUrlSettings"), JumpUrlSettingsBean.class);

        if (!req.getMethod().equals(HttpMethod.POST)) {
            sendError(HttpStatus.SC_METHOD_NOT_ALLOWED, resp, "Expected POST request");
            return;
        }
        String solManCustGuiD = jumpUrlSettingsBean.getSolManCustGuiD();
        if (solManCustGuiD == null) {
            sendError_WrongParams(resp, "Parameter solManCustGuiD should not be empty.");
            return;
        }
        String workPackageAppJumpUrl  = jumpUrlSettingsBean.getWorkPackageAppJumpUrl();
        if (workPackageAppJumpUrl == null) {
            sendError_WrongParams(resp, "Parameter workPackageAppJumpUrl should not be empty.");
            return;
        }
        String workItemAppJumpUrl = jumpUrlSettingsBean.getWorkItemAppJumpUrl();
        if (workItemAppJumpUrl == null) {
            sendError_WrongParams(resp, "Parameter workItemAppJumpUrl should not be empty.");
            return;
        }
        List<SolmanParamsAO> solmanParams = solmanParamsDAO.getSolmanParamsByGuid(solManCustGuiD);

        if (solmanParams == null || solmanParams.isEmpty()) {
            sendError_WrongParams(resp, "Not existing SolmanConnection with GuiD = " + solManCustGuiD);
            return;
        }

        Integer jumpUrlSettingsID = jumpUrlSettingsDAO.saveJumpUrlSettings(solmanParams.get(0), workPackageAppJumpUrl, workItemAppJumpUrl);
        resp.setHeader("jumpUrlSettingsID", jumpUrlSettingsID.toString());
        sendOK(resp, "nice");
    }

    @Override
    public String requiredRoles() {
        return "jira-administrators";
    }
}
