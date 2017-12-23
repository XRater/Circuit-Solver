package ru.spbau.mit.circuit.logic.matrices;


import ru.spbau.mit.circuit.logic.gauss.functions1.PolyFunction;

class FunctionMatrix {

    private final PolyFunction[][] data;

    public FunctionMatrix(int size) {
        this.data = new PolyFunction[size][size];
    }

    public void set(int i, int j, PolyFunction f) {
        data[i][j] = f;
    }

    public PolyFunction get(int i, int j) {
        return data[i][j];
    }
}
