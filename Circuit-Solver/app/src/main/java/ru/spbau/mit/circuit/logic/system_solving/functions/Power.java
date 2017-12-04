package ru.spbau.mit.circuit.logic.system_solving.functions;


import android.support.annotation.NonNull;

public class Power implements Comparable<Power> {

    private final int power;

    public Power(int power) {
        this.power = power;
    }

    public int power() {
        return power;
    }

    @Override
    public int compareTo(@NonNull Power o) {
        return power - o.power;
    }

    @Override
    public String toString() {
        return "x^" + power;
    }
}
