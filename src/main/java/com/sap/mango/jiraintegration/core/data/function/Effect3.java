package com.sap.mango.jiraintegration.core.data.function;

public interface Effect3<A,B,C,X extends Exception> {
    public void apply(A a, B b, C c) throws X;
}
