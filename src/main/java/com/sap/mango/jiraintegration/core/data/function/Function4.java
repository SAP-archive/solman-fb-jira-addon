package com.sap.mango.jiraintegration.core.data.function;

/**
 * Created by fmap on 02.07.15.
 */
public interface Function4<A,B,C,D,E> {
    E apply(A a, B b, C c, D d);
}
