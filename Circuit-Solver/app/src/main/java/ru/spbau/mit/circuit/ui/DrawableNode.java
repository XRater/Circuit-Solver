package ru.spbau.mit.circuit.ui;

import android.graphics.Canvas;

import ru.spbau.mit.circuit.model.node.Node;
import ru.spbau.mit.circuit.model.node.Point;
import ru.spbau.mit.circuit.ui.DrawableElements.Drawable;
import ru.spbau.mit.circuit.ui.DrawableElements.DrawableWire;

import static ru.spbau.mit.circuit.ui.Drawer.ELEMENTS_PAINT;
import static ru.spbau.mit.circuit.ui.Drawer.NODE_RADIUS;

public class DrawableNode extends Node implements Drawable {
    private boolean realNode = true;

    //private List<DrawableWire> wires = new ArrayList<>();

    public DrawableNode(Point point) {
        super(point);
    }

    public DrawableNode(Point point, boolean realNode, DrawableWire wire) {
        super(point);
        this.realNode = realNode;
        wires.add(wire);
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

    public boolean hasZeroWires() {
        return wires.size() == 0;
    }

    public void makeReal() {
        realNode = true;
    }
}
