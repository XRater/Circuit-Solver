package ru.spbau.mit.circuit.ui;

import android.app.Activity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ru.spbau.mit.circuit.MainActivity;
import ru.spbau.mit.circuit.model.elements.Element;
import ru.spbau.mit.circuit.model.elements.IllegalWireException;
import ru.spbau.mit.circuit.model.elements.Wire;
import ru.spbau.mit.circuit.model.exceptions.NodesAreAlreadyConnected;
import ru.spbau.mit.circuit.model.interfaces.CircuitObject;
import ru.spbau.mit.circuit.model.interfaces.WireEnd;
import ru.spbau.mit.circuit.model.node.Node;
import ru.spbau.mit.circuit.model.node.Point;
import ru.spbau.mit.circuit.ui.DrawableElements.Drawable;
import ru.spbau.mit.circuit.ui.DrawableElements.DrawableWire;

public class DrawableModel {
    private static Map<Point, Drawable> field = new HashMap<>();
    private static Set<DrawableWire> drawableWires = new HashSet<>();
    private final Drawer drawer;
    private Set<Drawable> drawables = new HashSet<>();
    private Set<DrawableNode> realNodes = new HashSet<>();
    private Activity activity;

    private DrawableNode holded;
    private boolean showingCurrents;

    DrawableModel(Activity activity, Drawer drawer) {
        this.activity = activity;
        this.drawer = drawer;
    }

    public static Drawable getByPoint(Point p) {
        return field.get(p);
    }

    public static Set<DrawableWire> wires() {
        return drawableWires;
    }

    public Set<Drawable> drawables() {
        return drawables;
    }

