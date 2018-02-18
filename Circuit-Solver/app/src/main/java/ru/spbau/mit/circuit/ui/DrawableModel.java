package ru.spbau.mit.circuit.ui;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ru.spbau.mit.circuit.MainActivity;
import ru.spbau.mit.circuit.model.circuitObjects.elements.Element;
import ru.spbau.mit.circuit.model.circuitObjects.nodes.Node;
import ru.spbau.mit.circuit.model.circuitObjects.nodes.Point;
import ru.spbau.mit.circuit.model.circuitObjects.wires.Wire;
import ru.spbau.mit.circuit.model.exceptions.NodesAreAlreadyConnected;
import ru.spbau.mit.circuit.model.interfaces.CircuitObject;
import ru.spbau.mit.circuit.model.interfaces.WireEnd;
import ru.spbau.mit.circuit.ui.DrawableElements.Drawable;
import ru.spbau.mit.circuit.ui.DrawableElements.DrawableWire;

public class DrawableModel {
    @NonNull
    private static Map<Point, Drawable> field = new LinkedHashMap<>();
    private final Activity activity;
    @NonNull
    private Set<DrawableWire> drawableWires = new LinkedHashSet<>();
    @NonNull
    private Set<Drawable> drawables = new LinkedHashSet<>();
    @NonNull
    private Set<DrawableNode> realNodes = new LinkedHashSet<>();
    private Drawer drawer;

    @Nullable
    private DrawableNode holded;
    @Nullable
    private Drawable chosen;

    private boolean showingCurrents;

    DrawableModel(Activity activity, Drawer drawer) {
        this.activity = activity;
        this.drawer = drawer;
    }

    // Many methods are package private. We dont want to change model from outside.

    public static Drawable getByPoint(Point p) {
        return field.get(p);
    }

    @NonNull
    public Set<DrawableWire> wires() {
        return drawableWires;
    }

    @NonNull
    Set<Drawable> drawables() {
        return drawables;
    }

    @NonNull
    Set<DrawableNode> realNodes() {
        return realNodes;
    }

    boolean isShowingCurrents() {
        return showingCurrents;
    }

    void changeShowingCurrents() {
        showingCurrents = !showingCurrents;
    }

