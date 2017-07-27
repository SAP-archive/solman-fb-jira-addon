package com.sap.mango.jiraintegration.solman.entities.partnerfieldmapping;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sap.mango.jiraintegration.core.JsonHtmlXssSerializer;

/**
 * PartnerFieldMappingBean
 *
 */

@JsonSerialize(using = JsonHtmlXssSerializer.class)
public class PartnerFieldMappingBean {

    private Integer id;
    private String solmanProcessType;
    private String solmanPartnerFunction;
    private String solmanField;
    private String jiraField;

    @SuppressWarnings("unused")
    public PartnerFieldMappingBean(){}

    public PartnerFieldMappingBean(Integer id, String solmanProcessType, String solmanPartnerFunction, String solmanField, String jiraField) {
        this.id = id;
        this.solmanProcessType = solmanProcessType;
        this.solmanPartnerFunction = solmanPartnerFunction;
        this.solmanField = solmanField;
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

    public String getSolmanPartnerFunction() {
        return solmanPartnerFunction;
    }

    public void setSolmanPartnerFunction(String solmanPartnerFunction) {
        this.solmanPartnerFunction = solmanPartnerFunction;
    }

    public String getSolmanField() {
        return solmanField;
    }

    public void setSolmanField(String solmanField) {
        this.solmanField = solmanField;
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

        PartnerFieldMappingBean that = (PartnerFieldMappingBean) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (solmanProcessType != null ? !solmanProcessType.equals(that.solmanProcessType) : that.solmanProcessType != null)
            return false;
        if (solmanPartnerFunction != null ? !solmanPartnerFunction.equals(that.solmanPartnerFunction) : that.solmanPartnerFunction != null)
            return false;
        if (solmanField != null ? !solmanField.equals(that.solmanField) : that.solmanField != null) return false;
        return !(jiraField != null ? !jiraField.equals(that.jiraField) : that.jiraField != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (solmanProcessType != null ? solmanProcessType.hashCode() : 0);
        result = 31 * result + (solmanPartnerFunction != null ? solmanPartnerFunction.hashCode() : 0);
        result = 31 * result + (solmanField != null ? solmanField.hashCode() : 0);
        result = 31 * result + (jiraField != null ? jiraField.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PartnerFieldMappingBean{" +
                "id=" + id +
                ", solmanProcessType='" + solmanProcessType + '\'' +
                ", solmanPartnerFunction='" + solmanPartnerFunction + '\'' +
                ", solmanField='" + solmanField + '\'' +
                ", jiraField='" + jiraField + '\'' +
                '}';
    }
}
