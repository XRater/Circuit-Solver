package ru.spbau.mit.circuit.model.elements;

import ru.spbau.mit.circuit.model.point.InvalidPointException;
import ru.spbau.mit.circuit.model.point.Point;

abstract public class CircuitItem {
    private Point from;
    private Point to;

    private double current; // TODO It should be a function.
    private double voltage;

    protected CircuitItem(Point from, Point to) {
        if (from.equals(to)) {
            throw new InvalidCircuitItemException("Equals end points");
        }
        if (from.x() != to.x() && from.y() != to.y()) {
            throw new InvalidPointException("Points are not on the one line");
        }
        this.from = from;
        this.to = to;
    }

    public void setPosition(Point to, Point from) {
        this.to = to;
        this.from = from;
    }

    public Point getFrom() {
        return from;
    }

    public void setFrom(Point from) {
        this.from = from;
    }

    public Point getTo() {
        return to;
    }

    public void setTo(Point to) {
        this.to = to;
    }

    public double getCurrent() {
        return current;
    }

    public void setCurrent(double current) {
        this.current = current;
    }

    public double getVoltage() {
        return voltage;
    }

    public void setVoltage(double voltage) {
        this.voltage = voltage;
    }

    public final boolean isVertical() {
        return from.x() == to.x();
    }

    public final boolean isHorizontal() {
        return from.y() == to.y();
    }

    public void move(int dx, int dy) {
        to = new Point(to.x() + dx, to.y() + dy);
        from = new Point(from.x() + dx, from.y() + dy);
    }

    @Override
    public String toString() {
        return from.toString() + ":" + to.toString();
    }
}
