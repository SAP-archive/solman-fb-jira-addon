package com.sap.mango.jiraintegration.core.data.function;

/**
 * Dimitar Georgiev
 * 30.01.15.
 */
public interface Function3<A,B,C,D> extends Function3Throws<A,B,C,D,RuntimeException> {
    @Override
    D apply(A a, B b, C c);
}
