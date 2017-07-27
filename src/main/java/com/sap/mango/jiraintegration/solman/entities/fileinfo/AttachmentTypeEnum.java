package com.sap.mango.jiraintegration.solman.entities.fileinfo;

/**
 * Enum, that stores all attachment types, that comes from
 */
public enum AttachmentTypeEnum {

    ATTACHMENT(1),
    DOCUMENT(2);

    private Integer value;

    AttachmentTypeEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return this.value;
    }
}
