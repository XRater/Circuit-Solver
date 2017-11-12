package ru.spbau.mit.circuit.ui;


enum HighlightedWire {
    LEFT,
    RIGHT,
    NO;
}

public interface Drawable {
    void draw(MyCanvas canvas, HighlightedWire side);

    public int x();

    public int y();

    public void setX(int x);

    public void setY(int y);
}