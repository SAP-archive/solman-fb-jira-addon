package com.sap.mango.jiraintegration.solman.webservices.partnerfieldmapping;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.sap.mango.jiraintegration.core.JsonServlet;
import com.sap.mango.jiraintegration.solman.entities.partnerfieldmapping.PartnerFieldMappingBean;
import com.sap.mango.jiraintegration.solman.entities.partnerfieldmapping.PartnerFieldMappingDAO;
import com.sap.mango.jiraintegration.solman.entities.partnerfieldmapping.PartnerFldMap;

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
public class GetPartnerFieldMappingServlet extends JsonServlet {

    private final PartnerFieldMappingDAO fieldMappingDAO;

    public GetPartnerFieldMappingServlet(ActiveObjects ao, PartnerFieldMappingDAO fieldMappingDAO) {
        this.fieldMappingDAO = checkNotNull(fieldMappingDAO);
    }

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String solManCustGuiD = req.getParameter("solManCustGuiD");
        if (solManCustGuiD == null) {
            sendError_WrongParams(resp, "Parameter solManCustGuiD should not be empty.");
            return;
        }

        final List<PartnerFldMap> fieldMappings = fieldMappingDAO.getFieldMappings(solManCustGuiD);

        sendOK(resp, transformProjectMappings(fieldMappings));
    }

    private List<PartnerFieldMappingBean> transformProjectMappings(List<PartnerFldMap> fieldMappings) {
        List<PartnerFieldMappingBean> fieldMappingsBeans = new ArrayList<>();
        for (PartnerFldMap fieldMapping : fieldMappings) {
            PartnerFieldMappingBean fieldMappingBean = new PartnerFieldMappingBean(fieldMapping.getID(), fieldMapping.getSolmanProcessType(),
                    fieldMapping.getSolmanPartnerFunction(), fieldMapping.getSolmanField(), fieldMapping.getJiraField());
            fieldMappingsBeans.add(fieldMappingBean);
        }
        return fieldMappingsBeans;
    }

    @Override
    public String requiredRoles() {
        return "jira-administrators";
    }
}
