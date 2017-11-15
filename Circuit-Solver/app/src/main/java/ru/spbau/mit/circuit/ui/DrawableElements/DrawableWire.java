package ru.spbau.mit.circuit.ui.DrawableElements;

import ru.spbau.mit.circuit.model.Point;
import ru.spbau.mit.circuit.model.Elements.Element;
import ru.spbau.mit.circuit.model.Elements.Wire;
import ru.spbau.mit.circuit.ui.Drawer;
import ru.spbau.mit.circuit.ui.MyCanvas;


public class DrawableWire extends Wire implements Drawable {
    public DrawableWire(Point from, Point to, Element e1, Element e2) {
        super(from, to, e1, e2);
    }

    @Override
    public void draw(MyCanvas canvas) {
        canvas.drawLine(getFrom().x(), getFrom().y(), getTo().x(), getTo().y(), Drawer
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
    public void setX(int x) {

    }

    @Override
    public void setY(int y) {

    }

    @Override
    public Point getPoint() {
        return null;
    }

    @Override
    public void updatePosition(int nx, int ny) {

    }
}
