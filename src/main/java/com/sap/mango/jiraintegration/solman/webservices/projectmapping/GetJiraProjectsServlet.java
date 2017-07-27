package com.sap.mango.jiraintegration.solman.webservices.projectmapping;

import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.sap.mango.jiraintegration.core.JsonServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet, that returns all jira projects. It will be used by the ui.
 */
public class GetJiraProjectsServlet extends JsonServlet {

    private ProjectManager projectManager;

    public GetJiraProjectsServlet(ProjectManager projectManager) {
        this.projectManager = projectManager;
    }

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Project> jiraProjects = projectManager.getProjectObjects();
        sendOK(resp, getJiraProjectResultBeans(jiraProjects));
    }

    private List<JiraProjectResultBean> getJiraProjectResultBeans(List<Project> projects) {
        List<JiraProjectResultBean> projectResultBean = new ArrayList<>();
        for (Project project : projects) {
            JiraProjectResultBean jiraProjectResultBean = new JiraProjectResultBean(project.getKey(), project.getName());
            projectResultBean.add(jiraProjectResultBean);
        }

        return projectResultBean;
    }

    @Override
    public String requiredRoles() {
        return "jira-administrators";
    }
}
