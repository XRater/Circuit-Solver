package ru.spbau.mit.circuit.ui.DrawableElements;

import android.graphics.Canvas;

import ru.spbau.mit.circuit.model.elements.Capacitor;
import ru.spbau.mit.circuit.model.node.Point;
import ru.spbau.mit.circuit.ui.DrawableNode;
import ru.spbau.mit.circuit.ui.Drawer;

import static ru.spbau.mit.circuit.ui.Drawer.CELL_SIZE;
import static ru.spbau.mit.circuit.ui.Drawer.NODE_RADIUS;
import static ru.spbau.mit.circuit.ui.Drawer.WIRE_PAINT;

public class DrawableCapacitor extends Capacitor implements Drawable {

    protected DrawableCapacitor(DrawableNode from, DrawableNode to) {
        super(from, to);
    }

    public DrawableCapacitor(Point center) {
        super(new DrawableNode(center.x() - 2 * Drawer.CELL_SIZE, center.y()),
                new DrawableNode(center.x() + 2 * Drawer.CELL_SIZE, center.y()));
    }

    @Override
    public void draw(Canvas canvas) {
        if (isVertical()) {
            canvas.drawLine(x(), y() + CELL_SIZE * 2, x(), y() + CELL_SIZE / 2,
                    Drawer.ELEMENTS_PAINT);
            canvas.drawLine(x(), y() - CELL_SIZE * 2, x(), y() - CELL_SIZE / 2,
                    Drawer.ELEMENTS_PAINT);
            canvas.drawLine(x() - Drawer.CELL_SIZE * 3 / 4, y() - Drawer.CELL_SIZE / 2,
                    x() + Drawer.CELL_SIZE * 3 / 4, y() - Drawer.CELL_SIZE / 2, Drawer.ELEMENTS_PAINT);
            canvas.drawLine(x() - Drawer.CELL_SIZE * 3 / 4, y() + Drawer.CELL_SIZE / 2,
                    x() + Drawer.CELL_SIZE * 3 / 4, y() + Drawer.CELL_SIZE / 2, Drawer.ELEMENTS_PAINT);
        } else {
            canvas.drawLine(x() - CELL_SIZE * 2, y(), x() - CELL_SIZE / 2, y(),
                    Drawer.ELEMENTS_PAINT);
            canvas.drawLine(x() + Drawer.CELL_SIZE * 2, y(), x() + Drawer.CELL_SIZE / 2, y(),
                    Drawer.ELEMENTS_PAINT);
            canvas.drawLine(x() - Drawer.CELL_SIZE / 2, y() - Drawer.CELL_SIZE * 3 / 4, x() - Drawer.CELL_SIZE
                    / 2, y() + Drawer.CELL_SIZE * 3 / 4, Drawer.ELEMENTS_PAINT);
            canvas.drawLine(x() + Drawer.CELL_SIZE / 2, y() - Drawer.CELL_SIZE * 3 / 4, x() + Drawer.CELL_SIZE
                    / 2, y() + Drawer.CELL_SIZE * 3 / 4, Drawer.ELEMENTS_PAINT);

        }
        canvas.drawCircle(from.x(), from.y(), NODE_RADIUS, WIRE_PAINT);
        canvas.drawCircle(to.x(), to.y(), NODE_RADIUS, WIRE_PAINT);
    }
}