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
import ru.spbau.mit.circuit.model.interfaces.CircuitObject;
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
        //horizontal case
        int realx = Math.max(2 * Drawer.CELL_SIZE, point.x());
        realx = Math.min((Drawer.FIELD_SIZE - 2) * Drawer.CELL_SIZE, realx);
        int realy = Math.max(0, point.y());
        realy = Math.min(Drawer.FIELD_SIZE * Drawer.CELL_SIZE, realy);
        point = new Point(realx, realy);

        Element element = (Element) drawable;

        if (element.center().equals(point) || !isValid(point, element))
            return;
        List<DrawableWire> wiresToUpdate = new ArrayList<>();
        for (DrawableWire wire : drawableWires) {
            //if (wire.adjacent(element)) {
            wiresToUpdate.add(wire);
            deleteOldWirePosition(wire);
            //}
        }

        deleteOldElementPosition(drawable);
        element.replace(point); // Model changed
        addNewElementPosition(drawable);

        for (DrawableWire wire : wiresToUpdate) {
            wire.build();
            addNewWirePosition(wire);
        }
        redraw();
        System.out.println(field.size());
    }

    private boolean isValid(Point point, Element element) {
        if (element.isHorizontal()) {
            return isValidPoint(point, element) &&
                    isValidPoint(new Point(point.x() - 2 * Drawer.CELL_SIZE, point.y()), element) &&
                    isValidPoint(new Point(point.x() + 2 * Drawer.CELL_SIZE, point.y()), element) &&
                    isValidPoint(new Point(point.x() + Drawer.CELL_SIZE, point.y()), element) &&
                    isValidPoint(new Point(point.x() - Drawer.CELL_SIZE, point.y()), element);
        } else {
            return isValidPoint(point, element) &&
                    isValidPoint(new Point(point.x(), point.y() - 2 * Drawer.CELL_SIZE), element) &&
                    isValidPoint(new Point(point.x(), point.y() + 2 * Drawer.CELL_SIZE), element) &&
                    isValidPoint(new Point(point.x(), point.y() + Drawer.CELL_SIZE), element) &&
                    isValidPoint(new Point(point.x(), point.y() - Drawer.CELL_SIZE), element);
        }
    }

    private boolean isValidPoint(Point point, Element element) {
        Drawable cur = field.get(point);
        if (cur == null)
            return true;
        if (cur instanceof DrawableNode && !((DrawableNode) cur).isRealNode())
            return true;
        else if (!(cur == element || cur == element.to() || cur == element.from()))
            return false;
        return true;
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
            // FIXME
            if (node != null) {
                node.deleteWire(wire);
                if (!node.isRealNode())// && node.hasZeroWires())
                    field.put(node.position(), null);
            }
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

    public void removeElement(Drawable chosen) {
        if (chosen instanceof Element) {
            List<DrawableWire> adjacent = new ArrayList<>();
            for (DrawableWire wire : drawableWires) {
                if (wire.adjacent((Element) chosen)) {
                    adjacent.add(wire);
                    deleteOldWirePosition(wire);
                    MainActivity.ui.removeFromModel(wire);
                }
            }
            drawableWires.removeAll(adjacent);
            deleteOldElementPosition(chosen);
        }
        MainActivity.ui.removeFromModel((CircuitObject) chosen);
        drawables.remove(chosen);
        redraw();
    }

    public void rotateElement(Element element) {
        List<DrawableWire> adjacent = new ArrayList<>();
        for (DrawableWire wire : drawableWires) {
            if (wire.adjacent(element)) {
                adjacent.add(wire);
                deleteOldWirePosition(wire);
            }
        }
        deleteOldElementPosition((Drawable) element);
        element.rotate();
        addNewElementPosition((Drawable) element);
        for (DrawableWire wire : adjacent) {
            wire.build();
            addNewWirePosition(wire);
        }
    }
}
