package ru.spbau.mit.circuit.ui;


enum Highlighted {
    LEFT,
    RIGHT,
    NO;
}

public interface Drawable {
    void draw(MyCanvas canvas);

    void highlight(Highlighted side);
}