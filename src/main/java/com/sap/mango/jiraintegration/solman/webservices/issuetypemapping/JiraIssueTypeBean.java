package com.sap.mango.jiraintegration.solman.webservices.issuetypemapping;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sap.mango.jiraintegration.core.JsonHtmlXssSerializer;

/**
 * Stores information about issue type
 */
@JsonSerialize(using = JsonHtmlXssSerializer.class)
public class JiraIssueTypeBean {

    private String issueTypeId;
    private String issueType;

    public JiraIssueTypeBean(String issueTypeId, String issueType) {
        this.issueTypeId = issueTypeId;
        this.issueType = issueType;
    }

    public String getIssueTypeId() {
        return issueTypeId;
    }

    public void setIssueTypeId(String issueTypeId) {
        this.issueTypeId = issueTypeId;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    @Override
    public String toString() {
        return "JiraIssueTypeBean{" +
                "issueTypeId=" + issueTypeId +
                ", issueType='" + issueType + '\'' +
                '}';
    }
}
