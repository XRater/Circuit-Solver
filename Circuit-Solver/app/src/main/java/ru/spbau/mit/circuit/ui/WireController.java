package ru.spbau.mit.circuit.ui;

import android.view.MotionEvent;
import android.view.View;

import ru.spbau.mit.circuit.MainActivity;
import ru.spbau.mit.circuit.model.Elements.Element;
import ru.spbau.mit.circuit.model.Point;
import ru.spbau.mit.circuit.ui.DrawableElements.Drawable;
import ru.spbau.mit.circuit.ui.DrawableElements.DrawableWire;

import static java.lang.Math.max;
import static ru.spbau.mit.circuit.ui.Drawer.cellSize;
import static ru.spbau.mit.circuit.ui.Drawer.drawables;
import static ru.spbau.mit.circuit.ui.Drawer.fieldSize;
import static ru.spbau.mit.circuit.ui.Drawer.highlighted;
import static ru.spbau.mit.circuit.ui.Drawer.offsetX;
import static ru.spbau.mit.circuit.ui.Drawer.offsetY;

public class WireController implements View.OnTouchListener {
    private elementaryWire horizontalWires[][] = new elementaryWire[fieldSize][fieldSize];
    private elementaryWire verticalWires[][] = new elementaryWire[fieldSize][fieldSize];
    private Element chosen;
    private NewCircuitActivity activity;

    public WireController(NewCircuitActivity newCircuitActivity) {
        activity = newCircuitActivity;
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                horizontalWires[i][j] = new elementaryWire();
                verticalWires[i][j] = new elementaryWire();
            }
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        float mX, mY;
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                mX = motionEvent.getX();
                mY = motionEvent.getY();
                Point current = new Point((int) mX - offsetX, (int) mY - offsetY);
                //Point scaled = new Point(Math.round(mX / cellSize), Math.round(mY / cellSize));

                for (Drawable d : drawables) {
                    Element e = (Element) d;
                    if (current.distance(e.getFrom()) < cellSize) {
                        current = e.getFrom();
                    } else if (current.distance(e.getTo()) < cellSize) {
                        current = e.getTo();
                    } else {
                        continue;
                    }

                    if (highlighted == null) {
                        chosen = e;
                        highlighted = current;
                    } else {
                        addSimpleWire(current, e);
                        chosen = null;
                        highlighted = null;
                    }
                    activity.redraw();
                    return true;

                }

                Point scaled = new Point(Math.round(mX / cellSize), Math.round(mY / cellSize));
                current = new Point(scaled.x() * cellSize, scaled.y() * cellSize);
                if (hasWire(scaled)) {
                    if (highlighted != null) {
                        highlighted = current;
                    } else {
                        addSimpleWire(current, null);
                        chosen = null;
                        highlighted = null;
                    }
                    activity.redraw();
                    return true;
                }
                chosen = null;
                highlighted = null;
                activity.redraw();
            }
        }
        return true;

    }

    public boolean hasWire(Point p) {
        return horizontalWires[max(p.x() - 1, 0)][p.y()].have || horizontalWires[p.x()][p.y()].have ||
                verticalWires[p.x()][max(p.y() - 1, 0)].have || verticalWires[p.x()][p.y()].have;
    }

    public void addSimpleWire(Point p, Element other) {
        int x1 = highlighted.x() / cellSize;
        int y1 = highlighted.y() / cellSize;
        int x2 = p.x() / cellSize;
        int y2 = p.y() / cellSize;
        int height = y1;
        if (x1 > x2) {
            int t = x1;
            x1 = x2;
            x2 = t;
            height = y2;
        }

        // TODO KRASIVO
        Point start = null;
        for (int i = x1; i < x2; i++) {
            if (!horizontalWires[i][height].have) {
                horizontalWires[i][height].addElementaryWire(chosen, other);
                if (start == null) {
                    start = new Point(i * cellSize, height * cellSize);
                }
            } else if (start != null) {
                this.addSimpleWire(start, new Point(i * cellSize, height * cellSize), other);
                start = null;
            }
        }
        if (start != null) {
            this.addSimpleWire(start, new Point(x2 * cellSize, height * cellSize), other);
        }

        if (y1 > y2) {
            int t = y1;
            y1 = y2;
            y2 = t;
        }
        start = null;
        for (int i = y1; i < y2; i++) {
            if (!verticalWires[x2][i].have) {
                verticalWires[x2][i].addElementaryWire(chosen, other);
                if (start == null) {
                    start = new Point(x2 * cellSize, i * cellSize);
                }
            } else if (start != null) {
                addSimpleWire(start, new Point(x2 * cellSize, i * cellSize), other);
                start = null;
            }
        }
        if (start != null) {
            addSimpleWire(start, new Point(x2 * cellSize, y2 * cellSize), other);
        }
    }

    private void addSimpleWire(Point from, Point to, Element e) {
        DrawableWire w = new DrawableWire(from, to, chosen, e);
        Drawer.wires.add(w);
        MainActivity.ui.addToModel(w);
    }
}
