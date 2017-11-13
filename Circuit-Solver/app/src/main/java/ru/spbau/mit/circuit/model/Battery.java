package ru.spbau.mit.circuit.model;

public class Battery extends Element {

    private double voltage;

    protected Battery(Point from, Point to) {
        super(from, to);
    }
}
