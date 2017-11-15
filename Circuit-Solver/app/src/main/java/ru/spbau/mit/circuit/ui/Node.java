package ru.spbau.mit.circuit.ui;


import ru.spbau.mit.circuit.model.point.Point;

public class Node {

    private final Point point;

    Node(Point point) {
        this.point = point;
    }

    public Point point() {
        return point;
    }

    @Override
    public int hashCode() {
        return point.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Node && point.equals(obj);
    }

    @Override
    public String toString() {
        return point.toString();
    }
}
