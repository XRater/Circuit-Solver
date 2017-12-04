package ru.spbau.mit.circuit.logic.system_solving.polynoms;


import ru.spbau.mit.circuit.logic.system_solving.functions.exceptions.IllegalAdditionException;

public class Monom<T extends Comparable<? super T>> implements Linear<Monom<T>> {

    protected final T value;
    protected double coefficient;

    public Monom(T value) {
        this.value = value;
    }

    public Monom(T value, double coefficient) {
        this.value = value;
        this.coefficient = coefficient;
    }

    public double coefficient() {
        return coefficient;
    }

    public T value() {
        return value;
    }

    @Override
    public void add(Monom<T> item) {
        if (item.value.compareTo(value) != 0) {
            throw new IllegalAdditionException();
        }
        coefficient += item.coefficient;
    }

    @Override
    public void mul(double d) {
        coefficient *= d;
    }

    @Override
    public String toString() {
        return coefficient + ":" + value.toString();
    }
}
