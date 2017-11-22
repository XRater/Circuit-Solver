package ru.spbau.mit.circuit.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.spbau.mit.circuit.MainActivity;
import ru.spbau.mit.circuit.model.elements.Element;
import ru.spbau.mit.circuit.model.interfaces.WireEnd;
import ru.spbau.mit.circuit.model.node.Point;
import ru.spbau.mit.circuit.ui.DrawableElements.Drawable;
import ru.spbau.mit.circuit.ui.DrawableElements.DrawableWire;

public class DrawableModel {
    private static Map<Point, Drawable> field = new HashMap<>();
    private final Drawer drawer;
    private List<Drawable> drawables = new ArrayList<>();
    private List<DrawableWire> wires = new ArrayList<>();
    private WireEnd holded;
    private boolean showingCurrents;

    DrawableModel(Drawer drawer) {
        this.drawer = drawer;
    }

    public static Drawable getByPoint(Point p) {
        return field.get(p);
    }

    public List<DrawableWire> wires() {
        return wires;
    }

    public List<Drawable> drawables() {
        return drawables;
    }

    public boolean isShowingCurrents() {
        return showingCurrents;
    }

    public void changeShowingCurrents() {
        showingCurrents = !showingCurrents;
    }

    public void addElement(Drawable e) {
        drawables.add(e);
        MainActivity.ui.addToModel((Element) e);
        addNewElementPosition(e);
        drawer.drawModel(this);
    }

    public void redraw() {
        drawer.drawModel(this);
    }

    public void move(Drawable drawable, Point point) {
        Element element = (Element) drawable;
        // TODO honestly
        if (element.center().equals(point) || !isValid(point, point))
            return;

        List<DrawableWire> wiresToUpdate = new ArrayList<>();
        for (DrawableWire wire : wires) {
            if (wire.adjacent(element)) {
                wiresToUpdate.add(wire);
                deleteOldWirePosition(wire);
            }
        }

        deleteOldElementPosition(drawable);
        element.replace(point); // Model changed
        addNewElementPosition(drawable);

        for (DrawableWire wire : wiresToUpdate) {

            wire.build();
            addNewWirePosition(wire);
        }
        redraw();
    }

    private boolean isValid(Point point1, Point point2) {
        //TODO FINISH
        return !(field.get(point1) instanceof Element) && !(field.get(point2) instanceof Element) &&
                !(field.get(Point.getCenter(point1, point2)) instanceof Element);
    }

    private void addNewElementPosition(Drawable drawable) {
        Element element = (Element) drawable;
        Point center = element.center();
        field.put(center, drawable);
        field.put(Point.getCenter(center, element.from().position()), drawable);
        field.put(Point.getCenter(center, element.to().position()), drawable);

        field.put(element.to().position(), (DrawableNode) element.to());
        field.put(element.from().position(), (DrawableNode) element.from());
    }

    private void deleteOldElementPosition(Drawable drawable) {
        Element element = (Element) drawable;
        Point center = element.center();
        field.remove(center);
        field.remove(Point.getCenter(center, element.from().position()));
        field.remove(Point.getCenter(center, element.to().position()));

        field.remove(element.to().position());
        field.remove(element.from().position());
    }

    private void addNewWirePosition(DrawableWire wire) {
        ArrayList<Point> path = wire.getPath();
        for (Point p : path) {
            DrawableNode node = (DrawableNode) field.get(p);
            if (node == null) {
                node = new DrawableNode(p, false);
                field.put(p, node);
            }
            node.addWire(wire);
        }
        // TODO make node beautiful
    }

    private void deleteOldWirePosition(DrawableWire wire) {
        ArrayList<Point> path = wire.getPath();
        for (Point p : path) {
            DrawableNode node = (DrawableNode) field.get(p);
            node.deleteWire(wire);
            if (!node.isRealNode() && node.hasZeroWires())
                field.put(node.position(), null);
        }
        wire.clearPath();
    }

    // No need to unhold
    public void connect(WireEnd chosen) {
        DrawableWire dw = new DrawableWire((DrawableNode) holded, (DrawableNode) chosen);
        MainActivity.ui.addToModel(dw);
        wires.add(dw);
        addNewWirePosition(dw);
        redraw();
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

    public void clear() {
        drawables.clear();
        wires.clear();
        field.clear();
        showingCurrents = false;
        holded = null;
    }
}
