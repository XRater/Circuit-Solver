package ru.spbau.mit.circuit.model.elements;

import ru.spbau.mit.circuit.model.point.InvalidPointException;
import ru.spbau.mit.circuit.model.point.Point;

abstract public class Element {
    private Point from;
    private Point to;

    private double current; // TODO It should be a function.
    private double voltage;

    protected Element(Point from, Point to) {
        if (from.equals(to)) {
            throw new InvalidPointException();
        }
        if (from.x() != to.x() && from.y() != to.y()) {
            throw new InvalidPointException();
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

    public boolean isVertical() {
        return from.x() == to.x();
    }

    public boolean isHorizontal() {
        return from.y() == to.y();
    }

    public void rotate() {
        //TODO
    }

    public void flip() {
        //TODO
    }

    @Override
    public String toString() {
        return from.toString() + ":" + to.toString();
    }
}
