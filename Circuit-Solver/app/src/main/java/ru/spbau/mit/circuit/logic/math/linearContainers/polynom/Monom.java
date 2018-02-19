package ru.spbau.mit.circuit.logic.math.linearContainers.polynom;


import android.support.annotation.NonNull;

import ru.spbau.mit.circuit.logic.math.algebra.interfaces.OrderedGroup;

class Monom implements OrderedGroup<Monom> {

    private static final Monom zero = new Monom();
    private final int power;

    private Monom() {
        power = 0;
    }

    private Monom(int p) {
        power = p;
    }

    @NonNull
    public static Monom identity() {
        return identity();
    }

    public static Monom monom(int n) {
        return new Monom(n);
    }

    public int power() {
        return power;
    }

    @Override
    public int compareTo(@NonNull Monom o) {
        return power - o.power;
    }

    @NonNull
    @Override
    public Monom multiply(@NonNull Monom monom) {
        return monom(monom.power + power);
    }

    @NonNull
    @Override
    public Monom reciprocal() {
        throw new UnsupportedOperationException();
    }

    @NonNull
    @Override
    public Monom getIdentity() {
        return identity();
    }

    @NonNull
    @Override
    public String toString() {
        return "x^" + String.valueOf(power);
    }
}
