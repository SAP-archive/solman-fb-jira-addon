package com.sap.mango.jiraintegration.core.data.function;

/**
 * Dimitar Georgiev
 * 30.01.15.
 */
public interface Function3Throws<A,B,C,D,X extends Exception>  {
    public D apply(A a, B b, C c) throws X;
}
