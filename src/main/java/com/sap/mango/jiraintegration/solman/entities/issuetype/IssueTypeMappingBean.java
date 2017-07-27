package com.sap.mango.jiraintegration.solman.entities.issuetype;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sap.mango.jiraintegration.core.JsonHtmlXssSerializer;

/**
 * IssueTypeMappingBean
 *
 */

@JsonSerialize(using = JsonHtmlXssSerializer.class)
public class IssueTypeMappingBean {

    private Integer id;
    private String solmanProcessType;
    private Integer jiraIssueType;
    private String jiraIssueTypeName;

    public IssueTypeMappingBean(Integer id, String solmanProcessType, Integer jiraIssueType, String jiraIssueTypeName) {
        this.solmanProcessType = solmanProcessType;
        this.jiraIssueType = jiraIssueType;
        this.id = id;
        this.jiraIssueTypeName = jiraIssueTypeName;
    }

    public String getSolmanProcessType() {
        return this.solmanProcessType;
    }
    public void setSolmanProcessType(String solmanProcessType) {
        this.solmanProcessType = solmanProcessType;
    }

    public Integer getJiraIssueType() {
        return jiraIssueType;
    }
    public void setJiraIssueType(Integer jiraIssueType) {
        this.jiraIssueType = jiraIssueType;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getJiraIssueTypeName() {
        return jiraIssueTypeName;
    }

    public void setJiraIssueTypeName(String jiraIssueTypeName) {
        this.jiraIssueTypeName = jiraIssueTypeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IssueTypeMappingBean that = (IssueTypeMappingBean) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (solmanProcessType != null ? !solmanProcessType.equals(that.solmanProcessType) : that.solmanProcessType != null)
            return false;
        return !(jiraIssueType != null ? !jiraIssueType.equals(that.jiraIssueType) : that.jiraIssueType != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (solmanProcessType != null ? solmanProcessType.hashCode() : 0);
        result = 31 * result + (jiraIssueType != null ? jiraIssueType.hashCode() : 0);
        result = 31 * result + (jiraIssueTypeName != null ? jiraIssueTypeName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "IssueTypeMappingBean{" +
                "id=" + id +
                ", solmanProcessType='" + solmanProcessType + '\'' +
                ", jiraIssueType=" + jiraIssueType +
                ", jiraIssueTypeName='" + jiraIssueTypeName + '\'' +
                '}';
    }
}
