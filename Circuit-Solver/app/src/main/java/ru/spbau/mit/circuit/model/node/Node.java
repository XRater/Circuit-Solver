package ru.spbau.mit.circuit.model.node;

import java.util.Collection;
import java.util.HashSet;

import ru.spbau.mit.circuit.model.elements.Wire;
import ru.spbau.mit.circuit.model.interfaces.CircuitObject;
import ru.spbau.mit.circuit.model.interfaces.Movable;
import ru.spbau.mit.circuit.model.interfaces.WireEnd;
import ru.spbau.mit.circuit.model.interfaces.Wireable;

//ATTENTION you may have two different nodes in one point. Do not override hash/equals.

// In fact, in release version all nodes might be different. But right now they are not.
public abstract class Node implements CircuitObject, Movable, WireEnd, Wireable {

    private Point point;
    private Collection<Wire> wires = new HashSet<>();

    public Node(Point point) {
        this.point = point;
    }

    public Node(int x, int y) {
        point = new Point(x, y);
    }

    @Override
    public Collection<Wire> wires() {
        return wires;
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

    public void addWire(Wire wire) {
        wires.add(wire);
    }

    public void removeWire(Wire wire) {
        wires.remove(wire);
    }
}
