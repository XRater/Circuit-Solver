package ru.spbau.mit.circuit.ui;

import android.graphics.Canvas;

import ru.spbau.mit.circuit.model.circuitObjects.nodes.Node;
import ru.spbau.mit.circuit.model.circuitObjects.nodes.Point;
import ru.spbau.mit.circuit.ui.DrawableElements.Drawable;

import static ru.spbau.mit.circuit.ui.Drawer.ELEMENTS_PAINT;
import static ru.spbau.mit.circuit.ui.Drawer.NODE_RADIUS;

public class DrawableNode extends Node implements Drawable {
    private boolean realNode = true;

    public DrawableNode(Point point) {
        super(point);
    }

    public DrawableNode(Point point, boolean realNode) {
        super(point);
        this.realNode = realNode;
    }

    public DrawableNode(int x, int y) {
        super(x, y);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(x(), y(), NODE_RADIUS, ELEMENTS_PAINT);
    }

    public boolean isRealNode() {
        return realNode;
    }

    void makeReal() {
        realNode = true;
    }

    void makeSimple() {
        realNode = false;
    }

    public boolean equalPositions(DrawableNode that) {
        return point.equals(that.point);
    }
}
