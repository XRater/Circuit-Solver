package ru.spbau.mit.circuit.model;

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
