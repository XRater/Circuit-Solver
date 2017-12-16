package ru.spbau.mit.circuit.ui.DrawableElements;

import android.graphics.Canvas;

import java.util.ArrayDeque;
import java.util.LinkedHashSet;
import java.util.Queue;

import ru.spbau.mit.circuit.model.elements.Element;
import ru.spbau.mit.circuit.model.elements.Wire;
import ru.spbau.mit.circuit.model.node.Point;
import ru.spbau.mit.circuit.ui.DrawableModel;
import ru.spbau.mit.circuit.ui.DrawableNode;

import static ru.spbau.mit.circuit.ui.Drawer.CELL_SIZE;
import static ru.spbau.mit.circuit.ui.Drawer.FIELD_SIZE;
import static ru.spbau.mit.circuit.ui.Drawer.WIRE_PAINT;

public class DrawableWire extends Wire implements Drawable {
    private static int dist[][] = new int[FIELD_SIZE][FIELD_SIZE];
    private static Point prev[][] = new Point[FIELD_SIZE][FIELD_SIZE];
    private LinkedHashSet<Point> path = new LinkedHashSet<>();

    public DrawableWire(DrawableNode from, DrawableNode to) {
        super(from, to);
//        build();
    }

    @Override
    public void draw(Canvas canvas) {
        Point prev = null;
        for (Point nxt : path) {
            if (prev != null) {
                canvas.drawLine(prev.x(), prev.y(),
                        nxt.x(), nxt.y(), WIRE_PAINT);
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
            // FIXME  it must be handled normally
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
        if (drawable instanceof Element)
            return false;
        if (drawable instanceof DrawableNode) {
            DrawableNode node = (DrawableNode) drawable;
            if (!node.position().equals(to().position()) && node.isRealNode()) {
                return false;
            }
        }
        return true;
    }

    private boolean areOverlapping(Point p, Point point) {
        for (DrawableWire wire : DrawableModel.wires()) {
            if (wire.path.contains(p) && wire.path.contains(point)) {
                return false;
            }
        }
        return true;
    }

    public boolean adjacent(Element element) {
        return to() == element.to() || to() == element.from() ||
                from() == element.to() || from() == element.from();
    }

    public LinkedHashSet<Point> getPath() {
        return path;
    }

    public void clearPath() {
        path.clear();
    }
}
