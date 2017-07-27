package com.sap.mango.jiraintegration.utils;

import org.apache.olingo.commons.api.http.HttpStatusCode;

/**
 * Stores Http Utils methods
 */
public class HttpUtils {
    public static boolean isCommunicationError(int statusCode) {
       return (statusCode >= HttpStatusCode.BAD_REQUEST.getStatusCode()
                && statusCode <= HttpStatusCode.PRECONDITION_REQUIRED.getStatusCode()) ||
               statusCode == -1;
    }

    public static boolean isApplicationError(int statusCode) {
        return (statusCode >= HttpStatusCode.BAD_REQUEST.getStatusCode()
                && statusCode <= HttpStatusCode.PRECONDITION_REQUIRED.getStatusCode()) ||
                statusCode == -1;
    }
}
