package ru.spbau.mit.circuit.logic.solver;


import org.apache.commons.math3.linear.RealVector;

import ru.spbau.mit.circuit.logic.gauss.functions1.Function;


class PartialSolution {

    private final RealVector vector;
    private final Function function;

    PartialSolution(RealVector vector, Function function) {
        this.vector = vector;
        this.function = function;
    }

    public RealVector vector() {
        return vector;
    }

    public double coefficientAt(int index) {
        return vector.getEntry(index);
    }

    public Function function() {
        return function;
    }

    public Function functionAt(int index) {
        return function.mul(vector.getEntry(index));
    }

    @Override
    public String toString() {
        return function.toString();
    }
}
