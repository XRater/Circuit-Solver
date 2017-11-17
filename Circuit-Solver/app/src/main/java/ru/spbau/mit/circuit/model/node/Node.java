package ru.spbau.mit.circuit.model.node;

import ru.spbau.mit.circuit.model.CircuitObject;
import ru.spbau.mit.circuit.model.elements.Movable;
import ru.spbau.mit.circuit.ui.WireEnd;

//ATTENTION you may have two different nodes in one point. Do not override hash/equals.

// In fact, in release version all nodes might be different. But right now they are not.
public class Node implements CircuitObject, Movable, WireEnd {

    private Point point;

    public Node(Point point) {
        this.point = point;
    }

    @Override
    public int x() {
        return point.x();
    }

    @Override
    public int y() {
        return point.y();
    }

    public Point position() {
        return point;
    }

    @Override
    public void move(int dx, int dy) {
        point = new Point(point.x() + dx, point.y() + dy);
    }

    @Override
    public String toString() {
        return "<" + x() + ", " + y() + ">";
    }
}
