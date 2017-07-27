package com.sap.mango.jiraintegration.solman.entities.appointmentmapping;

import com.sap.mango.jiraintegration.solman.entities.solmanparams.SolmanParamsAO;
import net.java.ao.Entity;
import net.java.ao.Preload;

/**
 * AppointmentMapping
 *
 */
@Preload("SOLMAN_PROCESS_TYPE, SOLMAN_APPOINTMENT, JIRA_FIELD")
public interface AppointmentMapping extends Entity {

    String getSolmanProcessType();
    void setSolmanProcessType(String solmanProcessType);

    String getSolmanAppointment();
    void setSolmanAppointment(String textType);

    String getJiraField();
    void setJiraField(String jiraField);

    SolmanParamsAO getSolmanParams();
    void setSolmanParams(SolmanParamsAO solmanParams);
}
