package ru.spbau.mit.circuit.ui.DrawableElements;

import android.graphics.Canvas;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

import ru.spbau.mit.circuit.model.elements.Element;
import ru.spbau.mit.circuit.model.elements.Wire;
import ru.spbau.mit.circuit.model.node.Point;
import ru.spbau.mit.circuit.ui.DrawableModel;
import ru.spbau.mit.circuit.ui.DrawableNode;

import static ru.spbau.mit.circuit.ui.Drawer.CELL_SIZE;
import static ru.spbau.mit.circuit.ui.Drawer.FIELD_SIZE;
import static ru.spbau.mit.circuit.ui.Drawer.HIGHLIGHT_PAINT;

public class DrawableWire extends Wire implements Drawable {
    private static int dist[][] = new int[FIELD_SIZE][FIELD_SIZE];
    private static Point prev[][] = new Point[FIELD_SIZE][FIELD_SIZE];
    private ArrayList<Point> path = new ArrayList<>();

    public DrawableWire(DrawableNode from, DrawableNode to) {
        super(from, to);
        build();
    }

    @Override
    public void draw(Canvas canvas) {
        for (int i = 0; i < path.size() - 1; i++) {
            canvas.drawLine(path.get(i).x(), path.get(i).y(),
                    path.get(i + 1).x(), path.get(i + 1).y(), HIGHLIGHT_PAINT);
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
        while (!queue.isEmpty()) {
            Point p = queue.poll();
            x = p.x();
            y = p.y();
            if (x + 1 < FIELD_SIZE && !(DrawableModel.getByPoint(new Point(x + 1, y)) instanceof Element)) {
                if (dist[x + 1][y] > dist[x][y] + 1) {
                    dist[x + 1][y] = dist[x][y] + 1;
                    prev[x + 1][y] = p;
                    queue.add(new Point(x + 1, y));
                }
            }
            if (x - 1 >= 0 && !(DrawableModel.getByPoint(new Point(x - 1, y)) instanceof Element)) {
                if (dist[x - 1][y] > dist[x][y] + 1) {
                    dist[x - 1][y] = dist[x][y] + 1;
                    prev[x - 1][y] = p;
                    queue.add(new Point(x - 1, y));
                }
            }
            if (y + 1 < FIELD_SIZE && !(DrawableModel.getByPoint(new Point(x, y + 1)) instanceof Element)) {
                if (dist[x][y + 1] > dist[x][y] + 1) {
                    dist[x][y + 1] = dist[x][y] + 1;
                    prev[x][y + 1] = p;
                    queue.add(new Point(x, y + 1));
                }
            }
            if (y - 1 >= 0 && !(DrawableModel.getByPoint(new Point(x, y - 1)) instanceof Element)) {
                if (dist[x][y - 1] > dist[x][y] + 1) {
                    dist[x][y - 1] = dist[x][y] + 1;
                    prev[x][y - 1] = p;
                    queue.add(new Point(x, y - 1));
                }
            }
        }
        Point p = new Point(to().x() / CELL_SIZE, to().y() / CELL_SIZE);
        while (!p.equals(start)) {
            path.add(new Point(p.x() * CELL_SIZE, p.y() * CELL_SIZE));
            p = prev[p.x()][p.y()];
        }
        path.add(from().position());
    }

    public boolean adjacent(Element element) {
        return to() == element.to() || to() == element.from() ||
                from() == element.to() || from() == element.from();
    }

    public ArrayList<Point> getPath() {
        return path;
    }

    public void clearPath() {
        path.clear();
    }
}
