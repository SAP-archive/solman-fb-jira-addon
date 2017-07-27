package com.sap.mango.jiraintegration.solman.webservices.issuestatus;

/**
 * Bean, that stores request parameters for AddIssueStatusMappingServlet
 */
public class AddIssueStatusMappingRequestBean {

    private String solmanProcessType;
    private String solmanStatus;
    private String jiraTransition;
    private String solManCustGuiD;

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

    public String getSolManCustGuiD() {
        return solManCustGuiD;
    }

    public void setSolManCustGuiD(String solManCustGuiD) {
        this.solManCustGuiD = solManCustGuiD;
    }
}
