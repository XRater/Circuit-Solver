package ru.spbau.mit.circuit.ui.DrawableElements;


import ru.spbau.mit.circuit.model.point.Point;
import ru.spbau.mit.circuit.ui.Drawer;
import ru.spbau.mit.circuit.ui.MyCanvas;

public interface Drawable {
    void draw(MyCanvas canvas);

    int x();

    int y();

    void updatePosition(int nx, int ny);

    default void setX(int x) {
        this.updatePosition(x, y());
    }

    default void setY(int y) {
        this.updatePosition(x(), y);
    }

    default Point getPoint() {
        return new Point(x(), y());
    }

    // TODO it is temporary
    default boolean clickedInside(int x, int y) {
        Point one = new Point(x() + Drawer.CELL_SIZE / 2, y);
        Point two = new Point(x() - Drawer.CELL_SIZE / 2, y);
        if (one.isInSquare(x, y, Drawer.CELL_SIZE / 2) || two.isInSquare(x, y, Drawer.CELL_SIZE / 2))
            return true;
        return false;
    }
}