    /**
     * Adding an element to drawableModel. Information is also transfered to controller.
     *
     * @param e drawable to add.
     */
    void addElement(Drawable e) {
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

    @Nullable
    WireEnd getHolded() {
        return holded;
    }

    void redraw() {
        drawer.drawModel(this);
    }

    /**
     * Moves an object if point is empty. Otherwise nothing happens.
     *
     * @param drawable an object to move.
     * @param point    where to move.
     */
    void move(@NonNull Drawable drawable, @NonNull Point point) {
        int realX = 0, realY = 0;
        if (((Element) drawable).isHorizontal()) {
            realX = Math.max(2 * Drawer.CELL_SIZE, point.x());
            realX = Math.min((Drawer.FIELD_SIZE - 2) * Drawer.CELL_SIZE, realX);
            realY = Math.max(0, point.y());
            realY = Math.min(Drawer.FIELD_SIZE * Drawer.CELL_SIZE, realY);
        } else {
            realX = Math.max(0, point.x());
            realX = Math.min(Drawer.FIELD_SIZE * Drawer.CELL_SIZE, realX);
            realY = Math.max(2 * Drawer.CELL_SIZE, point.y());
            realY = Math.min((Drawer.FIELD_SIZE - 2) * Drawer.CELL_SIZE, realY);
        }
        point = new Point(realX, realY);

        Element element = (Element) drawable;

        if (element.center().equals(point) || !isValid(point, element)) {
            return;
        }
        List<DrawableWire> wiresToUpdate = new ArrayList<>();

        deleteOldObjectPosition(drawable);
        element.replace(point); // Model changed

        for (DrawableWire wire : drawableWires) {
            if (wire.adjacent(element)) {
                wiresToUpdate.add(wire);
                continue;
            }
            if (wire.getPath().contains(element.center()) ||
                    wire.getPath().contains(element.to().position()) ||
                    wire.getPath().contains(element.from().position()) ||
                    wire.getPath().contains(
                            Point.getCenter(element.center(), element.to().position())) ||
                    wire.getPath().contains(
                            Point.getCenter(element.center(), element.from().position()))) {
                wiresToUpdate.add(wire);
            }

        }

        for (DrawableWire wire : wiresToUpdate) {
            deleteOldWirePosition(wire);
        }
        addNewObjectPosition(drawable);
        for (DrawableWire wire : wiresToUpdate) {
            addNewObjectPosition(wire);
        }

        redraw();
    }

    /**
     * Checking if an element can be centered in point.
     * @param point new center.
     * @param element element that is moved.
     * @return true if can move.
     */
    private boolean isValid(@NonNull Point point, @NonNull Element element) {
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

    private boolean isValidPoint(Point point, @NonNull Element element) {
        Drawable cur = field.get(point);
        if (cur == null) {
            return true;
        }
        if (cur instanceof DrawableNode) {
            DrawableNode node = ((DrawableNode) cur);
            if (!node.isRealNode()) {
                return true;
            } else if (cur == element.to() || cur == element.from()) {
                return true;
            }
            return false;
        } else if (!(cur == element)) {
            return false;
        }
        return true;
    }

    /**
     * @return a possible position to spawn new element.
     */
    @NonNull
    Point getPossiblePosition() {
        int x = 5 * Drawer.CELL_SIZE;
        int y = 5 * Drawer.CELL_SIZE;
        while (!canPut(new Point(x, y))) {
            y += 2 * Drawer.CELL_SIZE;
        }
        return new Point(x, y);
    }

    private boolean canPut(@NonNull Point point) {
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
            // To many additions.
            drawableWires.add((DrawableWire) drawable);
            ((DrawableWire) drawable).build();
            addNewWirePosition((DrawableWire) drawable);
        }
        if (drawable instanceof Node) {
            field.put(((Node) drawable).position(), drawable);
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
            drawableWires.remove((DrawableWire) drawable);
            deleteOldWirePosition((DrawableWire) drawable);
        }
    }

    private void addNewWirePosition(@NonNull DrawableWire wire) {
        LinkedHashSet<Point> path = wire.getPath();
        for (Point p : path) {
            DrawableNode node = (DrawableNode) field.get(p);
            if (node == null) {
                node = new DrawableNode(p, false);
                field.put(p, node);
            }
        }
    }

    private void deleteOldWirePosition(@NonNull DrawableWire wire) {
        LinkedHashSet<Point> path = wire.getPath();
        for (Point p : path) {
            DrawableNode node = (DrawableNode) field.get(p);
            if (node != null) {
                if (!node.isRealNode())
                {
                    boolean flag = false;
                    // Now we are checking if this Point is covered with any of other wires.
                    for (DrawableWire another : drawableWires) {
                        if (another != wire && another.getPath().contains(node.position())) {
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        field.remove(node.position());
                    }
                }
            }
        }
        wire.clearPath();
    }

    /**
     * Connecting holded point with given.
     *
     * @param second second end of wire.
     */
    void connect(@NonNull DrawableNode second) {
        if (second.position().equals(holded.position())) {
            return;
        }
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

        DrawableWire dw = new DrawableWire(first, second, drawableWires);
        toBeAdded.add(dw);

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

    private void splitWires(@NonNull DrawableNode node, @NonNull List<CircuitObject> toBeDeleted,
                            @NonNull List<CircuitObject> toBeAdded) {
        if (node.isRealNode()) {
            return; // No wires through real node.
        }

        for (DrawableWire wire : drawableWires) {
            if (!wire.getPath().contains(node.position())) {
                continue;
            }
            if (!toBeDeleted.contains(wire)) {
                toBeDeleted.add(wire);
            }

            DrawableWire newWire1 = new DrawableWire((DrawableNode) wire.from(), node, drawableWires);
            DrawableWire newWire2 = new DrawableWire((DrawableNode) wire.to(), node, drawableWires);

            toBeAdded.add(newWire1);
            toBeAdded.add(newWire2);
        }
    }

    void hold(DrawableNode chosen) {
        holded = chosen;
        redraw();
    }

    boolean holding() {
        return holded != null;
    }

    void unhold() {
        holded = null;
    }

    void clear() {
        drawables.clear();
        drawableWires.clear();
        realNodes.clear();
        field.clear();
        showingCurrents = false;
        holded = null;
        chosen = null;
    }

    /**
     * Remove element from drawableModel. Changes are transfered to controller,
     *
     * @param chosen an element to be removed.
     */
    void removeElement(Drawable chosen) {
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

    void removeWire(@NonNull DrawableNode node) {
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

    /**
     * Rotates an element if possible.
     *
     * @param element an element to rotate.
     */
    void rotateElement(@NonNull Element element) {
        if (!canRotate(element)) {
            Toast.makeText(activity.getApplicationContext(), "It is not possible to rotate the " +
                            "element here.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
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

    private boolean canRotate(@NonNull Element element) {
        int x = element.center().x();
        int y = element.center().y();
        if (element.isHorizontal()) { // If it is horizontal, it is going to become vertical.
            return y >= 2 * Drawer.CELL_SIZE && y <= (Drawer.FIELD_SIZE - 2) * Drawer.CELL_SIZE &&
                    pointIsNotForbidden(new Point(x, y + Drawer.CELL_SIZE)) &&
                    pointIsNotForbidden(new Point(x, y + 2 * Drawer.CELL_SIZE)) &&
                    pointIsNotForbidden(new Point(x, y - Drawer.CELL_SIZE)) &&
                    pointIsNotForbidden(new Point(x, y - 2 * Drawer.CELL_SIZE));
        } else {
            return x >= 2 * Drawer.CELL_SIZE && x <= (Drawer.FIELD_SIZE - 2) * Drawer.CELL_SIZE &&
                    pointIsNotForbidden(new Point(x + Drawer.CELL_SIZE, y)) &&
                    pointIsNotForbidden(new Point(x + 2 * Drawer.CELL_SIZE, y)) &&
                    pointIsNotForbidden(new Point(x - Drawer.CELL_SIZE, y)) &&
                    pointIsNotForbidden(new Point(x - 2 * Drawer.CELL_SIZE, y));
        }
    }

    private boolean pointIsNotForbidden(Point point) {
        Drawable d = field.get(point);
        if (d instanceof Element) {
            return false;
        } else if (d instanceof DrawableNode) {
            DrawableNode node = (DrawableNode) d;
            if (node.isRealNode()) {
                return false;
            }
        }
        return true;
    }

    void deleteUnnecessaryNode(@NonNull Node common, Wire first, Wire second) {
        DrawableWire del1 = (DrawableWire) first;
        DrawableWire del2 = (DrawableWire) second;

        ((DrawableNode) common).makeSimple();
        drawableWires.remove(del2);
        field.put(common.position(), (Drawable) common); // Map modified after deleting wire.

        realNodes.remove(common);
        DrawableWire.mergePath(del1, del2, common);
        redraw();
    }

    private void killWire(@NonNull DrawableWire wire) {
        deleteOldWirePosition(wire);
        MainActivity.ui.removeFromModel(wire);
        drawableWires.remove(wire);
    }

    void addRealNode(DrawableNode node) {
        realNodes.add(node);
        addNewObjectPosition(node);
    }

    void loadElement(Drawable element) {
        drawables.add(element);
        addNewObjectPosition(element);
    }

    void loadWire(@NonNull DrawableWire wire) {
        drawableWires.add(wire);
        addNewWirePosition(wire);
    }

    void setDrawer(Drawer drawer) {
        this.drawer = drawer;
    }

    @Nullable
    Drawable chosen() {
        return chosen;
    }

    void setChosen(Drawable chosen) {
        this.chosen = chosen;
        redraw();
    }
}
