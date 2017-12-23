package ru.spbau.mit.circuit.logic.gauss;


import org.apache.commons.math3.util.BigReal;

import ru.spbau.mit.circuit.logic.gauss.algebra.Linear;

public class Id implements Linear<BigReal, Id> {

    BigReal n = BigReal.ONE;

    @Override
    public Id add(Id item) {
        n = n.add(item.n);
        return this;
    }

    @Override
    public Id mul(BigReal cf) {
        n = n.multiply(cf);
        return this;
    }

    @Override
    public String toString() {
        return n.toString();
    }
}
