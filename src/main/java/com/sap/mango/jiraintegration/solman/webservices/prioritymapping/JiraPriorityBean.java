package com.sap.mango.jiraintegration.solman.webservices.prioritymapping;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sap.mango.jiraintegration.core.JsonHtmlXssSerializer;

/**
 * Bean, that represents the properties of the jira priority
 */
@JsonSerialize(using = JsonHtmlXssSerializer.class)
public class JiraPriorityBean {
    private String id;
    private String priority;

    public JiraPriorityBean(String id, String priority) {
        this.id = id;
        this.priority = priority;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "JiraPriorityBean{" +
                "id='" + id + '\'' +
                ", priority='" + priority + '\'' +
                '}';
    }
}
