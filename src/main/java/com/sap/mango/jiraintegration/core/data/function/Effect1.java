package com.sap.mango.jiraintegration.core.data.function;

public interface Effect1<A,X extends Exception> {
    public void apply(A a) throws X;
}
