package com.sap.mango.jiraintegration.core.data.function;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Tuple3<A,B,C> extends  Tuple2<A,B> {
    public final C c;

    public Tuple3(@JsonProperty("a")A a, @JsonProperty("b")B b, @JsonProperty("c")C c) {
        super(a, b);
        this.c = c;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Tuple3 tuple3 = (Tuple3) o;

        if (c != null ? !c.equals(tuple3.c) : tuple3.c != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (c != null ? c.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Tuple3{" +
                "c=" + c +
                "} " + super.toString();
    }
}
