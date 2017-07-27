package com.sap.mango.jiraintegration.solman.entities.unsynchronizedissue;

/**
 * Stores the different statuses of unsynchronized issue
 */
public enum ProcessingStatus {

    New(1) {
        @Override
        public String getName() {
            return "New";
        }
    },
    CommError(2) {
        @Override
        public String getName() {
            return "Comm Error";
        }
    },
    AppError(3) {
        @Override
        public String getName() {
            return "App Error";
        }
    },
    Success(4) {
        @Override
        public String getName() {
            return "Success";
        }
    },
    Manual(5) {
        @Override
        public String getName() {
            return "Manual";
        }
    };


    private Integer value;

    public abstract String getName();

    ProcessingStatus(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
