package ru.spbau.mit.circuit.logic.math.expressions;


import android.support.annotation.NonNull;

import ru.spbau.mit.circuit.logic.math.algebra.Numerical;

public class PolyExpressions {

    private static final PolyExpression zero = new PolyExpression();
    private static final PolyExpression identity = zero.single();

    @NonNull
    public static PolyExpression zero() {
        return zero;
    }

    @NonNull
    public static PolyExpression identity() {
        return identity;
    }

    public static PolyExpression constant(double n) {
        return zero.singleton(Numerical.number(n), Monom.identity());
    }

    public static PolyExpression variable(String name) {
        return zero.singleton(Numerical.identity(), Monom.monom(name));
    }

    public static PolyExpression variable(double n, String name) {
        return zero.singleton(Numerical.number(n), Monom.monom(name));
    }

    public static void main(String[] args) {
        PolyExpression p1 = variable(3, "A");
        PolyExpression p2 = variable(1, "B");
        System.out.println(p1.add(p2).multiply(p1.add(p2)));
        System.out.println(p1.subtract(p2).multiply(p1.add(p2)));
    }
}
