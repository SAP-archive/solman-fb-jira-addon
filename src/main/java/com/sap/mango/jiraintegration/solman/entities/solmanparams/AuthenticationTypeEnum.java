package com.sap.mango.jiraintegration.solman.entities.solmanparams;

/**
 * Enum, that stores all possible authentication types for Solman
 */
public enum AuthenticationTypeEnum {

    Basic(1),
    Bearer(2);

    public final Integer value;

    AuthenticationTypeEnum(Integer value) {
        this.value = value;
    }

    public static String getValueByKey(Integer key) {
        switch(key) {
            case 1 : return "Basic";
            case 2 : return "Bearer";
            default : return "-";
        }
    }
}
