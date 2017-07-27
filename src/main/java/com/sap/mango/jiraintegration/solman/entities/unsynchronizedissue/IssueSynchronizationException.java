package com.sap.mango.jiraintegration.solman.entities.unsynchronizedissue;

/**
 * Exception, used when synchronization of issue is not successful
 */
public class IssueSynchronizationException extends Exception {

    private String errorMessage;
    private Integer code;

    public IssueSynchronizationException(String errorMessage, Integer code) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.code = code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Integer getCode() {
        return code;
    }
}
