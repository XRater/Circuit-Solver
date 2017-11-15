package ru.spbau.mit.circuit.ui.DrawableElements;

import ru.spbau.mit.circuit.model.elements.Battery;
import ru.spbau.mit.circuit.model.point.Point;
import ru.spbau.mit.circuit.ui.Drawer;
import ru.spbau.mit.circuit.ui.MyCanvas;


public class DrawableBattery extends Battery implements Drawable {
    private int x;
    private int y;

    protected DrawableBattery(Point from, Point to) {
        super(from, to);
    }

    public DrawableBattery(Point center) {
        super(new Point(center.x() - 2 * Drawer.CELL_SIZE, center.y()),
                new Point(center.x() + 2 * Drawer.CELL_SIZE, center.y()));
        x = center.x();
        y = center.y();
    }

    @Override
    public void draw(MyCanvas canvas) {
        canvas.drawLine(x - Drawer.CELL_SIZE * 2, y, x - Drawer.CELL_SIZE / 3, y, Drawer
                .elementsPaint);
        canvas.drawLine(x + Drawer.CELL_SIZE * 2, y, x + Drawer.CELL_SIZE / 3, y, Drawer
                .elementsPaint);
        canvas.drawLine(x - Drawer.CELL_SIZE / 3, y - Drawer.CELL_SIZE * 3 / 7, x - Drawer.CELL_SIZE
                / 3, y + Drawer.CELL_SIZE * 3 / 7, Drawer.elementsPaint);
        canvas.drawLine(x + Drawer.CELL_SIZE / 3, y - Drawer.CELL_SIZE * 3 / 4, x + Drawer.CELL_SIZE
                / 3, y + Drawer.CELL_SIZE * 3 / 4, Drawer.elementsPaint);
    }

    @Override
    public int x() {
        return x;
    }

    @Override
    public int y() {
        return y;
    }

    @Override
    public void updatePosition(int nx, int ny) {
        x = nx;
        y = ny;
        this.setPosition(new Point(nx - 2 * Drawer.CELL_SIZE, ny), new Point(nx + 2 * Drawer
                .CELL_SIZE, ny));
    }
}

