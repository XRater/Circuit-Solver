package ru.spbau.mit.circuit.model;

public class Capacitor extends Element {
    public float capacity;

    protected Capacitor(Point from, Point to) {
        super(from, to);
    }
}
