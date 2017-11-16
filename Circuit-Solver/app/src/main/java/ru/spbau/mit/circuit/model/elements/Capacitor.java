package ru.spbau.mit.circuit.model.elements;

import ru.spbau.mit.circuit.model.node.Point;

public class Capacitor extends Element {
    private float capacity;

    protected Capacitor(Point from, Point to) {
        super(from, to);
    }
}
