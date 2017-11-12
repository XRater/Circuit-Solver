package ru.spbau.mit.circuit.ui;

import ru.spbau.mit.circuit.model.Point;
import ru.spbau.mit.circuit.model.Wire;


public class DrawableWire extends Wire implements Drawable {
    public DrawableWire(Point from, Point to) {
        super(from, to);
    }

    @Override
    public void draw(MyCanvas canvas, HighlightedWire side) {
        // TODO Slojna.
    }

    @Override
    public int x() {
        return 0;
    }

    @Override
    public int y() {
        return 0;
    }

    @Override
    public void setX(int x) {

    }

    @Override
    public void setY(int y) {

    }
}
