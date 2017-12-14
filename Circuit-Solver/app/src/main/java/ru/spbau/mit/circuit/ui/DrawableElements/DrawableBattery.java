package ru.spbau.mit.circuit.ui.DrawableElements;

import android.graphics.Canvas;

import ru.spbau.mit.circuit.model.elements.Battery;
import ru.spbau.mit.circuit.model.node.Point;
import ru.spbau.mit.circuit.ui.DrawableNode;
import ru.spbau.mit.circuit.ui.Drawer;

import static ru.spbau.mit.circuit.ui.Drawer.NODE_RADIUS;
import static ru.spbau.mit.circuit.ui.Drawer.WIRE_PAINT;


public class DrawableBattery extends Battery implements Drawable {

    protected DrawableBattery(DrawableNode from, DrawableNode to) {
        super(from, to);
    }

    public DrawableBattery(Point center) {
        super(new DrawableNode(center.x() - 2 * Drawer.CELL_SIZE, center.y()),
                new DrawableNode(center.x() + 2 * Drawer.CELL_SIZE, center.y()));
    }

    @Override
    public void draw(Canvas canvas) {
        if (isHorizontal()) {
            canvas.drawLine(x() - Drawer.CELL_SIZE * 2, y(), x() - Drawer.CELL_SIZE / 3, y(), Drawer
                    .ELEMENTS_PAINT);
            canvas.drawLine(x() + Drawer.CELL_SIZE * 2, y(), x() + Drawer.CELL_SIZE / 3, y(),
                    Drawer.ELEMENTS_PAINT);
            if (from().x() < to.x()) {
                canvas.drawLine(x() - Drawer.CELL_SIZE / 3, y() - Drawer.CELL_SIZE * 3 / 7, x() - Drawer
                        .CELL_SIZE / 3, y() + Drawer.CELL_SIZE * 3 / 7, Drawer.ELEMENTS_PAINT);
                canvas.drawLine(x() + Drawer.CELL_SIZE / 3, y() - Drawer.CELL_SIZE * 3 / 4, x() +
                        Drawer.CELL_SIZE / 3, y() + Drawer.CELL_SIZE * 3 / 4, Drawer.ELEMENTS_PAINT);
            } else {
                canvas.drawLine(x() + Drawer.CELL_SIZE / 3, y() - Drawer.CELL_SIZE * 3 / 7, x() + Drawer
                        .CELL_SIZE / 3, y() + Drawer.CELL_SIZE * 3 / 7, Drawer.ELEMENTS_PAINT);
                canvas.drawLine(x() - Drawer.CELL_SIZE / 3, y() - Drawer.CELL_SIZE * 3 / 4, x() -
                        Drawer.CELL_SIZE / 3, y() + Drawer.CELL_SIZE * 3 / 4, Drawer.ELEMENTS_PAINT);
            }
        } else {
            canvas.drawLine(x(), y() - 2 * Drawer.CELL_SIZE, x(), y() - Drawer.CELL_SIZE / 3, Drawer
                    .ELEMENTS_PAINT);
            canvas.drawLine(x(), y() + 2 * Drawer.CELL_SIZE, x(), y() + Drawer.CELL_SIZE / 3, Drawer
                    .ELEMENTS_PAINT);
            if (from().y() < to.y()) {
                canvas.drawLine(x() - Drawer.CELL_SIZE * 3 / 7, y() - Drawer.CELL_SIZE / 3, x() + Drawer
                        .CELL_SIZE * 3 / 7, y() - Drawer.CELL_SIZE / 3, Drawer.ELEMENTS_PAINT);
                canvas.drawLine(x() - Drawer.CELL_SIZE * 3 / 4, y() + Drawer.CELL_SIZE / 3, x() + Drawer
                        .CELL_SIZE * 3 / 4, y() + Drawer.CELL_SIZE / 3, Drawer.ELEMENTS_PAINT);
            } else {
                canvas.drawLine(x() - Drawer.CELL_SIZE * 3 / 7, y() + Drawer.CELL_SIZE / 3, x() + Drawer
                        .CELL_SIZE * 3 / 7, y() + Drawer.CELL_SIZE / 3, Drawer.ELEMENTS_PAINT);
                canvas.drawLine(x() - Drawer.CELL_SIZE * 3 / 4, y() - Drawer.CELL_SIZE / 3, x() + Drawer
                        .CELL_SIZE * 3 / 4, y() - Drawer.CELL_SIZE / 3, Drawer.ELEMENTS_PAINT);
            }
        }
        canvas.drawCircle(from.x(), from.y(), NODE_RADIUS, WIRE_PAINT);
        canvas.drawCircle(to.x(), to.y(), NODE_RADIUS, WIRE_PAINT);
    }
}

