package ru.spbau.mit.circuit.model.elements;


import ru.spbau.mit.circuit.model.interfaces.Movable;
import ru.spbau.mit.circuit.model.node.Node;
import ru.spbau.mit.circuit.model.node.Point;

abstract public class Element extends Item implements Movable {

    protected final Node from;
    protected final Node to;
    private Point center;

    public Element(Node from, Node to) {
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
        from.replace(new Point(center.x() + (center.y() - from.y()),
                center.y() + (from.x() - center.x())));
        to.replace(new Point(center.x() + (center.y() - to.y()),
                center.y() + (to.x() - center.x())));
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

    public boolean adjacent(Node node) {
        return node == from || node == to;
    }

    @Override
    public String toString() {
        return "Element:" + from.toString() + ":" + to.toString();
    }

    private Point getCenter() {
        return Point.getCenter(from.position(), to.position());
    }

    abstract public String getCharacteristicName();

    abstract public double getCharacteristicValue();

    abstract public void setCharacteristicValue(double value);
}