    public Set<DrawableNode> realNodes() {
        return realNodes;
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
        } catch (NodesAreAlreadyConnected ex) {
            Toast toast = Toast.makeText(activity.getApplicationContext(),
                    "Nodes were already connected.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        addNewObjectPosition(e);
        redraw();
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

        if (element.center().equals(point) || !isValid(point, element)) {
            return;
        }
        List<DrawableWire> wiresToUpdate = new ArrayList<>();
        for (DrawableWire wire : drawableWires) {
            //if (wire.adjacent(element)) {
            wiresToUpdate.add(wire);
            deleteOldWirePosition(wire);
            //}
        }

        deleteOldObjectPosition(drawable);
        element.replace(point); // Model changed
        addNewObjectPosition(drawable);

        for (DrawableWire wire : wiresToUpdate) {
//            wire.build();
//            addNewWirePosition(wire);
            addNewObjectPosition(wire);
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
        if (cur == null) {
            return true;
        }
        if (cur instanceof DrawableNode && !((DrawableNode) cur).isRealNode()) {
            return true;
        } else if (!(cur == element || cur == element.to() || cur == element.from())) {
            return false;
        }
        return true;
    }

    public Point getPossiblePosition() {
        int x = 5 * Drawer.CELL_SIZE;
        int y = 5 * Drawer.CELL_SIZE;
        while (!canPut(new Point(x, y))) {
            x += 2 * Drawer.CELL_SIZE;
            y += 2 * Drawer.CELL_SIZE;
        }
        return new Point(x, y);
    }

    private boolean canPut(Point point) {
        return field.get(point) == null &&
                field.get(new Point(point.x() - 2 * Drawer.CELL_SIZE, point.y())) == null &&
                field.get(new Point(point.x() + 2 * Drawer.CELL_SIZE, point.y())) == null &&
                field.get(new Point(point.x() + Drawer.CELL_SIZE, point.y())) == null &&
                field.get(new Point(point.x() - Drawer.CELL_SIZE, point.y())) == null;
    }

    private void addNewObjectPosition(Drawable drawable) {
        if (drawable instanceof Element) {
            Element element = (Element) drawable;
            Point center = element.center();
            field.put(center, drawable);
            field.put(Point.getCenter(center, element.from().position()), drawable);
            field.put(Point.getCenter(center, element.to().position()), drawable);

            field.put(element.to().position(), (DrawableNode) element.to());
            field.put(element.from().position(), (DrawableNode) element.from());
        }
        if (drawable instanceof Wire) {
            drawableWires.add((DrawableWire) drawable);
            ((DrawableWire) drawable).build();
            addNewWirePosition((DrawableWire) drawable);
        }
    }

    private void deleteOldObjectPosition(Drawable drawable) {
        if (drawable instanceof Element) {
            Element element = (Element) drawable;
            Point center = element.center();
            field.remove(center);
            field.remove(Point.getCenter(center, element.from().position()));
            field.remove(Point.getCenter(center, element.to().position()));

            field.remove(element.to().position());
            field.remove(element.from().position());
        }
        if (drawable instanceof Wire) {
            drawableWires.remove(drawable);
            deleteOldWirePosition((DrawableWire) drawable);
        }
    }

    private void addNewWirePosition(DrawableWire wire) {
        LinkedHashSet<Point> path = wire.getPath();
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
        LinkedHashSet<Point> path = wire.getPath();
        for (Point p : path) {
            DrawableNode node = (DrawableNode) field.get(p);
            // FIXME
            if (node != null) {
//                node.deleteWire(wire);
                if (!node.isRealNode())// && node.hasZeroWires())
                {
                    field.put(node.position(), null);
                }
            }
        }
        wire.clearPath();
    }

    public void connect(DrawableNode second) {
        // Collect changes
        DrawableNode first = holded;
        unhold();
        List<CircuitObject> toBeDeleted = new ArrayList<>();
        List<CircuitObject> toBeAdded = new ArrayList<>();

        if (!first.isRealNode()) {
            toBeAdded.add(first);
        }
        if (!second.isRealNode()) {
            toBeAdded.add(second);
        }

        splitWires(first, toBeDeleted, toBeAdded);
        splitWires(second, toBeDeleted, toBeAdded);

        DrawableWire dw = new DrawableWire(first, second);
        toBeAdded.add(dw); // Everything excepting last wire should be added.

        // Try to apply changes to model
        try {
            MainActivity.ui.removeThenAdd(toBeDeleted, toBeAdded);
        } catch (NodesAreAlreadyConnected ex) {
            redraw();
            Toast toast = Toast.makeText(activity.getApplicationContext(),
                    "Nodes were already connected.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        // Apply to UI if succeeded
        for (CircuitObject object : toBeDeleted) {
            Drawable drawable = (Drawable) object;
            deleteOldObjectPosition(drawable);
        }
        drawableWires.removeAll(toBeDeleted);

        for (CircuitObject object : toBeAdded) {
            Drawable drawable = (Drawable) object;
            if (drawable instanceof Node) {
                realNodes.add((DrawableNode) object);
                ((DrawableNode) object).makeReal();
            }
            addNewObjectPosition(drawable);
        }

        redraw();
    }

    private void splitWires(DrawableNode node, List<CircuitObject> toBeDeleted,
                            List<CircuitObject> toBeAdded) {
        if (node.isRealNode()) {
            // No wires through real node.
            return;
        }

        for (DrawableWire wire : drawableWires) {
            if (!wire.getPath().contains(node.position())) {
                continue;
            }
            if (!toBeDeleted.contains(wire)) {
                toBeDeleted.add(wire);
            }

            DrawableWire newWire1 = new DrawableWire((DrawableNode) wire.from(), node);
            DrawableWire newWire2 = new DrawableWire((DrawableNode) wire.to(), node);

            toBeAdded.add(newWire1);
            toBeAdded.add(newWire2);
        }
    }

    public void hold(DrawableNode chosen) {
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
        List<CircuitObject> toBeDeleted = new ArrayList<>();
        if (chosen instanceof Element) {
            for (DrawableWire wire : drawableWires) {
                if (wire.adjacent((Element) chosen)) {
                    toBeDeleted.add(wire);
                    deleteOldWirePosition(wire);
                }
            }
            drawableWires.removeAll(toBeDeleted);
            deleteOldObjectPosition(chosen);
        }
        toBeDeleted.add((CircuitObject) chosen);
        MainActivity.ui.removeFromModel(toBeDeleted);
        drawables.remove(chosen);
        redraw();
    }

    public void removeWire(DrawableNode node) {
//        ArrayList<Wire> toBeDeleted = new ArrayList<>();
        for (DrawableWire wire : drawableWires) {
            if (wire.getPath().contains(node.position())) {
                killWire(wire);
                break;
            }
        }

        //check holded
        if (holded != null && field.get(holded.position()) == null) {
            holded = null;
        }
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
        deleteOldObjectPosition((Drawable) element);
        element.rotate();
        addNewObjectPosition((Drawable) element);
        for (DrawableWire wire : adjacent) {
            wire.build();
            addNewWirePosition(wire);
        }
        redraw();
    }

    public void deleteUnnecessaryNodes(Node node) {
        ArrayList<CircuitObject> toBeDeleted = new ArrayList<>();
        ArrayList<CircuitObject> toBeAdded = new ArrayList<>();
        //for (Node node : unnecessaryNodes) {
        Iterator<Wire> iter = node.wires().iterator();
        DrawableWire del1 = (DrawableWire) iter.next();
        DrawableWire del2 = (DrawableWire) iter.next();
        DrawableNode from;
        if (!del1.from().position().equals(node.position())) {
            from = (DrawableNode) del1.from();
        } else {
            from = (DrawableNode) del1.to();
        }

        DrawableNode to;
        if (!del2.from().position().equals(node.position())) {
            to = (DrawableNode) del2.from();
        } else {
            to = (DrawableNode) del2.to();
        }
        deleteOldWirePosition(del1);
        toBeDeleted.add(del1);
        drawableWires.remove(del1);
        deleteOldWirePosition(del2);
        toBeDeleted.add(del2);
        drawableWires.remove(del2);
        try {
            DrawableWire newWire = new DrawableWire(from, to);
            drawableWires.add(newWire);
            toBeAdded.add(newWire);
            addNewWirePosition(newWire);
        } catch (IllegalWireException e) {
            e.printStackTrace();
        }
        ((DrawableNode) node).makeSimple();
        realNodes.remove(node);
        toBeDeleted.add((DrawableNode) node);
        System.out.println("Deleted " + node);
        //}
        try {
            MainActivity.ui.removeThenAdd(toBeDeleted, toBeAdded);
        } catch (NodesAreAlreadyConnected nodesAreAlreadyConnected) {
            nodesAreAlreadyConnected.printStackTrace();
        }
        redraw();
    }

    private void killWire(DrawableWire wire) {
        deleteOldWirePosition(wire);
        MainActivity.ui.removeFromModel(wire);
        drawableWires.remove(wire);
    }
}
