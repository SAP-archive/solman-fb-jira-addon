package com.sap.mango.jiraintegration.core.data;


import java.util.ArrayList;
import java.util.List;

public abstract class Either<A, B> {
    private Either() { } // makes this a safe ADT
    public abstract boolean isRight();
    
    public boolean isLeft() {
    	return !isRight();
    }
    
    public Left<A,B> toLeft() {
    	return (Left<A,B>)this;
    }
    
    public Right<A,B> toRight() {
    	return (Right<A,B>)this;
    }


    public final static class Left<L, R> extends Either<L, R>  {
        public final L left_value;
        public Left(L l) { left_value = l; }
        public boolean isRight() { return false; }
    }
    public final static class Right<L, R> extends Either<L, R>  {
        public final R right_value;
        public Right(R r) { right_value = r; }
        public boolean isRight() { return true; }
    }

    @Override
    public String toString() {
        if(isLeft()) {
            return toLeft().left_value.toString();
        }
        return toRight().right_value.toString();
    }

    public static <A> Either<List<A>, Void> accumulateLeft(Either<A,?>... xs) {
        List<A> err = new ArrayList<>();
        for(Either<A,?> x: xs) {
            if(x.isLeft()) {
                err.add(x.toLeft().left_value);
            }
        }

        if(err.isEmpty()) {
            return new Right<>(null);
        }

        return new Left<>(err);
    }

    public static <A> Either<List<A>, Void> accumulateLeft(Either<List<A>,Void> acc, Either<A,?> e) {
        if(acc.isRight()&& e.isRight()) {
            return new Right<>(null);
        } else {
            List<A> err = new ArrayList<>();

            if(acc.isLeft()) {
                err.addAll(acc.toLeft().left_value);
            }
            if(e.isLeft()) {
                err.add(e.toLeft().left_value);
            }

            return new Either.Left<>(err);
        }
    }



}