package com.sap.mango.jiraintegration.solman.webservices.fieldmapping;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.sap.mango.jiraintegration.core.JsonServlet;
import com.sap.mango.jiraintegration.solman.entities.fieldmapping.FieldMapping;
import com.sap.mango.jiraintegration.solman.entities.fieldmapping.FieldMappingBean;
import com.sap.mango.jiraintegration.solman.entities.fieldmapping.FieldMappingDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Web service, that get mapping
 * from the jira database.
 * Url: /getProcessTypeMapping
 * Request Parameters <p>
 * solManCustGuiD : the Solution Manager Customer GuiD
 * </p>
 */
public class GetFieldMappingServlet extends JsonServlet {

    private final FieldMappingDAO fieldMappingDAO;

    public GetFieldMappingServlet(ActiveObjects ao, FieldMappingDAO fieldMappingDAO) {
        this.fieldMappingDAO = checkNotNull(fieldMappingDAO);
    }

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String solManCustGuiD = req.getParameter("solManCustGuiD");
        if (solManCustGuiD == null) {
            sendError_WrongParams(resp, "Parameter solManCustGuiD should not be empty.");
            return;
        }

        final List<FieldMapping> fieldMappings = fieldMappingDAO.getFieldMappings(solManCustGuiD);

        sendOK(resp, transformProjectMappings(fieldMappings));
    }

    private List<FieldMappingBean> transformProjectMappings(List<FieldMapping> fieldMappings) {
        List<FieldMappingBean> fieldMappingsBeans = new ArrayList<>();
        for (FieldMapping fieldMapping : fieldMappings) {
            FieldMappingBean fieldMappingBean = new FieldMappingBean(fieldMapping.getID(), fieldMapping.getSolmanProcessType(),
                    fieldMapping.getSolmanField(), fieldMapping.getJiraField());
            fieldMappingsBeans.add(fieldMappingBean);
        }
        return fieldMappingsBeans;
    }

    @Override
    public String requiredRoles() {
        return "jira-administrators";
    }
}
