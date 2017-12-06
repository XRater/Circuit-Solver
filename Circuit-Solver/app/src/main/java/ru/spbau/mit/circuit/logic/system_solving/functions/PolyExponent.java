package ru.spbau.mit.circuit.logic.system_solving.functions;


import android.support.annotation.NonNull;

public class PolyExponent implements Comparable<PolyExponent> {

    private final Exponent exponent;
    private final Power power;

    public PolyExponent(Power power, Exponent exponent) {
        this.exponent = exponent;
        this.power = power;
    }

    @Override
    public int compareTo(@NonNull PolyExponent o) {
        if (exponent.compareTo(o.exponent) > 0) {
            return 1;
        } else if (exponent.compareTo(o.exponent) == 0) {
            return power.compareTo(o.power);
        }
        return -1;
    }
}
