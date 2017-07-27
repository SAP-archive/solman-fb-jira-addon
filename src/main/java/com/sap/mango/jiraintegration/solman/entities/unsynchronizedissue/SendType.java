package com.sap.mango.jiraintegration.solman.entities.unsynchronizedissue;

/**
 * Storing all types of synchronization between Jira and Solution Manager
 */
public enum SendType {

    FIELD_UPDATE(1) {
        @Override
        public String getName() {
            return "Field Update";
        }
    },
    STATUS_UPDATE(2) {
        @Override
        public String getName() {
            return "Status Update";
        }
    },
    ISSUE_CREATION(3) {
        @Override
        public String getName() {
            return "Issue Creation";
        }
    };

    private Integer value;

    SendType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public abstract String getName();
}
