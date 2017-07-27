package com.sap.mango.jiraintegration.solman.webservices.appointmentmapping;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.sap.mango.jiraintegration.core.JsonServlet;
import com.sap.mango.jiraintegration.solman.entities.appointmentmapping.AppointmentMapping;
import com.sap.mango.jiraintegration.solman.entities.appointmentmapping.AppointmentMappingBean;
import com.sap.mango.jiraintegration.solman.entities.appointmentmapping.AppointmentMappingDAO;

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
 * Url: /getAppointmentMapping
 * Request Parameters <p>
 * solManCustGuiD : the Solution Manager Customer GuiD
 * </p>
 */
public class GetAppointmentMappingServlet extends JsonServlet {

    private final AppointmentMappingDAO fieldMappingDAO;

    public GetAppointmentMappingServlet(ActiveObjects ao, AppointmentMappingDAO fieldMappingDAO) {
        this.fieldMappingDAO = checkNotNull(fieldMappingDAO);
    }

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String solManCustGuiD = req.getParameter("solManCustGuiD");
        if (solManCustGuiD == null) {
            sendError_WrongParams(resp, "Parameter solManCustGuiD should not be empty.");
            return;
        }

        final List<AppointmentMapping> fieldMappings = fieldMappingDAO.getFieldMappings(solManCustGuiD);

        sendOK(resp, transformProjectMappings(fieldMappings));
    }

    private List<AppointmentMappingBean> transformProjectMappings(List<AppointmentMapping> fieldMappings) {
        List<AppointmentMappingBean> fieldMappingsBeans = new ArrayList<>();
        for (AppointmentMapping fieldMapping : fieldMappings) {
            AppointmentMappingBean fieldMappingBean = new AppointmentMappingBean(fieldMapping.getID(), fieldMapping.getSolmanProcessType(),
                    fieldMapping.getSolmanAppointment(), fieldMapping.getJiraField());
            fieldMappingsBeans.add(fieldMappingBean);
        }
        return fieldMappingsBeans;
    }

    @Override
    public String requiredRoles() {
        return "jira-administrators";
    }
}
