package ru.spbau.mit.circuit.ui.DrawableElements;


import android.graphics.Canvas;

// Do not implement Drawable from Movable. Wont work for wires.
// Now it extends Movable only for compilation.
public interface Drawable {
    void draw(Canvas canvas);

//    int x(); ----> Element has

//    int y(); ----> Element has

//    void updatePosition(int nx, int ny); ----> replace(Point p) of Element

//    default void setX(int x) {
//        this.updatePosition(x, y());  ----> use replace
//    }

//    default void setY(int y) {
//        this.updatePosition(x(), y);  ----> use replace
//    }

//    default Point getPoint() {
//        return new Point(x(), y());   ----> center() of Element
//    }

    // TODO it is temporary
//    default boolean clickedInside(int x, int y) {
//        Point one = new Point(x() + Drawer.CELL_SIZE / 2, y());
//        Point two = new Point(x() - Drawer.CELL_SIZE / 2, y());
//        if (one.isInSquare(x, y, Drawer.CELL_SIZE / 2) || two.isInSquare(x, y, Drawer.CELL_SIZE /
//                2)) {
//            return true;
//        }
//        return false;
//    }
}