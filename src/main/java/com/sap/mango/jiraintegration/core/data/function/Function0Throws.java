package com.sap.mango.jiraintegration.core.data.function;

public interface Function0Throws<T,X extends Exception> {
    T apply() throws X;
}
