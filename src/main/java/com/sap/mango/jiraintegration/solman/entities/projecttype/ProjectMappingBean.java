package com.sap.mango.jiraintegration.solman.entities.projecttype;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sap.mango.jiraintegration.core.JsonHtmlXssSerializer;

/**
 * Created by Emo on 1/27/16.
 */

@JsonSerialize(using = JsonHtmlXssSerializer.class)
public class ProjectMappingBean {

    private Integer id;
    private String solmanProjectID;
    private String jiraProjectID;

    public ProjectMappingBean(Integer id, String solmanProjectID, String jiraProjectID) {
        this.solmanProjectID = solmanProjectID;
        this.jiraProjectID = jiraProjectID;
        this.id = id;
    }

    public String getSolmanProjectID() {
        return this.solmanProjectID;
    }
    public void setSolmanProjectID(String solmanProjectID) {
        this.solmanProjectID = solmanProjectID;
    }

    public String getJiraProjectID() {
        return jiraProjectID;
    }
    public void setJiraProjectID(String jiraProjectID) {
        this.jiraProjectID = jiraProjectID;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ProjectMappingBean{" +
                "id=" + id +
                ", solmanProjectID='" + solmanProjectID + '\'' +
                ", jiraProjectID='" + jiraProjectID + '\'' +
                '}';
    }
}
