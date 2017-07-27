package com.sap.mango.jiraintegration.solman.entities.issuestatus;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sap.mango.jiraintegration.core.JsonHtmlXssSerializer;

/**
 * IssueStatusMappingBean
 */

@JsonSerialize(using = JsonHtmlXssSerializer.class)
public class IssueStatusMappingBean {

    private Integer id;
    private String solmanProcessType;
    private String solmanStatus;
    private String jiraTransition;

    public IssueStatusMappingBean(Integer id, String solmanProcessType, String solmanStatus, String jiraTransition) {
        this.id = id;
        this.solmanProcessType = solmanProcessType;
        this.solmanStatus = solmanStatus;
        this.jiraTransition = jiraTransition;
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

    public String getSolmanStatus() {
        return solmanStatus;
    }

    public void setSolmanStatus(String solmanStatus) {
        this.solmanStatus = solmanStatus;
    }

    public String getJiraTransition() {
        return jiraTransition;
    }

    public void setJiraTransition(String jiraTransition) {
        this.jiraTransition = jiraTransition;
    }
}
