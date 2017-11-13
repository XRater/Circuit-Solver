package ru.spbau.mit.circuit.ui;

import android.view.MotionEvent;
import android.view.View;

import ru.spbau.mit.circuit.model.Element;
import ru.spbau.mit.circuit.model.Point;

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
                mX = motionEvent.getX() - offsetX;
                mY = motionEvent.getY() - offsetY;
                Point rounded = new Point(Math.round(mX / cellSize) * cellSize, Math.round(mY / cellSize) * cellSize);
                Point scaled = new Point(Math.round(mX / cellSize), Math.round(mY / cellSize));

                for (Drawable d : drawables) {
                    Element e = (Element) d;
                    if (rounded.equals(e.getFrom()) || rounded.equals(e.getTo())) {
                        if (highlighted == null) {
                            chosen = e;
                            highlighted = rounded;
                        } else {
                            addWire(rounded, e);
                            chosen = null;
                            highlighted = null;
                        }
                        activity.redraw();
                        return true;
                    }
                }

                if (hasWire(scaled)) {
                    if (highlighted != null) {
                        highlighted = rounded;
                    } else {
                        addWire(rounded, null);
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

    public void addWire(Point p, Element other) {
        int x1 = highlighted.x() / cellSize;
        int y1 = highlighted.y() / cellSize;
        int x2 = p.x() / cellSize;
        int y2 = p.y() / cellSize;
        x1 = Math.min(x1, x2);
        y1 = Math.min(y1, y2);
        Point start = null;
        // TODO KRASIVO
        for (int i = x1; i < x2; i++) {
            if (!horizontalWires[i][y1].have) {
                horizontalWires[i][y1].addElementaryWire(chosen, other);
                if (start == null) {
                    start = new Point(i * cellSize, y1 * cellSize);
                }
            } else if (start != null) {
                this.addWire(start, new Point(i * cellSize, y1 * cellSize), other);
                start = null;
            }
        }
        if (start != null) {
            this.addWire(start, new Point(x2 * cellSize, y1 * cellSize), other);
        }

        start = null;
        for (int i = y1; i < y2; i++) {
            if (!verticalWires[x2][i].have) {
                verticalWires[x2][i].addElementaryWire(chosen, other);
                if (start == null) {
                    start = new Point(x2 * cellSize, i * cellSize);
                }
            } else if (start != null) {
                addWire(start, new Point(x2 * cellSize, i * cellSize), other);
                start = null;
            }
        }
        if (start != null) {
            addWire(start, new Point(x2 * cellSize, y2 * cellSize), other);
        }
    }

    private void addWire(Point from, Point to, Element e) {
        Drawer.wires.add(new DrawableWire(from, to, chosen, e));
    }
}
