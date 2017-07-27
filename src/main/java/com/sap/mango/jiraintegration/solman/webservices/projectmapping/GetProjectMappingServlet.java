package com.sap.mango.jiraintegration.solman.webservices.projectmapping;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.jira.project.ProjectManager;
import com.sap.mango.jiraintegration.core.JsonServlet;
import com.sap.mango.jiraintegration.solman.entities.projecttype.ProjectMapping;
import com.sap.mango.jiraintegration.solman.entities.projecttype.ProjectMappingBean;
import com.sap.mango.jiraintegration.solman.entities.projecttype.ProjectMappingDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Web service, that adds a mapper (SolMan Project ID <-> Jira Project Key)
 * in the jira database.
 * Url: /getProjectTypeMapping
 * Request Parameters <p>
 * solManCustGuiD : the Solution Manager Customer GuiD
 * </p>
 */
public class GetProjectMappingServlet extends JsonServlet {

    private final ProjectMappingDAO projectMappingDAO;

    public GetProjectMappingServlet(ActiveObjects ao, ProjectManager projectManager, ProjectMappingDAO projectMappingDAO) {
        this.projectMappingDAO = checkNotNull(projectMappingDAO);
    }

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String solManCustGuiD = req.getParameter("solManCustGuiD");
        if (solManCustGuiD == null) {
            sendError_WrongParams(resp, "Parameter solManCustGuiD should not be empty.");
            return;
        }

        List<ProjectMapping> projectMappingList = projectMappingDAO.getProjectMappings(solManCustGuiD);

        List<ProjectMappingBean> projectMappingBeanList = transformProjectMappings(projectMappingList);

        sendOK(resp, projectMappingBeanList);
    }

    private List<ProjectMappingBean> transformProjectMappings(List<ProjectMapping> projectMappings) {
        List<ProjectMappingBean> projectMappingBeans = new ArrayList<>();
        for (ProjectMapping projectMapping : projectMappings) {
            ProjectMappingBean projectMappingBean = new ProjectMappingBean(projectMapping.getID(), projectMapping.getSolmanProjectID(),
                    projectMapping.getJiraProjectID());
            projectMappingBeans.add(projectMappingBean);
        }
        return projectMappingBeans;
    }

    @Override
    public String requiredRoles() {
        return "jira-administrators";
    }
}
