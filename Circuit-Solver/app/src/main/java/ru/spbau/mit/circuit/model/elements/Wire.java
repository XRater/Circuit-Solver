package ru.spbau.mit.circuit.model.elements;


import ru.spbau.mit.circuit.model.CircuitSolverException;
import ru.spbau.mit.circuit.model.point.Point;

public class Wire extends CircuitItem {
    private CircuitItem neighbour1, neighbour2;

    public Wire(Point from, Point to, CircuitItem e1, CircuitItem e2) {
        super(from, to);
        neighbour1 = e1;
        neighbour2 = e2;
    }

    @Override
    public double getVoltage() {
        if (super.getVoltage() != 0) {
            throw new CircuitSolverException();
        }
        return 0;
    }
}
