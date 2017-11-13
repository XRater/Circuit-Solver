package ru.spbau.mit.circuit.ui;


import ru.spbau.mit.circuit.model.Point;

public interface Drawable {
    void draw(MyCanvas canvas);

    int x();

    int y();

    void setX(int x);

    void setY(int y);

    Point getPoint();

    void updatePosition(int nx, int ny);
}