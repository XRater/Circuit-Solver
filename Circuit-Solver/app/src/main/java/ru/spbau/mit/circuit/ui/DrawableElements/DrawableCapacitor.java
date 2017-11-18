package ru.spbau.mit.circuit.ui.DrawableElements;

import android.graphics.Canvas;

import ru.spbau.mit.circuit.model.elements.Capacitor;
import ru.spbau.mit.circuit.model.node.Point;
import ru.spbau.mit.circuit.ui.DrawableNode;
import ru.spbau.mit.circuit.ui.Drawer;


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
        canvas.drawLine(x() - Drawer.CELL_SIZE * 2, y(), x() - Drawer.CELL_SIZE / 2, y(), Drawer
                .ELEMENTS_PAINT);
        canvas.drawLine(x() + Drawer.CELL_SIZE * 2, y(), x() + Drawer.CELL_SIZE / 2, y(), Drawer
                .ELEMENTS_PAINT);
        canvas.drawLine(x() - Drawer.CELL_SIZE / 2, y() - Drawer.CELL_SIZE * 3 / 4, x() - Drawer.CELL_SIZE
                / 2, y() + Drawer.CELL_SIZE * 3 / 4, Drawer.ELEMENTS_PAINT);
        canvas.drawLine(x() + Drawer.CELL_SIZE / 2, y() - Drawer.CELL_SIZE * 3 / 4, x() + Drawer.CELL_SIZE
                / 2, y() + Drawer.CELL_SIZE * 3 / 4, Drawer.ELEMENTS_PAINT);
    }

//    @Override
//    public void updatePosition(int nx, int ny) {
//        x = nx;
//        y = ny;
//        this.setPosition(new Point(nx - 2 * Drawer.CELL_SIZE, ny), new Point(nx + 2 * Drawer
//                .CELL_SIZE, ny));
//    }
}
