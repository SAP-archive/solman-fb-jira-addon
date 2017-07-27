package com.sap.mango.jiraintegration.solman.webservices.copyconfiguration;

import com.atlassian.jira.project.ProjectManager;
import com.sap.mango.jiraintegration.core.JsonServlet;
import com.sap.mango.jiraintegration.solman.entities.priority.PriorityMapping;
import com.sap.mango.jiraintegration.solman.entities.priority.PriorityMappingDAO;
import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsAO;
import com.sap.mango.jiraintegration.solman.entities.issuestatus.IssueStatusMapping;
import com.sap.mango.jiraintegration.solman.entities.issuestatus.IssueStatusMappingDAO;
import com.sap.mango.jiraintegration.solman.entities.issuetype.IssueTypeMapping;
import com.sap.mango.jiraintegration.solman.entities.issuetype.IssueTypeMappingDAO;
import com.sap.mango.jiraintegration.solman.entities.projecttype.ProjectMapping;
import com.sap.mango.jiraintegration.solman.entities.projecttype.ProjectMappingDAO;
import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Web service, that executes configuration copy for a Solution Manager connection
 * in the jira database.</br>
 * Url: /copyIssueConfiguration</br>
 * Request Parameters <p>
 *     solManCustGuiD : the Solution Manager Customer GuiD <br/>
 *     solManProcessType : the Solution Manager Process type <br/>
 *     overwrite : Flag, that we use in case of existing configuration for the Destination Solution Manager GuiD <br/>
 * </p>
 *
 */
public class CopyIssueConfigurationServlet extends JsonServlet {

    private final SolmanParamsDAO solmanParamsDAO;

    private final ProjectMappingDAO projectMappingDAO;
    private final IssueTypeMappingDAO issueTypeMappingDAO;
    private final IssueStatusMappingDAO issueStatusMappingDAO;
    private final PriorityMappingDAO priorityMappingDAO;
    private final ProjectManager projectManager;

    public CopyIssueConfigurationServlet(SolmanParamsDAO solmanParamsDAO, ProjectMappingDAO projectMappingDAO, IssueTypeMappingDAO issueTypeMappingDAO, IssueStatusMappingDAO issueStatusMappingDAO, ProjectManager projectManager,
                                         PriorityMappingDAO priorityMappingDAO) {
        this.solmanParamsDAO = checkNotNull(solmanParamsDAO);
        this.projectMappingDAO = checkNotNull(projectMappingDAO);
        this.issueTypeMappingDAO = checkNotNull(issueTypeMappingDAO);
        this.issueStatusMappingDAO = checkNotNull(issueStatusMappingDAO);
        this.projectManager = checkNotNull(projectManager);
        this.priorityMappingDAO = checkNotNull(priorityMappingDAO);
    }

    /**
     * Executes a validation on input parameters and creates field mapping in jira database
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String solManCustGuiD = req.getParameter("solManCustGuiD");
        if (solManCustGuiD == null || solManCustGuiD.isEmpty()) {
           sendError_WrongParams(resp, "Parameter solManCustGuiD should not be empty.");
           return;
        }
        String solManCustGuiDDest = req.getParameter("solManCustGuiDDest");
        if (solManCustGuiDDest == null || solManCustGuiDDest.isEmpty()) {
            sendError_WrongParams(resp, "Parameter solManCustGuiDDest should not be empty.");
            return;
        }

        String overwrite  = req.getParameter("overwrite");

        if (overwrite == null || overwrite.isEmpty()) {
            sendError_WrongParams(resp, "Parameter overwrite should not be empty.");
            return;
        }
        boolean overwriteflg = Boolean.parseBoolean(overwrite);


        List<SolmanParamsAO> solmanParams = solmanParamsDAO.getSolmanParamsByGuid(solManCustGuiD);

        if (solmanParams == null || solmanParams.isEmpty()) {
            sendError_WrongParams(resp, "Not existing SolmanConnection with GuiD = " + solManCustGuiD);
            return;
        }

        List<SolmanParamsAO> solmanParamsDest = solmanParamsDAO.getSolmanParamsByGuid(solManCustGuiDDest);

        if (solmanParamsDest == null || solmanParamsDest.isEmpty()) {
            sendError_WrongParams(resp, "Not existing SolmanConnection with GuiD = " + solManCustGuiDDest);
            return;
        }

        final List<ProjectMapping> projectMappingsDest = projectMappingDAO.getProjectMappings(solManCustGuiDDest);
        final List<IssueTypeMapping> issueTypeMappingsDest = issueTypeMappingDAO.getIssueTypeMappings(solManCustGuiDDest);
        final List<IssueStatusMapping> issueStatusMappingsDest = issueStatusMappingDAO.getIssueStatusMappings(solManCustGuiDDest);
        final List<PriorityMapping> priorityMappingsDest = priorityMappingDAO.getPriorityMappings(solManCustGuiDDest);

        if ((!projectMappingsDest.isEmpty() ||
             !issueTypeMappingsDest.isEmpty() ||
             !issueStatusMappingsDest.isEmpty()
            )&& !overwriteflg ){
            sendError_WrongParams(resp, "Overwrite");
            return;
        }


        for (ProjectMapping projectMapping : projectMappingsDest) {
            projectMappingDAO.deleteProjectTypeMapping(projectMapping.getID());
        }
        for (IssueTypeMapping issueTypeMapping : issueTypeMappingsDest) {
            issueTypeMappingDAO.deleteIssueTypeMapping(issueTypeMapping.getID());
        }
        for (IssueStatusMapping issueStatusMapping : issueStatusMappingsDest) {
            issueStatusMappingDAO.deleteIssueStatusMapping(issueStatusMapping.getID());
        }
        for (PriorityMapping priorityMapping : priorityMappingsDest) {
            priorityMappingDAO.deletePriorityMapping(priorityMapping.getID());
        }

        final List<ProjectMapping> projectMappings = projectMappingDAO.getProjectMappings(solManCustGuiD);
        for (ProjectMapping projectMapping : projectMappings) {
            projectMappingDAO.saveUpdateProjectTypeMapping(solmanParamsDest.get(0),
                projectManager.getProjectObjByKey(projectMapping.getJiraProjectID()), projectMapping.getSolmanProjectID()
            );
        }
        final List<IssueTypeMapping> issueTypeMappings = issueTypeMappingDAO.getIssueTypeMappings(solManCustGuiD);
        for (IssueTypeMapping issueTypeMapping : issueTypeMappings) {
            issueTypeMappingDAO.createIssueTypeMapping(solmanParamsDest.get(0),
                    issueTypeMapping.getJiraIssueType(), issueTypeMapping.getSolmanProcessType()
            );
        }
        final List<IssueStatusMapping> issueStatusMappings = issueStatusMappingDAO.getIssueStatusMappings(solManCustGuiD);
        for (IssueStatusMapping issueStatusMapping : issueStatusMappings) {
            issueStatusMappingDAO.saveIssueStatusMapping(solmanParamsDest.get(0),
                issueStatusMapping.getSolmanProcessType(),  issueStatusMapping.getSolmanStatus(), issueStatusMapping.getJiraTransition()
            );
        }

        final List<PriorityMapping> priorityMappings = priorityMappingDAO.getPriorityMappings(solManCustGuiD);
        for (PriorityMapping priorityMapping : priorityMappings) {
            priorityMappingDAO.saveUpdatePriorityMapping(solmanParamsDest.get(0),
                    priorityMapping.getSolmanPriority(), priorityMapping.getJiraPriority());
        }

        sendOK(resp, "nice");
    }

    @Override
    public String requiredRoles() {
        return "jira-administrators";
    }
}
