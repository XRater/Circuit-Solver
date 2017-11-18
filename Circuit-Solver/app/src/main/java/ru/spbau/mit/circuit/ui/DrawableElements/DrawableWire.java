package ru.spbau.mit.circuit.ui.DrawableElements;

import android.graphics.Canvas;

import ru.spbau.mit.circuit.model.elements.Element;
import ru.spbau.mit.circuit.model.elements.Wire;
import ru.spbau.mit.circuit.ui.DrawableNode;
import ru.spbau.mit.circuit.ui.Drawer;


public class DrawableWire extends Wire implements Drawable {

    public DrawableWire(DrawableNode from, DrawableNode to) {
        super(from, to);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawLine(from().x(), from().y(), to().x(), to().y(), Drawer
                .HIGHLIGHT_PAINT);
    }

    public void build() {

    }

    public boolean adjacent(Element element) {
        return to() == element.to() || to() == element.from() ||
                from() == element.to() || from() == element.from();
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
