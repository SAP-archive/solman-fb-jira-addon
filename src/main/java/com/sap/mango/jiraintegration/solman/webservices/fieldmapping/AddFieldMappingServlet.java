package com.sap.mango.jiraintegration.solman.webservices.fieldmapping;

import com.sap.mango.jiraintegration.core.JsonServlet;
import com.sap.mango.jiraintegration.solman.entities.fieldmapping.FieldMappingBean;
import com.sap.mango.jiraintegration.solman.entities.fieldmapping.FieldMappingDAO;
import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsAO;
import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsDAO;
import com.sap.mango.jiraintegration.utils.JsonEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Web service, that adds a mapper (SolMan field <-> Jira field)
 * in the jira database.</br>
 * Url: /addFieldMapping</br>
 * Request Parameters <p>
 *     solManCustGuiD : the Solution Manager Customer GuiD <br/>
 *     solManProcessType : the Solution Manager Process type <br/>
 *     jiraIssueType : Jira Issue type <br/>
 * </p>
 *
 */
public class AddFieldMappingServlet extends JsonServlet {

    private final SolmanParamsDAO solmanParamsDAO;

    private final FieldMappingDAO fieldMappingDAO;

    public AddFieldMappingServlet(SolmanParamsDAO solmanParamsDAO, FieldMappingDAO fieldMappingDAO) {
        this.solmanParamsDAO = checkNotNull(solmanParamsDAO);
        this.fieldMappingDAO = checkNotNull(fieldMappingDAO);
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
        String fieldMapping  = req.getParameter("fieldMapping");
        if (fieldMapping == null || fieldMapping.isEmpty()) {
            sendError_WrongParams(resp, "Parameter fieldMapping should not be empty.");
            return;
        }
        final FieldMappingBean fieldMappingData = JsonEncoder.mapper.
                readValue(fieldMapping, FieldMappingBean.class);


        List<SolmanParamsAO> solmanParams = solmanParamsDAO.getSolmanParamsByGuid(solManCustGuiD);

        if (solmanParams == null || solmanParams.isEmpty()) {
            sendError_WrongParams(resp, "Not existing SolmanConnection with GuiD = " + solManCustGuiD);
            return;
        }


        final Integer fieldMappingID = fieldMappingDAO.createFieldMapping(solmanParams.get(0), fieldMappingData);

        resp.setHeader("fieldMappingID", fieldMappingID.toString());

        sendOK(resp, "nice");
    }

    @Override
    public String requiredRoles() {
        return "jira-administrators";
    }
}
