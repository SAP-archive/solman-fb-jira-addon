package com.sap.mango.jiraintegration.solman.webservices.projectmapping;

/**
 * Bean, that stores the input parameters of the project mapping.
 */
public class DeleteProjectMappingRequestBean {

    private String solManCustGuiD;
    private String id;

    public String getSolManCustGuiD() {
        return solManCustGuiD;
    }

    public void setSolManCustGuiD(String solManCustGuiD) {
        this.solManCustGuiD = solManCustGuiD;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
