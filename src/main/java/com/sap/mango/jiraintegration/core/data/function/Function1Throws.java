package com.sap.mango.jiraintegration.core.data.function;


public interface Function1Throws<A,B,X extends Exception> {
    B apply(A a) throws X;
}
