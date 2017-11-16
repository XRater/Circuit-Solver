package ru.spbau.mit.circuit.ui.DrawableElements;

import ru.spbau.mit.circuit.model.elements.Item;
import ru.spbau.mit.circuit.model.elements.Wire;
import ru.spbau.mit.circuit.model.node.Point;
import ru.spbau.mit.circuit.ui.Drawer;
import ru.spbau.mit.circuit.ui.MyCanvas;


public class DrawableWire extends Wire implements Drawable {

    // Nodes are required. Cannot fix fast:(
    public DrawableWire(Point from, Point to, Item e1, Item e2) {
        super(from, to, e1, e2);
    }

    @Override
    public void draw(MyCanvas canvas) {
        canvas.drawLine(from().x(), from().y(), to().x(), to().y(), Drawer
                .highlightPaint);
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
    public void move(int dx, int dy) {

    }

    @Override
    public void replace(Point p) {

    }

//    @Override
//    public void setX(int x) {
//
//    }

//    @Override
//    public void setY(int y) {
//
//    }

//    @Override
//    public Point getPoint() {
//        return null;
//    }

//    @Override
//    public void updatePosition(int nx, int ny) {
//
//    }
}
