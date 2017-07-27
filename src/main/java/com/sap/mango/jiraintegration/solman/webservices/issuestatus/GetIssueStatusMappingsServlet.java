package com.sap.mango.jiraintegration.solman.webservices.issuestatus;

import com.sap.mango.jiraintegration.core.JsonServlet;
import com.sap.mango.jiraintegration.solman.entities.issuestatus.IssueStatusMapping;
import com.sap.mango.jiraintegration.solman.entities.issuestatus.IssueStatusMappingBean;
import com.sap.mango.jiraintegration.solman.entities.issuestatus.IssueStatusMappingDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet, that returns all issue mappings for specific Solman connection
 */
public class GetIssueStatusMappingsServlet extends JsonServlet {

    private IssueStatusMappingDAO issueStatusMappingDAO;

    public GetIssueStatusMappingsServlet(IssueStatusMappingDAO issueStatusMappingDAO) {
        this.issueStatusMappingDAO = issueStatusMappingDAO;
    }

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String solManCustGuiD = req.getParameter("solManCustGuiD");
        if (solManCustGuiD == null) {
            sendError_WrongParams(resp, "Parameter solManCustGuiD should not be empty.");
            return;
        }

        List<IssueStatusMapping> issueStatusMappingList = issueStatusMappingDAO.getIssueStatusMappings(solManCustGuiD);

        List<IssueStatusMappingBean> issueStatusMappingBeanList = transformIssueMappings(issueStatusMappingList);

        sendOK(resp, issueStatusMappingBeanList);
    }

    @Override
    public String requiredRoles() {
        return "jira-administrators";
    }

    private List<IssueStatusMappingBean> transformIssueMappings(List<IssueStatusMapping> issueStatusMappings) {
        List<IssueStatusMappingBean> issueMappingBeans = new ArrayList<>();
        for (IssueStatusMapping issueStatusMapping : issueStatusMappings) {
            IssueStatusMappingBean issueStatusMappingBean = new IssueStatusMappingBean(issueStatusMapping.getID(), issueStatusMapping.getSolmanProcessType(),
                    issueStatusMapping.getSolmanStatus(), issueStatusMapping.getJiraTransition());
            issueMappingBeans.add(issueStatusMappingBean);
        }
        return issueMappingBeans;
    }
}
