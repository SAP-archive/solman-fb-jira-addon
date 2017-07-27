package com.sap.mango.jiraintegration.solman.webservices.copyconfiguration;

import com.sap.mango.jiraintegration.core.JsonServlet;
import com.sap.mango.jiraintegration.solman.entities.appointmentmapping.AppointmentMapping;
import com.sap.mango.jiraintegration.solman.entities.appointmentmapping.AppointmentMappingBean;
import com.sap.mango.jiraintegration.solman.entities.appointmentmapping.AppointmentMappingDAO;
import com.sap.mango.jiraintegration.solman.entities.fieldmapping.FieldMapping;
import com.sap.mango.jiraintegration.solman.entities.fieldmapping.FieldMappingBean;
import com.sap.mango.jiraintegration.solman.entities.fieldmapping.FieldMappingDAO;
import com.sap.mango.jiraintegration.solman.entities.partnerfieldmapping.PartnerFieldMappingBean;
import com.sap.mango.jiraintegration.solman.entities.partnerfieldmapping.PartnerFieldMappingDAO;
import com.sap.mango.jiraintegration.solman.entities.partnerfieldmapping.PartnerFldMap;
import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsAO;
import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsDAO;
import com.sap.mango.jiraintegration.solman.entities.textfieldmapping.TextFieldMapping;
import com.sap.mango.jiraintegration.solman.entities.textfieldmapping.TextFieldMappingBean;
import com.sap.mango.jiraintegration.solman.entities.textfieldmapping.TextFieldMappingDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Web service, that executes configuration copy for a Solution Manager connection
 * in the jira database.</br>
 * Url: /copyFieldConfiguration</br>
 * Request Parameters <p>
 *     solManCustGuiD : the Solution Manager Customer GuiD <br/>
 *     solManCustGuiDDest : the Destination Solution Manager GuiD <br/>
 *     overwrite : Flag, that we use in case of existing configuration for the Destination Solution Manager GuiD <br/>
 * </p>
 *
 */
public class CopyFieldConfigurationServlet extends JsonServlet {

    private final SolmanParamsDAO solmanParamsDAO;

    private final FieldMappingDAO fieldMappingDAO;
    private final PartnerFieldMappingDAO partnerFieldMappingDAO;
    private final TextFieldMappingDAO textFieldMappingDAO;
    private final AppointmentMappingDAO appointmentMappingDAO;

    public CopyFieldConfigurationServlet(SolmanParamsDAO solmanParamsDAO, FieldMappingDAO fieldMappingDAO, PartnerFieldMappingDAO partnerFieldMappingDAO, TextFieldMappingDAO textFieldMappingDAO, AppointmentMappingDAO appointmentMappingDAO) {
        this.partnerFieldMappingDAO = checkNotNull(partnerFieldMappingDAO);
        this.textFieldMappingDAO = checkNotNull(textFieldMappingDAO);
        this.appointmentMappingDAO = checkNotNull(appointmentMappingDAO);
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

        final List<FieldMapping> fieldMappingsDest = fieldMappingDAO.getFieldMappings(solManCustGuiDDest);
        final List<PartnerFldMap> partnerFieldMappingsDest = partnerFieldMappingDAO.getFieldMappings(solManCustGuiDDest);
        final List<TextFieldMapping> textFieldMappingsDest = textFieldMappingDAO.getFieldMappings(solManCustGuiDDest);
        final List<AppointmentMapping> appointmentMappingsDest = appointmentMappingDAO.getFieldMappings(solManCustGuiDDest);
        if ((!fieldMappingsDest.isEmpty() ||
             !partnerFieldMappingsDest.isEmpty() ||
             !textFieldMappingsDest.isEmpty() ||
             !appointmentMappingsDest.isEmpty()
            )&& !overwriteflg ){
            sendError_WrongParams(resp, "Overwrite");
            return;
        }

        for (FieldMapping fieldMapping : fieldMappingsDest) {
            fieldMappingDAO.deleteFieldMapping(fieldMapping.getID());
        }
        for (PartnerFldMap partnerFldMap : partnerFieldMappingsDest) {
            partnerFieldMappingDAO.deleteFieldMapping(partnerFldMap.getID());
        }
        for (TextFieldMapping textFieldMapping : textFieldMappingsDest) {
            textFieldMappingDAO.deleteFieldMapping(textFieldMapping.getID());
        }
        for (AppointmentMapping appointmentMapping : appointmentMappingsDest) {
            appointmentMappingDAO.deleteFieldMapping(appointmentMapping.getID());
        }




        final List<FieldMapping> fieldMappings = fieldMappingDAO.getFieldMappings(solManCustGuiD);
        for (FieldMapping fieldMapping : fieldMappings) {
            fieldMappingDAO.createFieldMapping(solmanParamsDest.get(0),
                    new FieldMappingBean(0, fieldMapping.getSolmanProcessType(), fieldMapping.getSolmanField(), fieldMapping.getJiraField())
            );
        }
        final List<PartnerFldMap> partnerFieldMappings = partnerFieldMappingDAO.getFieldMappings(solManCustGuiD);
        for (PartnerFldMap partnerFldMap : partnerFieldMappings) {
            partnerFieldMappingDAO.createFieldMapping(solmanParamsDest.get(0),
                new PartnerFieldMappingBean(0, partnerFldMap.getSolmanProcessType(), partnerFldMap.getSolmanPartnerFunction(), partnerFldMap.getSolmanField(), partnerFldMap.getJiraField())
            );
        }
        final List<TextFieldMapping> textFieldMappings = textFieldMappingDAO.getFieldMappings(solManCustGuiD);
        for (TextFieldMapping textFieldMapping : textFieldMappings) {
            textFieldMappingDAO.createFieldMapping(solmanParamsDest.get(0),
                new TextFieldMappingBean(0, textFieldMapping.getSolmanProcessType(), textFieldMapping.getSolmanTextType(), textFieldMapping.getJiraField())
            );
        }
        final List<AppointmentMapping> appointmentMappings = appointmentMappingDAO.getFieldMappings(solManCustGuiD);
        for (AppointmentMapping appointmentMapping : appointmentMappings) {
            appointmentMappingDAO.createFieldMapping(solmanParamsDest.get(0),
                new AppointmentMappingBean(0, appointmentMapping.getSolmanProcessType(), appointmentMapping.getSolmanAppointment(), appointmentMapping.getJiraField())
            );
        }

        sendOK(resp, "nice");
    }

    @Override
    public String requiredRoles() {
        return "jira-administrators";
    }
}
