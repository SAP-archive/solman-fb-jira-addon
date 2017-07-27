package com.sap.mango.jiraintegration.solman.entities.appointmentmapping;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sap.mango.jiraintegration.core.JsonHtmlXssSerializer;

/**
 * AppointmentMappingBean
 *
 */

@JsonSerialize(using = JsonHtmlXssSerializer.class)
public class AppointmentMappingBean {

    private Integer id;
    private String solmanProcessType;
    private String solmanAppointment;
    private String jiraField;

    @SuppressWarnings("unused")
    public AppointmentMappingBean(){}
    public AppointmentMappingBean(Integer id, String solmanProcessType, String solmanAppointment, String jiraField) {
        this.id = id;
        this.solmanProcessType = solmanProcessType;
        this.solmanAppointment = solmanAppointment;
        this.jiraField = jiraField;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSolmanProcessType() {
        return solmanProcessType;
    }

    public void setSolmanProcessType(String solmanProcessType) {
        this.solmanProcessType = solmanProcessType;
    }

    public String getSolmanAppointment() {
        return solmanAppointment;
    }

    public void setSolmanAppointment(String solmanAppointment) {
        this.solmanAppointment = solmanAppointment;
    }

    public String getJiraField() {
        return jiraField;
    }

    public void setJiraField(String jiraField) {
        this.jiraField = jiraField;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AppointmentMappingBean that = (AppointmentMappingBean) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (solmanProcessType != null ? !solmanProcessType.equals(that.solmanProcessType) : that.solmanProcessType != null)
            return false;
        if (solmanAppointment != null ? !solmanAppointment.equals(that.solmanAppointment) : that.solmanAppointment != null)
            return false;
        return !(jiraField != null ? !jiraField.equals(that.jiraField) : that.jiraField != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (solmanProcessType != null ? solmanProcessType.hashCode() : 0);
        result = 31 * result + (solmanAppointment != null ? solmanAppointment.hashCode() : 0);
        result = 31 * result + (jiraField != null ? jiraField.hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        return "AppointmentMappingBean{" +
                "id=" + id +
                ", solmanProcessType='" + solmanProcessType + '\'' +
                ", solmanAppointment='" + solmanAppointment + '\'' +
                ", jiraField='" + jiraField + '\'' +
                '}';
    }
}
