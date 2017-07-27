package com.sap.mango.jiraintegration.solman.webservices.issuestatus;

/**
 * Bean, that stores request parameters for DeleteStatusMappingServlet
 */
public class DeleteIssueMappingRequestBean {

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
