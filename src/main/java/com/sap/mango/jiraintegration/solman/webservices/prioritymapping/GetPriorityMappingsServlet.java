package com.sap.mango.jiraintegration.solman.webservices.prioritymapping;

import com.atlassian.jira.config.PriorityManager;
import com.sap.mango.jiraintegration.core.JsonServlet;
import com.sap.mango.jiraintegration.solman.entities.priority.PriorityMapping;
import com.sap.mango.jiraintegration.solman.entities.priority.PriorityMappingBean;
import com.sap.mango.jiraintegration.solman.entities.priority.PriorityMappingDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet, that returns all priority mappings for specific customer guid
 */
public class GetPriorityMappingsServlet extends JsonServlet {

    private PriorityMappingDAO priorityMappingDAO;

    private PriorityManager priorityManager;

    public GetPriorityMappingsServlet(PriorityMappingDAO priorityMappingDAO, PriorityManager priorityManager) {
        this.priorityMappingDAO = priorityMappingDAO;
        this.priorityManager = priorityManager;
    }

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String solManCustGuiD = req.getParameter("solManCustGuiD");
        if (solManCustGuiD == null) {
            sendError_WrongParams(resp, "Parameter solManCustGuiD should not be empty.");
            return;
        }
        List<PriorityMapping> priorityMappings = priorityMappingDAO.getPriorityMappings(solManCustGuiD);

        sendOK(resp, transformPriorityMappings(priorityMappings));
    }

    @Override
    public String requiredRoles() {
        return "jira-administrators";
    }

    private List<PriorityMappingBean> transformPriorityMappings(List<PriorityMapping> priorityMappings) {
        List<PriorityMappingBean> priorityMappingsList = new ArrayList<>();
        for (PriorityMapping priorityMapping : priorityMappings) {
            PriorityMappingBean priorityMappingBean = new PriorityMappingBean(priorityMapping.getID(), priorityMapping.getSolmanPriority(),
                    priorityManager.getPriority(priorityMapping.getJiraPriority()).getName());
            priorityMappingsList.add(priorityMappingBean);
        }
        return priorityMappingsList;
    }
}
