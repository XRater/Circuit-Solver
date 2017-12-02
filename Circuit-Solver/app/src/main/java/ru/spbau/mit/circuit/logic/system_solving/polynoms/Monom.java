package ru.spbau.mit.circuit.logic.system_solving.polynoms;


import android.support.annotation.NonNull;

import ru.spbau.mit.circuit.logic.system_solving.exceptions.InvalidMonomAdditionException;
import ru.spbau.mit.circuit.logic.system_solving.variables.Variable;

public class Monom implements Comparable<Monom>, Linear<Monom> {

    private double cf;

    private final Variable var;

    public Monom(Variable variable) {
        var = variable;
    }

    public Monom(Variable variable, double coefficient) {
        this.var = variable;
        cf = coefficient;
    }

    public double coefficient() {
        return cf;
    }

    @Override
    public void add(final Monom m) {
        if (!m.var.equals(var)) {
            System.err.println(m);
            System.err.println(this);
            throw new InvalidMonomAdditionException();
        }
        cf += m.cf;
    }

    @Override
    public void mul(double d) {
        cf *= d;
    }

    @Override
    public int compareTo(@NonNull Monom o) {
        return var.compareTo(o.var);
    }

    @Override
    public String toString() {
        return cf + var.toString();
    }

    public String getStringValue() {
        return Math.abs(cf) + var.toString();
    }
}
