package com.sap.mango.jiraintegration.solman.webservices.unsychronizedissue;

/**
 * Bean, that stores the result of issue synchronization : http status + detail of the message
 */
public class IssueSynchronizationStatusResultBean {

    private int code;
    private String detail;

    public IssueSynchronizationStatusResultBean(int code, String detail) {
        this.code = code;
        this.detail = detail;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "IssueSynchronizationStatusResultBean{" +
                "code=" + code +
                ", detail='" + detail + '\'' +
                '}';
    }
}
