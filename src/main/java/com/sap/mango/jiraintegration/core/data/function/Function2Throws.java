package com.sap.mango.jiraintegration.core.data.function;

public interface Function2Throws<A,B,C,X extends Exception> {
    C apply(A a, B b) throws X;
}
