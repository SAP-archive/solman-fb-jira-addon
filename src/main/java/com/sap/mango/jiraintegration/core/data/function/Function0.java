package com.sap.mango.jiraintegration.core.data.function;

public interface Function0<T> extends Function0Throws<T,RuntimeException> {
    @Override
    T apply();
}
