package com.sap.mango.jiraintegration.core.data.function;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Tuple2<A,B>  {

    public final A a;
    public final B b;

    public Tuple2(@JsonProperty("a")A a, @JsonProperty("b")B b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tuple2 tuple2 = (Tuple2) o;

        if (a != null ? !a.equals(tuple2.a) : tuple2.a != null) return false;
        if (b != null ? !b.equals(tuple2.b) : tuple2.b != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = a != null ? a.hashCode() : 0;
        result = 31 * result + (b != null ? b.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Tuple2{" +
                "a=" + a +
                ", b=" + b +
                '}';
    }

    public<C,D> Tuple2<C,D> map(Function1<A,C> f1, Function1<B,D> f2) {
        return new Tuple2<>(f1.apply(a), f2.apply(b));
    }
}
