package com.sap.mango.jiraintegration.solman.webservices.odata.userstory;


/**
 * Enum, that contains all values for WricefAttribute
 */
public enum WricefElementEnum {

    Workflow("Workflow"),
    Report("Report"),
    Interface("Interface"),
    Conversion("Conversion"),
    Enhancement("Enhancement"),
    Form("Form");

    public final String value;

    WricefElementEnum(String value) {
        this.value = value;
    }

    public static String getValueByKey(String key) {
        switch(key) {
            case "W" : return WricefElementEnum.Workflow.value;
            case "R" : return WricefElementEnum.Report.value;
            case "I" : return WricefElementEnum.Interface.value;
            case "C" : return WricefElementEnum.Conversion.value;
            case "E" : return WricefElementEnum.Enhancement.value;
            case "F" : return WricefElementEnum.Form.value;
            default : return null;
        }
    }

}
