package ru.spbau.mit.circuit.ui;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

import ru.spbau.mit.circuit.model.node.Node;
import ru.spbau.mit.circuit.model.node.Point;
import ru.spbau.mit.circuit.ui.DrawableElements.Drawable;
import ru.spbau.mit.circuit.ui.DrawableElements.DrawableWire;

public class DrawableNode extends Node implements Drawable {
    private boolean ElementNode;
    private List<DrawableWire> wires = new ArrayList<>();

    public DrawableNode(Point point) {
        super(point);
    }

    public DrawableNode(int x, int y) {
        super(x, y);
    }

    public List<DrawableWire> wires() {
        return wires;
    }

    @Override
    public void draw(Canvas canvas) {

    }
}
