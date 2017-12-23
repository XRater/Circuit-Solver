package ru.spbau.mit.circuit.logic.gauss.functions1;


import org.apache.commons.math3.FieldElement;

import ru.spbau.mit.circuit.logic.matrices.Matrix;

public class Polynom<T extends FieldElement<T>> extends Function {

    public Polynom(PolyFunction f) {
        super(f);
    }

    public T evaluate(T arg) {
        return null;
    }

    public Matrix<T> evaluate(Matrix<T> matrix) {
        return null;
    }
}
