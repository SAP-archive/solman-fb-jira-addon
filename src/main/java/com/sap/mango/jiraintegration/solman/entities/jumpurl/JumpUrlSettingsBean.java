package com.sap.mango.jiraintegration.solman.entities.jumpurl;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sap.mango.jiraintegration.core.JsonHtmlXssSerializer;

/**
 * Jump Url Settings Bean
 */

@JsonSerialize(using = JsonHtmlXssSerializer.class)
public class JumpUrlSettingsBean {

    private Integer id;
    private String solManCustGuiD;
    private String workPackageAppJumpUrl;
    private String workItemAppJumpUrl;

    public JumpUrlSettingsBean() {
    }

    public JumpUrlSettingsBean(Integer id, String solManCustGuiD, String workPackageAppJumpUrl, String workItemAppJumpUrl) {
        this.id = id;
        this.solManCustGuiD = solManCustGuiD;
        this.workPackageAppJumpUrl = workPackageAppJumpUrl;
        this.workItemAppJumpUrl = workItemAppJumpUrl;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWorkPackageAppJumpUrl() {
        return workPackageAppJumpUrl;
    }

    public void setWorkPackageAppJumpUrl(String workPackageAppJumpUrl) {
        this.workPackageAppJumpUrl = workPackageAppJumpUrl;
    }

    public String getWorkItemAppJumpUrl() {
        return workItemAppJumpUrl;
    }

    public void setWorkItemAppJumpUrl(String workItemAppJumpUrl) {
        this.workItemAppJumpUrl = workItemAppJumpUrl;
    }

    public String getSolManCustGuiD() {
        return this.solManCustGuiD;
    }

    public void setSolManCustGuiD(String solManCustGuiD) {
        this.solManCustGuiD = solManCustGuiD;
    }
}
