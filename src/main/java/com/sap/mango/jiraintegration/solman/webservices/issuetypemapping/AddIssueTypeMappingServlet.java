package com.sap.mango.jiraintegration.solman.webservices.issuetypemapping;

import com.sap.mango.jiraintegration.core.JsonServlet;
import com.sap.mango.jiraintegration.solman.entities.issuetype.IssueTypeMappingDAO;
import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsAO;
import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Web service, that adds a mapper (SolMan Process type <-> Jira Issue type)
 * in the jira database.</br>
 * Url: /addIssueTypeMapping</br>
 * Request Parameters <p>
 *     solManCustGuiD : the Solution Manager Customer GuiD <br/>
 *     solManProcessType : the Solution Manager Process type <br/>
 *     jiraIssueType : Jira Issue type <br/>
 * </p>
 *
 */
public class AddIssueTypeMappingServlet extends JsonServlet {

    private final SolmanParamsDAO solmanParamsDAO;

    private final IssueTypeMappingDAO issueTypeMappingDAO;

    public AddIssueTypeMappingServlet(SolmanParamsDAO solmanParamsDAO, IssueTypeMappingDAO issueTypeMappingDAO) {
        this.solmanParamsDAO = checkNotNull(solmanParamsDAO);
        this.issueTypeMappingDAO = checkNotNull(issueTypeMappingDAO);
    }

    /**
     * Executes a validation on input parameters and creates IssueTypeMapping in jira database
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String solManCustGuiD = req.getParameter("solManCustGuiD");
        if (solManCustGuiD == null) {
           sendError_WrongParams(resp, "Parameter solManCustGuiD should not be empty.");
           return;
        }
        String solManProcessType  = req.getParameter("solManProcessType");
        if (solManProcessType == null) {
            sendError_WrongParams(resp, "Parameter solManProcessType should not be empty.");
            return;
        }
        String jiraIssueType = req.getParameter("jiraIssueType");
        if (jiraIssueType == null) {
            sendError_WrongParams(resp, "Parameter jiraIssueType should not be empty.");
            return;
        }
        List<SolmanParamsAO> solmanParams = solmanParamsDAO.getSolmanParamsByGuid(solManCustGuiD);

        if (solmanParams == null || solmanParams.isEmpty()) {
            sendError_WrongParams(resp, "Not existing SolmanConnection with GuiD = " + solManCustGuiD);
            return;
        }

        final int issueType;
        try {
            issueType = Integer.parseInt(jiraIssueType);
        }catch (NumberFormatException e){
            sendError_WrongParams(resp, "Wrong issue type: " + jiraIssueType);
            return;
        }

        Integer processMappingID = issueTypeMappingDAO.createIssueTypeMapping(solmanParams.get(0), issueType, solManProcessType);

        resp.setHeader("processMappingID", processMappingID.toString());

        sendOK(resp, "nice");
    }

    @Override
    public String requiredRoles() {
        return "jira-administrators";
    }
}
