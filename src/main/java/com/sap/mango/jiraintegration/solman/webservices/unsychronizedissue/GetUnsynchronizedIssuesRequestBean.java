package com.sap.mango.jiraintegration.solman.webservices.unsychronizedissue;

import java.util.Date;

/**
 * Bean, that stores all input parameters when retrieving unsynchronized issues + filter criteria
 */
public class GetUnsynchronizedIssuesRequestBean {
    private Integer page;
    private Integer recordsPerPage;
    private String solManCustGuiD;
    //filtering criterias
    private String issueKey;
    private Integer sendTypeId;
    private Date unsynchronizedIssueCreationDate;
    //sort criterias
    private String orderBy;
    private String direction;

    public GetUnsynchronizedIssuesRequestBean() {
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRecordsPerPage() {
        return recordsPerPage;
    }

    public void setRecordsPerPage(Integer recordsPerPage) {
        this.recordsPerPage = recordsPerPage;
    }

    public String getSolManCustGuiD() {
        return solManCustGuiD;
    }

    public void setSolManCustGuiD(String solManCustGuiD) {
        this.solManCustGuiD = solManCustGuiD;
    }

    public String getIssueKey() {
        return issueKey;
    }

    public void setIssueKey(String issueKey) {
        this.issueKey = issueKey;
    }

    public Integer getSendTypeId() {
        return sendTypeId;
    }

    public void setSendTypeId(Integer sendTypeId) {
        this.sendTypeId = sendTypeId;
    }

    public Date getUnsynchronizedIssueCreationDate() {
        return unsynchronizedIssueCreationDate;
    }

    public void setUnsynchronizedIssueCreationDate(Date unsynchronizedIssueCreationDate) {
        this.unsynchronizedIssueCreationDate = unsynchronizedIssueCreationDate;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
