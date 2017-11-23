package ru.spbau.mit.circuit.ui;

import android.graphics.Canvas;

import ru.spbau.mit.circuit.model.node.Node;
import ru.spbau.mit.circuit.model.node.Point;
import ru.spbau.mit.circuit.ui.DrawableElements.Drawable;

public class DrawableNode extends Node implements Drawable {

//    private List<DrawableWire> wires = new ArrayList<>();

    public DrawableNode(Point point) {
        super(point);
    }

    public DrawableNode(int x, int y) {
        super(x, y);
    }

    @Override
    public void draw(Canvas canvas) {

    }
}
