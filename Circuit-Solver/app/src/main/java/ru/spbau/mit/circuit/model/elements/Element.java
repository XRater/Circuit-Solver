package ru.spbau.mit.circuit.model.elements;


import ru.spbau.mit.circuit.model.point.Point;

abstract public class Element extends CircuitItem {

    private Point center;

    protected Element(Point from, Point to) {
        super(from, to);
        center = Point.getCenter(from, to);
    }

    public Point center() {
        return center;
    }

    public void setPosition(Point center) {
        move(center.x() - this.center.x(), center.y() - this.center.y());
        this.center = center();
    }

    @Override
    public void move(int dx, int dy) {
        super.move(dx, dy);
        center = new Point(center.x() + dx, center.y() + dy);
    }

    public void rotate() {
        //TODO
    }

    public void flip() {
        //TODO
    }
}
