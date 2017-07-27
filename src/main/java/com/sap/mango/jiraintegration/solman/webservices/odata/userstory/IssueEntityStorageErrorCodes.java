package com.sap.mango.jiraintegration.solman.webservices.odata.userstory;

/**
 * Class, contains all possible error codes
 */
public interface IssueEntityStorageErrorCodes {
    String NOT_EXISTING_PROJECT_MAPPING = "1000";
    String NOT_EXISTING_ISSUE_MAPPING = "1001";
    String NOT_EXISTING_ISSUE_TYPE = "1002";
    String FORBIDDEN_LINK_OPERATION = "1003";
    String INTERNAL_ISSUE_CREATION_ERROR = "1004";
    String NOT_EXISTING_FIELD_MAPPING = "1005";
    String NOT_EXISTING_APPOINTMENT_MAPPING = "1008";
    String NOT_EXISTING_TEXT_MAPPING = "1009";
    String NOT_EXISTING_PRIORITY_MAPPING = "1010";
    String NOT_VALID_EFFORT_UNIT = "1006";
    String NOT_VALID_DATE_FORMAT = "10007";
}
