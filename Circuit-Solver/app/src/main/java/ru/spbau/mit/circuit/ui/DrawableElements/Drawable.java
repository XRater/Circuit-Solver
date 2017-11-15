package ru.spbau.mit.circuit.ui.DrawableElements;


import ru.spbau.mit.circuit.model.Point;
import ru.spbau.mit.circuit.ui.MyCanvas;

public interface Drawable {
    void draw(MyCanvas canvas);

    int x();

    int y();

    default void setX(int x) {
        this.updatePosition(x, y());
    }

    default void setY(int y) {
        this.updatePosition(x(), y);
    }

    default Point getPoint() {
        return new Point(x(), y());
    }

    void updatePosition(int nx, int ny);
}