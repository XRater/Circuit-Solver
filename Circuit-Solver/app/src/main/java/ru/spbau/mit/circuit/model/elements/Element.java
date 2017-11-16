package ru.spbau.mit.circuit.model.elements;


import ru.spbau.mit.circuit.model.node.Node;
import ru.spbau.mit.circuit.model.node.Point;

abstract public class Element extends Item implements Movable {

    private final Node from;
    private final Node to;
    private Point center;

    public Element(Point from, Point to) {
        if (from.equals(to)) {
            throw new InvalidElementException("End points are equal");
        }
        if (from.x() != to.x() && from.y() != to.y()) {
            throw new InvalidElementException("Points are not on the one line");
        }
        this.from = new Node(from);
        this.to = new Node(to);
        center = getCenter();
    }

    public Point center() {
        return center;
    }

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

    public Node from() {
        return from;
    }

    public Node to() {
        return to;
    }

    public void rotate() {
        //TODO
    }

    public void flip() {
        //TODO
    }

    public final boolean isVertical() {
        return from.x() == to.x();
    }

    public final boolean isHorizontal() {
        return from.y() == to.y();
    }

    @Override
    public String toString() {
        return "Element:" + from.toString() + ":" + to.toString();
    }

    private Point getCenter() {
        return Point.getCenter(from.position(), to.position());
    }
}
