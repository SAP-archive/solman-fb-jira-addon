package com.sap.mango.jiraintegration.solman.webservices.issuetypemapping;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.jira.config.IssueTypeManager;
import com.atlassian.jira.project.ProjectManager;
import com.sap.mango.jiraintegration.core.JsonServlet;
import com.sap.mango.jiraintegration.solman.entities.issuetype.IssueTypeMapping;
import com.sap.mango.jiraintegration.solman.entities.issuetype.IssueTypeMappingBean;
import com.sap.mango.jiraintegration.solman.entities.issuetype.IssueTypeMappingDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Web service, that adds a mapper (SolMan Process Type <-> Jira Issue Type)
 * in the jira database.
 * Url: /getProcessTypeMapping
 * Request Parameters <p>
 * solManCustGuiD : the Solution Manager Customer GuiD
 * </p>
 */
public class
GetIssueTypeMappingServlet extends JsonServlet {

    private final IssueTypeMappingDAO issueTypeMappingDAO;

    private final IssueTypeManager issueTypeManager;

    public GetIssueTypeMappingServlet(ActiveObjects ao, ProjectManager projectManager, IssueTypeMappingDAO issueTypeMappingDAO, IssueTypeManager issueTypeManager) {
        this.issueTypeMappingDAO = checkNotNull(issueTypeMappingDAO);
        this.issueTypeManager = checkNotNull(issueTypeManager);
    }

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String solManCustGuiD = req.getParameter("solManCustGuiD");
        if (solManCustGuiD == null) {
            sendError_WrongParams(resp, "Parameter solManCustGuiD should not be empty.");
            return;
        }

        final List<IssueTypeMapping> issueTypeMappings = issueTypeMappingDAO.getIssueTypeMappings(solManCustGuiD);

        sendOK(resp, transformProjectMappings(issueTypeMappings));
    }

    private List<IssueTypeMappingBean> transformProjectMappings(List<IssueTypeMapping> issueTypeMappings) {
        List<IssueTypeMappingBean> issueTypeMappingsBeans = new ArrayList<>();
        for (IssueTypeMapping issueTypeMapping : issueTypeMappings) {
            IssueTypeMappingBean projectMappingBean = new IssueTypeMappingBean(issueTypeMapping.getID(), issueTypeMapping.getSolmanProcessType(),
                    issueTypeMapping.getJiraIssueType(), issueTypeManager.getIssueType(issueTypeMapping.getJiraIssueType().toString()).getName());
            issueTypeMappingsBeans.add(projectMappingBean);
        }
        return issueTypeMappingsBeans;
    }

    @Override
    public String requiredRoles() {
        return "jira-administrators";
    }
}
