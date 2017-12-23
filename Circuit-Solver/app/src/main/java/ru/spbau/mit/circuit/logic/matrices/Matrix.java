package ru.spbau.mit.circuit.logic.matrices;


import org.apache.commons.math3.FieldElement;
import org.apache.commons.math3.linear.RealMatrix;

public class Matrix<T extends FieldElement<T>> {

    private final T[][] data;

    public Matrix(int size) {
        data = (T[][]) new Object[size][size];
    }

    // TODO
    public Matrix(RealMatrix matrix) {
        data = null;
    }

    public void set(int i, int j, T t) {
        data[i][j] = t;
    }

    public T get(int i, int j) {
        return data[i][j];
    }
}
