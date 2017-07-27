package com.sap.mango.jiraintegration.validators;

/**
 * Helper class, that inserts all generic errors
 */
public class InvalidInputException extends com.opensymphony.workflow.InvalidInputException {

    private String [] genericErrors;

    public InvalidInputException(String error) {
        super(error);
    }

    public InvalidInputException(String [] genericErrors) {
        this.genericErrors = genericErrors;
        setGenericErrors(this.genericErrors);
    }

    public void setGenericErrors(String [] genericErrors) {
        for (int i = 0; i < genericErrors.length; i++) {
            this.addError(genericErrors[i]);
        }
    }

}
