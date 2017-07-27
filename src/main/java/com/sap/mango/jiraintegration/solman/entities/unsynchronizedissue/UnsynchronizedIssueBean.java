package com.sap.mango.jiraintegration.solman.entities.unsynchronizedissue;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sap.mango.jiraintegration.core.JsonHtmlXssSerializer;

import java.util.Date;

/**
 * Bean, that stores UnsynchronizedIssue fields
 */
@JsonSerialize(using = JsonHtmlXssSerializer.class)
public class UnsynchronizedIssueBean {

    private Integer id;
    private Long issueId;
    private String issueKey;
    private int solManParamsId;
    private Date creationDate;
    private String fieldId;
    private String oldValue;
    private String newValue;
    private Date lastProcessingDate;
    private Boolean successful;
    private Date synchronizationDate;
    private String lastProcessor;
    private Integer sendType;
    private Integer processingStatus;
    private String solmanGuid;
    private String processingUser;


    public UnsynchronizedIssueBean() {
    }

    public UnsynchronizedIssueBean(Integer id, Long issueId, String issueKey, int solManParamsId, Date creationDate, String fieldId, String oldValue, String newValue, Date lastProcessingDate, Integer processingStatus, Boolean successful, Date synchronizationDate, String lastProcessor,
                                   Integer sendType, String solmanGuid, String processingUser) {
        this.id = id;
        this.issueId = issueId;
        this.issueKey = issueKey;
        this.solManParamsId = solManParamsId;
        this.creationDate = creationDate;
        this.fieldId = fieldId;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.lastProcessingDate = lastProcessingDate;
        this.successful = successful;
        this.synchronizationDate = synchronizationDate;
        this.lastProcessor = lastProcessor;
        this.sendType = sendType;
        this.solmanGuid = solmanGuid;
        this.processingStatus = processingStatus;
        this.processingUser = processingUser;
    }

    public Integer getSendType() {
        return sendType;
    }

    public void setSendType(Integer sendType) {
        this.sendType = sendType;
    }

    public Long getIssueId() {
        return issueId;
    }

    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }

    public int getSolManParamsId() {
        return solManParamsId;
    }

    public void setSolManParamsId(int solManParamsId) {
        this.solManParamsId = solManParamsId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public Date getLastProcessingDate() {
        return lastProcessingDate;
    }

    public void setLastProcessingDate(Date lastProcessingDate) {
        this.lastProcessingDate = lastProcessingDate;
    }

    public Boolean getSuccessful() {
        return successful;
    }

    public void setSuccessful(Boolean successful) {
        this.successful = successful;
    }

    public Date getSynchronizationDate() {
        return synchronizationDate;
    }

    public void setSynchronizationDate(Date synchronizationDate) {
        this.synchronizationDate = synchronizationDate;
    }

    public String getLastProcessor() {
        return lastProcessor;
    }

    public void setLastProcessor(String lastProcessor) {
        this.lastProcessor = lastProcessor;
    }

    public String getSolmanGuid() {
        return solmanGuid;
    }

    public void setSolmanGuid(String solmanGuid) {
        this.solmanGuid = solmanGuid;
    }

    public Integer getProcessingStatus() {
        return processingStatus;
    }

    public void setProcessingStatus(Integer processingStatus) {
        this.processingStatus = processingStatus;
    }

    public String getProcessingUser() {
        return processingUser;
    }

    public void setProcessingUser(String processingUser) {
        this.processingUser = processingUser;
    }

    public String getIssueKey() {
        return issueKey;
    }

    public void setIssueKey(String issueKey) {
        this.issueKey = issueKey;
    }

    @Override
    public String toString() {
        return "UnsynchronizedIssueBean{" +
                "id=" + id +
                ", issueId=" + issueId +
                ", issueKey='" + issueKey + '\'' +
                ", solManParamsId=" + solManParamsId +
                ", creationDate=" + creationDate +
                ", fieldId='" + fieldId + '\'' +
                ", oldValue='" + oldValue + '\'' +
                ", newValue='" + newValue + '\'' +
                ", lastProcessingDate=" + lastProcessingDate +
                ", successful=" + successful +
                ", synchronizationDate=" + synchronizationDate +
                ", lastProcessor='" + lastProcessor + '\'' +
                ", sendType=" + sendType +
                ", processingStatus=" + processingStatus +
                ", solmanGuid='" + solmanGuid + '\'' +
                '}';
    }
}
