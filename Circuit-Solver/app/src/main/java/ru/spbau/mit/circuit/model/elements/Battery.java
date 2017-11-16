package ru.spbau.mit.circuit.model.elements;

import ru.spbau.mit.circuit.model.node.Point;

public class Battery extends Element {

    private double voltage = 10;

    public Battery(Point from, Point to) {
        super(from, to);
    }

    @Override
    public double getVoltage() {
        return voltage;
    }
}
