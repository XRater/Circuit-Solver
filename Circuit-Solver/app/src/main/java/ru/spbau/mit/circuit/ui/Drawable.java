package ru.spbau.mit.circuit.ui;


enum HighlightedWire {
    LEFT,
    RIGHT,
    NO;
}

public interface Drawable {
    void draw(MyCanvas canvas, HighlightedWire side);
}