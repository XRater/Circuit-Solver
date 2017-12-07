package ru.spbau.mit.circuit.ui;

import android.app.Activity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

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
    private static List<DrawableWire> drawableWires = new ArrayList<>();
    private final Drawer drawer;
    private List<Drawable> drawables = new ArrayList<>();
    private List<DrawableNode> realNodes = new ArrayList<>();
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

    public static List<DrawableWire> wires() {
        return drawableWires;
    }

    public List<Drawable> drawables() {
        return drawables;
    }

    public List<DrawableNode> realNodes() {
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
        LinkedHashSet<Point> path = wire.getPath();
        for (Point p : path) {
            DrawableNode node = (DrawableNode) field.get(p);
            if (node == null) {
                node = new DrawableNode(p, false, wire);
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

    public void connect(DrawableNode chosen) {
        DrawableWire dw = null;
        makeRealIfNeсessary(holded);
        makeRealIfNeсessary(chosen);
        try {
            dw = new DrawableWire((DrawableNode) holded, chosen);
            MainActivity.ui.addToModel(dw);
        } catch (NodesAreAlreadyConnected ex) {
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

    private void makeRealIfNeсessary(DrawableNode node) {
        if (!node.isRealNode()) {
            node.makeReal();
            realNodes.add(node);
            try {
                MainActivity.ui.addToModel(node);
            } catch (NodesAreAlreadyConnected nodesAreAlreadyConnected) {
                nodesAreAlreadyConnected.printStackTrace();
            }
            ArrayList<Wire> toBeDeleted = new ArrayList<>();
            ArrayList<DrawableWire> toBeAdded = new ArrayList<>();
            for (DrawableWire wire : drawableWires) {
                if (!wire.getPath().contains(node.position())) {
                    continue;
                }
                deleteOldWirePosition(wire);
                toBeDeleted.add(wire);
                try {
                    DrawableWire newWire1 = new DrawableWire((DrawableNode) wire.from(), node);
                    DrawableWire newWire2 = new DrawableWire((DrawableNode) wire.to(), node);
                    toBeAdded.add(newWire1);
                    toBeAdded.add(newWire2);
                } catch (IllegalWireException e) {
                    e.printStackTrace();
                }
            }
            for (Wire wire : toBeDeleted) {
                drawableWires.remove(wire);
                MainActivity.ui.removeFromModel(wire);
            }

            for (DrawableWire wire : toBeAdded) {
                try {
                    MainActivity.ui.addToModel(wire);
                    drawableWires.add(wire);
                    addNewWirePosition(wire);
                } catch (NodesAreAlreadyConnected nodesAreAlreadyConnected) {
                    nodesAreAlreadyConnected.printStackTrace();
                }
            }
            redraw();
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

    public void removeWire(DrawableNode node) {
//        ArrayList<Wire> toBeDeleted = new ArrayList<>();
        if (node.wires().size() > 1)
            throw new RuntimeException();
        Wire wire = (Wire) node.wires().toArray()[0];
        //for (Wire wire : node.wires()) {
        killWire((DrawableWire) wire);
//            toBeDeleted.add(wire);
        // }
//        for (Wire wire : toBeDeleted) {
//            MainActivity.ui.removeFromModel(wire);
//        }
        //check holded
        if (holded != null && field.get(holded.position()) == null) {
            holded = null;
        }
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

    public void deleteUnnecessaryNodes(List<Node> unnecessaryNodes) {
        for (Node node : unnecessaryNodes) {
            Iterator<Wire> iter = node.wires().iterator();
            DrawableWire del1 = (DrawableWire) iter.next();
            DrawableWire del2 = (DrawableWire) iter.next();
            DrawableNode from = null;
            if (del1.from().position().equals(node.position())) {
                from = new DrawableNode(del1.from().position());
            } else {
                from = new DrawableNode(del1.to().position());
            }

            DrawableNode to = null;
            if (del2.from().position().equals(node.position())) {
                to = new DrawableNode(del2.from().position());
            } else {
                to = new DrawableNode(del2.to().position());
            }

            killWire(del1);
            killWire(del2);
            try {
                DrawableWire newWire = new DrawableWire(from, to);
                drawableWires.add(newWire);
                MainActivity.ui.addToModel(newWire);
                addNewWirePosition(newWire);
            } catch (IllegalWireException e) {
                e.printStackTrace();
            } catch (NodesAreAlreadyConnected nodesAreAlreadyConnected) {
                nodesAreAlreadyConnected.printStackTrace();
            }

        }
    }

    private void killWire(DrawableWire wire) {
        deleteOldWirePosition(wire);
        MainActivity.ui.removeFromModel(wire);
        drawableWires.remove(wire);
    }
}
