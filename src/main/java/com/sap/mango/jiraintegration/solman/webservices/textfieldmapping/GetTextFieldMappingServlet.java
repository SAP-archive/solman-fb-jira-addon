package com.sap.mango.jiraintegration.solman.webservices.textfieldmapping;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.sap.mango.jiraintegration.core.JsonServlet;
import com.sap.mango.jiraintegration.solman.entities.textfieldmapping.TextFieldMapping;
import com.sap.mango.jiraintegration.solman.entities.textfieldmapping.TextFieldMappingBean;
import com.sap.mango.jiraintegration.solman.entities.textfieldmapping.TextFieldMappingDAO;

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
 * Url: /getTextFieldMapping
 * Request Parameters <p>
 * solManCustGuiD : the Solution Manager Customer GuiD
 * </p>
 */
public class GetTextFieldMappingServlet extends JsonServlet {

    private final TextFieldMappingDAO fieldMappingDAO;

    public GetTextFieldMappingServlet(ActiveObjects ao, TextFieldMappingDAO fieldMappingDAO) {
        this.fieldMappingDAO = checkNotNull(fieldMappingDAO);
    }

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String solManCustGuiD = req.getParameter("solManCustGuiD");
        if (solManCustGuiD == null) {
            sendError_WrongParams(resp, "Parameter solManCustGuiD should not be empty.");
            return;
        }

        final List<TextFieldMapping> fieldMappings = fieldMappingDAO.getFieldMappings(solManCustGuiD);

        sendOK(resp, transformProjectMappings(fieldMappings));
    }

    private List<TextFieldMappingBean> transformProjectMappings(List<TextFieldMapping> fieldMappings) {
        List<TextFieldMappingBean> fieldMappingsBeans = new ArrayList<>();
        for (TextFieldMapping fieldMapping : fieldMappings) {
            TextFieldMappingBean fieldMappingBean = new TextFieldMappingBean(fieldMapping.getID(), fieldMapping.getSolmanProcessType(),
                    fieldMapping.getSolmanTextType(), fieldMapping.getJiraField());
            fieldMappingsBeans.add(fieldMappingBean);
        }
        return fieldMappingsBeans;
    }

    @Override
    public String requiredRoles() {
        return "jira-administrators";
    }
}
