package ru.spbau.mit.circuit.model;


public class Wire extends Element {

    public Wire(Point from, Point to) {
        super(from, to);
    }

    @Override
    public double getVoltage() {
        if (super.getVoltage() != 0) {
            throw new CircuitSolverException();
        }
        return 0;
    }
}
