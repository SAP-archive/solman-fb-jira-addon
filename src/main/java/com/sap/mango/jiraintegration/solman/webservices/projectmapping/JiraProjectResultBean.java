package com.sap.mango.jiraintegration.solman.webservices.projectmapping;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sap.mango.jiraintegration.core.JsonHtmlXssSerializer;

/**
 * Bean, that stores jira project key + jira project name
 */

@JsonSerialize(using = JsonHtmlXssSerializer.class)
public class JiraProjectResultBean {

    private String jiraProjectKey;
    private String jiraProjectName;

    public JiraProjectResultBean(String jiraProjectKey, String jiraProjectName) {
        this.jiraProjectKey = jiraProjectKey;
        this.jiraProjectName = jiraProjectName;
    }

    public String getJiraProjectKey() {
        return jiraProjectKey;
    }

    public void setJiraProjectKey(String jiraProjectKey) {
        this.jiraProjectKey = jiraProjectKey;
    }

    public String getJiraProjectName() {
        return jiraProjectName;
    }

    public void setJiraProjectName(String jiraProjectName) {
        this.jiraProjectName = jiraProjectName;
    }

    @Override
    public String toString() {
        return "JiraProjectResultBean{" +
                "jiraProjectKey='" + jiraProjectKey + '\'' +
                ", jiraProjectName='" + jiraProjectName + '\'' +
                '}';
    }
}
