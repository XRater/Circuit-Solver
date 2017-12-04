package ru.spbau.mit.circuit.logic.solver;


import org.apache.commons.math3.linear.RealVector;

import ru.spbau.mit.circuit.logic.system_solving.functions.MonomPolyExponent;

class PartialSolution {

    private final RealVector vector;
    private final MonomPolyExponent polyExponent;

    PartialSolution(RealVector vector, MonomPolyExponent polyExponent) {
        this.vector = vector;
        this.polyExponent = polyExponent;
    }

}
