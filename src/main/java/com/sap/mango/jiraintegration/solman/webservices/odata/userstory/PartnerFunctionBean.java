package com.sap.mango.jiraintegration.solman.webservices.odata.userstory;

/**
 * Bean, that store partner function information, used to group the partner functions
 */
public class PartnerFunctionBean {

    private String partnerFunction;
    private String partnerName;
    private String partnerEmail;
    private Boolean main;

    public PartnerFunctionBean(String partnerFunction, String partnerName, String partnerEmail, Boolean main) {
        this.partnerFunction = partnerFunction;
        this.partnerName = partnerName;
        this.partnerEmail = partnerEmail;
        this.main = main;
    }

    public String getPartnerFunction() {
        return partnerFunction;
    }

    public void setPartnerFunction(String partnerFunction) {
        this.partnerFunction = partnerFunction;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getPartnerEmail() {
        return partnerEmail;
    }

    public void setPartnerEmail(String partnerEmail) {
        this.partnerEmail = partnerEmail;
    }

    public Boolean getMain() {
        return main;
    }

    public void setMain(Boolean main) {
        this.main = main;
    }
}
