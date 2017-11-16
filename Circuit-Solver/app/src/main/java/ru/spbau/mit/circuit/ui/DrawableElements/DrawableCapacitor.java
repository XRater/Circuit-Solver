package ru.spbau.mit.circuit.ui.DrawableElements;

import ru.spbau.mit.circuit.model.elements.Capacitor;
import ru.spbau.mit.circuit.model.node.Point;
import ru.spbau.mit.circuit.ui.Drawer;
import ru.spbau.mit.circuit.ui.MyCanvas;


public class DrawableCapacitor extends Capacitor implements Drawable {

    protected DrawableCapacitor(Point from, Point to) {
        super(from, to);
    }

    public DrawableCapacitor(Point center) {
        super(new Point(center.x() - 2 * Drawer.CELL_SIZE, center.y()),
                new Point(center.x() + 2 * Drawer.CELL_SIZE, center.y()));
    }

    @Override
    public void draw(MyCanvas canvas) {
        canvas.drawLine(x() - Drawer.CELL_SIZE * 2, y(), x() - Drawer.CELL_SIZE / 2, y(), Drawer
                .elementsPaint);
        canvas.drawLine(x() + Drawer.CELL_SIZE * 2, y(), x() + Drawer.CELL_SIZE / 2, y(), Drawer
                .elementsPaint);
        canvas.drawLine(x() - Drawer.CELL_SIZE / 2, y() - Drawer.CELL_SIZE * 3 / 4, x() - Drawer.CELL_SIZE
                / 2, y() + Drawer.CELL_SIZE * 3 / 4, Drawer.elementsPaint);
        canvas.drawLine(x() + Drawer.CELL_SIZE / 2, y() - Drawer.CELL_SIZE * 3 / 4, x() + Drawer.CELL_SIZE
                / 2, y() + Drawer.CELL_SIZE * 3 / 4, Drawer.elementsPaint);
    }

//    @Override
//    public void updatePosition(int nx, int ny) {
//        x = nx;
//        y = ny;
//        this.setPosition(new Point(nx - 2 * Drawer.CELL_SIZE, ny), new Point(nx + 2 * Drawer
//                .CELL_SIZE, ny));
//    }
}
