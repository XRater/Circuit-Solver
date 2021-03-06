package ru.spbau.mit.circuit.ui.DrawableElements;

import android.graphics.Canvas;
import android.support.annotation.NonNull;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import ru.spbau.mit.circuit.model.circuitObjects.elements.Element;
import ru.spbau.mit.circuit.model.circuitObjects.nodes.Node;
import ru.spbau.mit.circuit.model.circuitObjects.nodes.Point;
import ru.spbau.mit.circuit.model.circuitObjects.wires.Wire;
import ru.spbau.mit.circuit.ui.DrawableModel;
import ru.spbau.mit.circuit.ui.DrawableNode;

import static ru.spbau.mit.circuit.ui.Drawer.CELL_SIZE;
import static ru.spbau.mit.circuit.ui.Drawer.FIELD_SIZE;
import static ru.spbau.mit.circuit.ui.Drawer.WIRE_PAINT;

public class DrawableWire extends Wire implements Drawable {
    @NonNull
    private static int dist[][] = new int[FIELD_SIZE][FIELD_SIZE];
    @NonNull
    private static Point prev[][] = new Point[FIELD_SIZE][FIELD_SIZE];

    private Set<DrawableWire> wires;

    @NonNull
    private LinkedHashSet<Point> path = new LinkedHashSet<>();

    public DrawableWire(DrawableNode from, DrawableNode to, Set<DrawableWire> wires) {
        super(from, to);
        this.wires = wires;
    }

    public static void mergePath(@NonNull DrawableWire first, @NonNull DrawableWire second, @NonNull Node common) {
        Point startFirst = first.getPath().iterator().next();
        Point startSecond = second.getPath().iterator().next();

        if (startFirst.equals(common.position())) {
            List<Point> list1 = new LinkedList<>(first.getPath());
            Collections.reverse(list1);
            first.getPath().clear();
            first.getPath().addAll(list1);
        }
        if (!(startSecond.equals(common.position()))) {
            List<Point> list2 = new LinkedList<>(second.getPath());
            Collections.reverse(list2);
            second.getPath().clear();
            second.getPath().addAll(list2);
        }
        first.getPath().addAll(second.getPath());
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Point prev = null;
        for (Point nxt : path) {
            if (prev != null) {
                canvas.drawLine(prev.x(), prev.y(), nxt.x(), nxt.y(), WIRE_PAINT);
            }
            prev = nxt;
        }
    }

    public void build() {
        for (int i = 0; i < FIELD_SIZE; i++) {
            for (int j = 0; j < FIELD_SIZE; j++) {
                dist[i][j] = Integer.MAX_VALUE;
                prev[i][j] = null;
            }
        }

        Queue<Point> queue = new ArrayDeque<>();
        int x = from().x() / CELL_SIZE;
        int y = from().y() / CELL_SIZE;
        Point start = new Point(x, y);
        dist[x][y] = 0;
        queue.add(start);
        while (!queue.isEmpty() &&
                dist[to().x() / CELL_SIZE][to().y() / CELL_SIZE] == Integer.MAX_VALUE) {
            Point p = queue.poll();
            x = p.x();
            y = p.y();
            Point scaled = new Point(x * CELL_SIZE, y * CELL_SIZE);

            if (x + 1 < FIELD_SIZE && (canGo(new Point((x + 1) * CELL_SIZE, y * CELL_SIZE)))
                    && areOverlapping(scaled, new Point((x + 1) * CELL_SIZE, y * CELL_SIZE))) {
                if (dist[x + 1][y] > dist[x][y] + 1) {
                    dist[x + 1][y] = dist[x][y] + 1;
                    prev[x + 1][y] = p;
                    queue.add(new Point(x + 1, y));
                }
            }
            if (x - 1 >= 0 && (canGo(new Point((x - 1) * CELL_SIZE, y * CELL_SIZE)))
                    && areOverlapping(scaled, new Point((x - 1) * CELL_SIZE, y * CELL_SIZE))) {
                if (dist[x - 1][y] > dist[x][y] + 1) {
                    dist[x - 1][y] = dist[x][y] + 1;
                    prev[x - 1][y] = p;
                    queue.add(new Point(x - 1, y));
                }
            }
            if (y + 1 < FIELD_SIZE && (canGo(new Point(x * CELL_SIZE, (y + 1)
                    * CELL_SIZE)))
                    && areOverlapping(scaled, new Point(x * CELL_SIZE, (y + 1) * CELL_SIZE))) {
                if (dist[x][y + 1] > dist[x][y] + 1) {
                    dist[x][y + 1] = dist[x][y] + 1;
                    prev[x][y + 1] = p;
                    queue.add(new Point(x, y + 1));
                }
            }
            if (y - 1 >= 0 && (canGo(new Point(x * CELL_SIZE, (y - 1) *
                    CELL_SIZE)))
                    && areOverlapping(scaled, new Point(x * CELL_SIZE, (y - 1) * CELL_SIZE))) {
                if (dist[x][y - 1] > dist[x][y] + 1) {
                    dist[x][y - 1] = dist[x][y] + 1;
                    prev[x][y - 1] = p;
                    queue.add(new Point(x, y - 1));
                }
            }
        }
        Point p = new Point(to().x() / CELL_SIZE, to().y() / CELL_SIZE);
        if (prev[p.x()][p.y()] == null) {
            // FIXME it must be handled normally
            // i still dont know how
            return;
        }
        while (!p.equals(start)) {
            path.add(new Point(p.x() * CELL_SIZE, p.y() * CELL_SIZE));
            p = prev[p.x()][p.y()];
        }
        path.add(from().position());
    }

    private boolean canGo(Point point) {
        Drawable drawable = DrawableModel.getByPoint(point);
        if (drawable instanceof Element) {
            return false;
        }
        if (drawable instanceof DrawableNode) {
            DrawableNode node = (DrawableNode) drawable;
            if (!node.position().equals(to().position()) && node.isRealNode()) {
                return false;
            }
        }
        return true;
    }

    private boolean areOverlapping(Point p, Point point) {
        for (DrawableWire wire : wires) {
            if (wire.path.contains(p) && wire.path.contains(point)) {
                return false;
            }
        }
        return true;
    }

    public boolean adjacent(@NonNull Element element) {
        return to() == element.to() || to() == element.from() ||
                from() == element.to() || from() == element.from();
    }

    @NonNull
    public LinkedHashSet<Point> getPath() {
        return path;
    }

    public void clearPath() {
        path.clear();
    }
}
