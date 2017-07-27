package com.sap.mango.jiraintegration.solman.entities.fieldmapping;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sap.mango.jiraintegration.core.JsonHtmlXssSerializer;

/**
 * FieldMappingBean
 *
 */

@JsonSerialize(using = JsonHtmlXssSerializer.class)
public class FieldMappingBean {

    private Integer id;
    private String solmanProcessType;
    private String solmanField;
    private String jiraField;

    @SuppressWarnings("unused")
    public FieldMappingBean(){}
    public FieldMappingBean(Integer id, String solmanProcessType, String solmanField, String jiraField) {
        this.id = id;
        this.solmanProcessType = solmanProcessType;
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

        FieldMappingBean that = (FieldMappingBean) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (solmanProcessType != null ? !solmanProcessType.equals(that.solmanProcessType) : that.solmanProcessType != null)
            return false;
        if (solmanField != null ? !solmanField.equals(that.solmanField) : that.solmanField != null) return false;
        return !(jiraField != null ? !jiraField.equals(that.jiraField) : that.jiraField != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (solmanProcessType != null ? solmanProcessType.hashCode() : 0);
        result = 31 * result + (solmanField != null ? solmanField.hashCode() : 0);
        result = 31 * result + (jiraField != null ? jiraField.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FieldMappingBean{" +
                "id=" + id +
                ", solmanProcessType='" + solmanProcessType + '\'' +
                ", solmanField='" + solmanField + '\'' +
                ", jiraField='" + jiraField + '\'' +
                '}';
    }
}
