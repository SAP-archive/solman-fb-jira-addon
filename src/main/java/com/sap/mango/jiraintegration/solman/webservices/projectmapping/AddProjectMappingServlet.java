package com.sap.mango.jiraintegration.solman.webservices.projectmapping;

import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.sap.mango.jiraintegration.core.JsonServlet;
import com.sap.mango.jiraintegration.solman.entities.projecttype.ProjectMappingDAO;
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

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Web service, that adds a mapper (SolMan Project ID <-> Jira Project Key)
 * in the jira database.</br>
 * Url: /addProjectTypeMapping</br>
 * Request Parameters <p>
 *     solManCustGuiD : the Solution Manager Customer GuiD <br/>
 *     solManProjectID : the Solution Manager Project ID <br/>
 *     jiraProjectID : Jira Project ID <br/>
 * </p>
 *
 */
public class AddProjectMappingServlet extends JsonServlet {

    private final SolmanParamsDAO solmanParamsDAO;

    private final ProjectManager projectManager;

    private final ProjectMappingDAO projectMappingDAO;

    public AddProjectMappingServlet(SolmanParamsDAO solmanParamsDAO, ProjectManager projectManager, ProjectMappingDAO projectMappingDAO) {
        this.solmanParamsDAO = checkNotNull(solmanParamsDAO);
        this.projectManager = checkNotNull(projectManager);
        this.projectMappingDAO = checkNotNull(projectMappingDAO);
    }

    /**
     * Executes a validation on input parameters and creates ProjectMapping in jira database
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        AddProjectMappingRequestBean addProjectMappingRequestBean = JsonEncoder.mapper.readValue(req.getParameter("projectMapping"), AddProjectMappingRequestBean.class);

        if (!req.getMethod().equals(HttpMethod.POST)) {
            sendError(HttpStatus.SC_METHOD_NOT_ALLOWED, resp, "Expected POST request");
            return;
        }

        String solManCustGuiD = addProjectMappingRequestBean.getSolManCustGuiD();
        if (solManCustGuiD == null) {
           sendError_WrongParams(resp, "Parameter solManCustGuiD should not be empty.");
           return;
        }
        String solManProjectID  = addProjectMappingRequestBean.getSolmanProjectID();
        if (solManProjectID == null) {
            sendError_WrongParams(resp, "Parameter solManProjectID should not be empty.");
            return;
        }
        String jiraProjectID = addProjectMappingRequestBean.getJiraProjectID();
        if (jiraProjectID == null) {
            sendError_WrongParams(resp, "Parameter jiraProjectID should not be empty.");
            return;
        }
        List<SolmanParamsAO> solmanParams = solmanParamsDAO.getSolmanParamsByGuid(solManCustGuiD);

        if (solmanParams == null || solmanParams.isEmpty()) {
            sendError_WrongParams(resp, "Not existing SolmanConnection with GuiD = " + solManCustGuiD);
            return;
        }
        Project project = projectManager.getProjectObjByKey(jiraProjectID);

        if (project == null) {
            sendError_WrongParams(resp, "Not existing project with jiraProjectID = " + jiraProjectID);
            return;
        }

        Integer projectMappingID = projectMappingDAO.saveUpdateProjectTypeMapping(solmanParams.get(0), project, solManProjectID);

        resp.setHeader("projectMappingID", projectMappingID.toString());

        sendOK(resp, "nice");
    }

    @Override
    public String requiredRoles() {
        return "jira-administrators";
    }
}
