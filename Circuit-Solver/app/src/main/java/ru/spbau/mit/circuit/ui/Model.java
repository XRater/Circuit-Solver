package ru.spbau.mit.circuit.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.spbau.mit.circuit.MainActivity;
import ru.spbau.mit.circuit.model.CircuitObject;
import ru.spbau.mit.circuit.model.elements.Element;
import ru.spbau.mit.circuit.model.elements.Movable;
import ru.spbau.mit.circuit.model.node.Point;
import ru.spbau.mit.circuit.ui.DrawableElements.Drawable;
import ru.spbau.mit.circuit.ui.DrawableElements.DrawableWire;

public class Model {
    public List<Drawable> drawables = new ArrayList<>();
    public List<DrawableWire> wires = new ArrayList<>();
    public Point highlighted;
    public boolean inWireMode;
    private Drawer drawer;
    private Map<Point, CircuitObject> field = new HashMap<>();
    private boolean showingCurrents = false;
    private WireEnd holded;


    Model(Drawer drawer) {
        this.drawer = drawer;
    }

    public CircuitObject getByPoint(Point p) {
        return field.get(Drawer.round(p));
    }

    public void addElement(Drawable e) {
        drawables.add(e);
        MainActivity.ui.addToModel((Element) e);
        drawer.drawModel(this);
    }

    public void redraw() {
        drawer.drawModel(this);
    }

    public void showCurrents() {
        showingCurrents = true;
    }

    public void move(Movable movable, Point point) {
    }

    // No need to unhold
    public void connect(WireEnd chosen) {

    }

    public void hold(WireEnd chosen) {
        holded = chosen;
    }

    public boolean holding() {
        return holded != null;
    }

    public void unhold() {
        holded = null;
    }
}
