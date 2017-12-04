package ru.spbau.mit.circuit.logic.system_solving.polynoms;


import java.util.Arrays;
import java.util.Collection;
import java.util.TreeSet;

/**
 * Sorted container of Monoms.
 *
 * @param <T>
 */
public class Polynom<T extends Comparable<? super T>> implements Linear<Polynom<T>> {

    private TreeSet<Monom<T>> monoms = new TreeSet<>((m1, m2) -> m1.value().compareTo(m2.value()));

    public Polynom() {
    }

    public Polynom(Collection<Monom<T>> monoms) {
        for (Monom<T> monom : monoms) {
            add(monom);
        }
    }

    public Polynom(Monom<T>[] monoms) {
        this(Arrays.asList(monoms));
    }

    public TreeSet<Monom<T>> monoms() {
        return monoms;
    }

    public void add(Monom<T> monom) {
        Monom<T> stored = monoms.ceiling(monom);
        if (stored == null || stored.value.compareTo(monom.value) != 0) {
            monoms.add(monom);
        } else {
            stored.add(monom);
        }
    }

    @Override
    public void add(Polynom<T> polynom) {
        for (Monom<T> monom : polynom.monoms) {
            add(monom);
        }
    }

    @Override
    public void mul(double d) {
        for (Monom<T> monom : monoms) {
            monom.mul(d);
        }
    }

    public double getCoefficient(T parent) {
        return 0;
    }

    @Override
    public String toString() {
        if (monoms.isEmpty()) {
            return "0";
        }
        StringBuilder sb = new StringBuilder();
        for (Monom<T> monom : monoms) {
            sb.append(monom).append(" ");
        }
        return sb.toString();
    }
}
