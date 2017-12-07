package ru.spbau.mit.circuit.logic.gauss;


import ru.spbau.mit.circuit.logic.gauss.algebra.Linear;
import ru.spbau.mit.circuit.logic.gauss.algebra.Numerical;

public class Id implements Linear<Numerical, Id> {

    Numerical n = Numerical.identity();

    @Override
    public Id add(Id item) {
        n = n.add(item.n);
        return this;
    }

    @Override
    public Id mul(Numerical cf) {
        n = n.mul(cf);
        return this;
    }

    @Override
    public String toString() {
        return n.toString();
    }
}
