package ru.spbau.mit.circuit.model.circuitObjects.nodes;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;

import ru.spbau.mit.circuit.model.circuitObjects.wires.Wire;
import ru.spbau.mit.circuit.model.interfaces.CircuitObject;
import ru.spbau.mit.circuit.model.interfaces.Movable;
import ru.spbau.mit.circuit.model.interfaces.WireEnd;
import ru.spbau.mit.circuit.model.interfaces.Wireable;

//ATTENTION you may have two different nodes in one point. Do not override hash/equals.
// In fact, in release version all nodes might be different. But right now they are not.

/**
 * This class represents a point, to which wires are connected.
 * <p>
 * It's position may be changed in opposite for point.
 */
public abstract class Node implements CircuitObject, Movable, WireEnd, Wireable, Serializable {

    @NonNull
    protected Collection<Wire> wires = new LinkedHashSet<>();  // adjacent wires
    protected Point point;

    public Node(Point point) {
        this.point = point;
    }

    public Node(int x, int y) {
        point = new Point(x, y);
    }

    /**
     * Returns collection of adjacent to the node wires.
     */
    @NonNull
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

    @NonNull
    @Override
    public String toString() {
        return "<" + x() + ", " + y() + ">";
    }

    // Do not use outside of the package
    //
    // There was no way to obey using this method in UI, but allow to us in Model.
    // Or at least we hadnot found any logical solution
    public void deleteWire(Wire wire) {
        wires.remove(wire);
    }

    // Do not use outside of the package
    public void addWire(Wire wire) {
        wires.add(wire);
    }
}
