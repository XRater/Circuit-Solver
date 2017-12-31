package ru.spbau.mit.circuit.model.circuitObjects.elements;


import android.support.annotation.NonNull;

import java.io.Serializable;

import ru.spbau.mit.circuit.model.circuitObjects.Item;
import ru.spbau.mit.circuit.model.circuitObjects.exceptions.InvalidElementException;
import ru.spbau.mit.circuit.model.circuitObjects.nodes.Node;
import ru.spbau.mit.circuit.model.circuitObjects.nodes.Point;
import ru.spbau.mit.circuit.model.interfaces.Movable;

/**
 * Base class for all elements. Has two nodes on the ends. End nodes cannot be changed.
 * <p>
 * Element may be easily replaced.
 * <p>
 * All elements must be abstract, therefore there is no way to create element inside model package.
 */
public abstract class Element extends Item implements Movable, Serializable {

    @NonNull
    protected final Node from;
    @NonNull
    protected final Node to;
    private Point center;

    public Element(@NonNull Node from, @NonNull Node to) {
        if (from.position().equals(to.position())) {
            throw new InvalidElementException("End points are equal");
        }
        if (from.x() != to.x() && from.y() != to.y()) {
            throw new InvalidElementException("Points are not on the one line");
        }
        this.from = from;
        this.to = to;
        center = getCenter();
    }

    public Point center() {
        return center;
    }

    abstract public String getCharacteristicName();

    abstract public double getCharacteristicValue();

    abstract public void setCharacteristicValue(double value);

    @Override
    public int x() {
        return center.x();
    }

    @Override
    public int y() {
        return center.y();
    }

    @Override
    public void move(int dx, int dy) {
        to.move(dx, dy);
        from.move(dx, dy);
        center = getCenter();
    }

    // some getters
    @NonNull
    public Node from() {
        return from;
    }

    @NonNull
    public Node to() {
        return to;
    }

    private Point getCenter() {
        return Point.getCenter(from.position(), to.position());
    }

    public final boolean isVertical() {
        return from.x() == to.x();
    }

    public final boolean isHorizontal() {
        return from.y() == to.y();
    }

    /**
     * Checks if node is adjacent to the element.
     */
    public boolean adjacent(Node node) {
        return node == from || node == to;
    }

    @NonNull
    @Override
    public String toString() {
        return "Element:" + from.toString() + ":" + to.toString();
    }

    public void rotate() {
        from.replace(new Point(center.x() + (center.y() - from.y()),
                center.y() + (from.x() - center.x())));
        to.replace(new Point(center.x() + (center.y() - to.y()),
                center.y() + (to.x() - center.x())));
    }
}
