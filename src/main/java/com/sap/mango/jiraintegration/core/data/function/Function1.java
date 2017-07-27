package com.sap.mango.jiraintegration.core.data.function;

public interface Function1<A,B> extends  Function1Throws<A,B,RuntimeException> {
    @Override
    B apply(A a);
}
