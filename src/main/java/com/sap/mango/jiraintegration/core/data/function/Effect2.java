package com.sap.mango.jiraintegration.core.data.function;

public interface Effect2<A,B,X extends Exception> {
    public void apply(A a, B b) throws X;
}
