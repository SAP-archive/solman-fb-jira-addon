package com.sap.mango.jiraintegration.solman.webservices.prioritymapping;

import com.atlassian.jira.config.PriorityManager;
import com.atlassian.jira.issue.priority.Priority;
import com.sap.mango.jiraintegration.core.JsonServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet, that returns all the jira priorities
 */
public class GetJiraPrioritiesServlet extends JsonServlet {

    private PriorityManager priorityManager;

    public GetJiraPrioritiesServlet(PriorityManager priorityManager) {
        this.priorityManager = priorityManager;
    }

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Priority> priorityList = priorityManager.getPriorities();
        List<JiraPriorityBean> priorityBeanList = new ArrayList<>();
        for (Priority priority : priorityList) {
            JiraPriorityBean jiraPriorityBean = new JiraPriorityBean(priority.getId(), priority.getName());
            priorityBeanList.add(jiraPriorityBean);
        }
        sendOK(resp, priorityBeanList);
    }

    @Override
    public String requiredRoles() {
        return "jira-administrators";
    }
}
