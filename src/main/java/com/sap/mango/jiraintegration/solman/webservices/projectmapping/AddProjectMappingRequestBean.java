package com.sap.mango.jiraintegration.solman.webservices.projectmapping;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sap.mango.jiraintegration.core.JsonHtmlXssSerializer;

/**
 * Bean, that stores the input parameters of the project mapping
 */
@JsonSerialize(using = JsonHtmlXssSerializer.class)
public class AddProjectMappingRequestBean {

    private String solManCustGuiD;
    private String solmanProjectID;
    private String jiraProjectID;

    public String getSolManCustGuiD() {
        return solManCustGuiD;
    }

    public void setSolManCustGuiD(String solManCustGuiD) {
        this.solManCustGuiD = solManCustGuiD;
    }

    public String getSolmanProjectID() {
        return solmanProjectID;
    }

    public void setSolManProjectID(String solManProjectID) {
        this.solmanProjectID = solManProjectID;
    }

    public String getJiraProjectID() {
        return jiraProjectID;
    }

    public void setJiraProjectID(String jiraProjectID) {
        this.jiraProjectID = jiraProjectID;
    }
}
