package ru.spbau.mit.circuit.ui;

import android.app.Activity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.spbau.mit.circuit.MainActivity;
import ru.spbau.mit.circuit.model.InvalidCircuitObjectAddition;
import ru.spbau.mit.circuit.model.elements.Element;
import ru.spbau.mit.circuit.model.elements.IllegalWireException;
import ru.spbau.mit.circuit.model.interfaces.WireEnd;
import ru.spbau.mit.circuit.model.node.Point;
import ru.spbau.mit.circuit.ui.DrawableElements.Drawable;
import ru.spbau.mit.circuit.ui.DrawableElements.DrawableWire;

public class DrawableModel {
    private static Map<Point, Drawable> field = new HashMap<>();
    private final Drawer drawer;
    private List<Drawable> drawables = new ArrayList<>();
    private List<DrawableWire> drawableWires = new ArrayList<>();
    private Activity activity;
    private WireEnd holded;
    private boolean showingCurrents;

    DrawableModel(Activity activity, Drawer drawer) {
        this.activity = activity;
        this.drawer = drawer;
    }

    public static Drawable getByPoint(Point p) {
        return field.get(p);
    }

    public List<DrawableWire> wires() {
        return drawableWires;
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
        try {
            MainActivity.ui.addToModel((Element) e);
        } catch (InvalidCircuitObjectAddition ex) {
            Toast toast = Toast.makeText(activity.getApplicationContext(),
                    "Nodes were already connected.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        addNewElementPosition(e);
        drawer.drawModel(this);
    }

    public WireEnd getHolded() {
        return holded;
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
        for (DrawableWire wire : drawableWires) {
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
            //node.addWire(wire); I forgot what it does.
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

    public void connect(WireEnd chosen) {
        DrawableWire dw = null;
        try {
            dw = new DrawableWire((DrawableNode) holded, (DrawableNode) chosen);
            MainActivity.ui.addToModel(dw);
        } catch (InvalidCircuitObjectAddition ex) {
            Toast toast = Toast.makeText(activity.getApplicationContext(),
                    "Nodes were already connected.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        } catch (IllegalWireException e) {
            // No info for user.
            return;
        }
        drawableWires.add(dw);
        addNewWirePosition(dw);
        unhold();
        redraw();
    }

    public void hold(WireEnd chosen) {
        holded = chosen;
        redraw();
    }

    public boolean holding() {
        return holded != null;
    }

    public void unhold() {
        holded = null;
    }

    public void clear() {
        drawables.clear();
        drawableWires.clear();
        field.clear();
        showingCurrents = false;
        holded = null;
    }
}
