package com.sap.mango.jiraintegration.solman.entities.unsynchronizedissue;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sap.mango.jiraintegration.core.JsonHtmlXssSerializer;

import java.util.Date;

/**
 * Stores issue transition information
 */
@JsonSerialize(using = JsonHtmlXssSerializer.class)
public class IssueTransitionBean {

    private Integer id;
    private Long unsynchronizedIssueId;
    private String issueKey;
    private Long issueId;
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

    public IssueTransitionBean() {
    }

    public IssueTransitionBean(Integer id, Long unsynchronizedIssueId, String issueKey, Long issueId, Date creationDate,
                               String fieldId, String oldValue, String newValue, Date lastProcessingDate, Boolean successful, Date synchronizationDate, String lastProcessor,
                               Integer sendType, Integer processingStatus, String solmanGuid, String processingUser) {
        this.id = id;
        this.unsynchronizedIssueId = unsynchronizedIssueId;
        this.issueKey = issueKey;
        this.issueId = issueId;
        this.creationDate = creationDate;
        this.fieldId = fieldId;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.lastProcessingDate = lastProcessingDate;
        this.successful = successful;
        this.synchronizationDate = synchronizationDate;
        this.lastProcessor = lastProcessor;
        this.sendType = sendType;
        this.processingStatus = processingStatus;
        this.solmanGuid = solmanGuid;
        this.processingUser = processingUser;
    }

    public Long getUnsynchronizedIssueId() {
        return unsynchronizedIssueId;
    }

    public void setUnsynchronizedIssueId(Long unsynchronizedIssueId) {
        this.unsynchronizedIssueId = unsynchronizedIssueId;
    }

    public String getIssueKey() {
        return issueKey;
    }

    public void setIssueKey(String issueKey) {
        this.issueKey = issueKey;
    }

    public Long getIssueId() {
        return issueId;
    }

    public void setIssueId(Long issueId) {
        this.issueId = issueId;
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

    public Integer getSendType() {
        return sendType;
    }

    public void setSendType(Integer sendType) {
        this.sendType = sendType;
    }

    public Integer getProcessingStatus() {
        return processingStatus;
    }

    public void setProcessingStatus(Integer processingStatus) {
        this.processingStatus = processingStatus;
    }

    public String getSolmanGuid() {
        return solmanGuid;
    }

    public void setSolmanGuid(String solmanGuid) {
        this.solmanGuid = solmanGuid;
    }

    public String getProcessingUser() {
        return processingUser;
    }

    public void setProcessingUser(String processingUser) {
        this.processingUser = processingUser;
    }

    @Override
    public String toString() {
        return "IssueTransitionBean{" +
                "id=" + id +
                ", unsynchronizedIssueId=" + unsynchronizedIssueId +
                ", issueKey='" + issueKey + '\'' +
                ", issueId=" + issueId +
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
                ", processingUser='" + processingUser + '\'' +
                '}';
    }
}
