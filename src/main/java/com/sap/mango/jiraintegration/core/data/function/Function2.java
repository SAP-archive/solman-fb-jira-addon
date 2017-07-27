package com.sap.mango.jiraintegration.core.data.function;

public interface Function2<A,B,C> extends Function2Throws <A,B,C,RuntimeException> {

    @Override
    C apply(A a, B b);
}
