package ru.spbau.mit.circuit.logic.system_solving.functions;


import android.support.annotation.NonNull;

public class Exponent implements FunctionExpression, Comparable<Exponent> {

    private final double power;

    Exponent(double power) {
        this.power = power;
    }

    double power() {
        return power;
    }

    @Override
    public int compareTo(@NonNull Exponent o) {
        return Double.compare(power, o.power);
    }

    @Override
    public String toString() {
        return "e^" + power;
    }
}
