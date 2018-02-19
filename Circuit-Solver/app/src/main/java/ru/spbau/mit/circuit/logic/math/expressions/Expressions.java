package ru.spbau.mit.circuit.logic.math.expressions;


import android.support.annotation.NonNull;

public class Expressions {

    private static final Expression zero = new Expression();
    private static final Expression identity = zero.single();

    @NonNull
    public static Expression zero() {
        return zero;
    }

    @NonNull
    public static Expression identity() {
        return identity;
    }

    public static Expression constant(double n) {
        return new Expression(PolyExpressions.constant(n));
    }

    public static Expression variable(String name) {
        return new Expression(PolyExpressions.variable(name));
    }

    public static Expression variable(double n, String name) {
        return new Expression(PolyExpressions.variable(n, name));
    }
}
