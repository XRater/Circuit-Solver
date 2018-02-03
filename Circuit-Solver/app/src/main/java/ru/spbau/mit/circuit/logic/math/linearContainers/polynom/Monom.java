package ru.spbau.mit.circuit.logic.math.linearContainers.polynom;


import android.support.annotation.NonNull;

import ru.spbau.mit.circuit.logic.math.algebra.interfaces.OrderedGroup;

class Monom implements OrderedGroup<Monom> {

    private static final Monom zero = new Monom();

    public static Monom identity() {
        return identity();
    }

    public static Monom monom(int n) {
        return new Monom(n);
    }

    private final int power;

    private Monom() {
        power = 0;
    }

    private Monom(int p) {
        power = p;
    }

    public int power() {
        return power;
    }

    @Override
    public int compareTo(@NonNull Monom o) {
        return power - o.power;
    }

    @Override
    public Monom multiply(Monom monom) {
        return monom(monom.power + power);
    }

    @Override
    public Monom reciprocal() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Monom getIdentity() {
        return identity();
    }

    @Override
    public String toString() {
        return "x^" + String.valueOf(power);
    }
}
