package com.sap.mango.jiraintegration.solman.entities.unsynchronizedissue;

import net.java.ao.Entity;
import net.java.ao.Preload;

import java.util.Date;

/**
 *
 */
@Preload("CREATION_DATE, FIELD_ID, OLD_VALUE, NEW_VALUE, PROCESSING_STATUS, SEND_TYPE, LAST_PROCESSING_DATE, SUCCESSFUL, SYNCHRONIZATION_DATE, LAST_PROCESSOR, PROCESSING_USER")
public interface IssueTransition extends Entity {

    void setCreationDate(Date date);
    Date getCreationDate();

    void setFieldId(String fieldId);
    String getFieldId();

    void setOldValue(String oldValue);
    String getOldValue();

    void setNewValue(String newValue);
    String getNewValue();

    void setProcessingStatus(Integer processingStatus);
    Integer getProcessingStatus();

    void setSendType(Integer sendType);
    Integer getSendType();

    void setLastProcessingDate(Date date);
    Date getLastProcessingDate();

    void setSuccessful(Boolean successful);
    Boolean isSuccessful();

    void setSynchronizationDate(Date date);
    Date getSynchronizationDate();

    String getLastProcessor();
    void setLastProcessor(String lastProcessor);

    String getProcessingUser();
    void setProcessingUser(String processingUser);

    UnsynchronizedIssue getUnsynchronizedIssue();
    void setUnsynchronizedIssue(UnsynchronizedIssue unsynchronizedIssue);

}
