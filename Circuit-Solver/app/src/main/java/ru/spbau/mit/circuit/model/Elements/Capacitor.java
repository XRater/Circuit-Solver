package ru.spbau.mit.circuit.model.Elements;

import ru.spbau.mit.circuit.model.Point;

public class Capacitor extends Element {
    private float capacity;

    protected Capacitor(Point from, Point to) {
        super(from, to);
    }
}
