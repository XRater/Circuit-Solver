package ru.spbau.mit.circuit.model;

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

    public double getVoltage() {
        return voltage;
    }

    public double getCurrent() {
        return current;
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
}
