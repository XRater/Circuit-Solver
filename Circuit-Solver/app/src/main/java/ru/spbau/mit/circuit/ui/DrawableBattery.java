package ru.spbau.mit.circuit.ui;

import ru.spbau.mit.circuit.model.elements.Battery;
import ru.spbau.mit.circuit.model.point.Point;


public class DrawableBattery extends Battery implements Drawable {
    private int x;
    private int y;

    protected DrawableBattery(Point from, Point to) {
        super(from, to);
    }

    public DrawableBattery(Point center) {
        super(new Point(center.x() - 2 * Drawer.cellSize, center.y()),
                new Point(center.x() + 2 * Drawer.cellSize, center.y()));
        x = center.x();
        y = center.y();
    }

    @Override
    public void draw(MyCanvas canvas) {
        canvas.drawLine(x - Drawer.cellSize * 2, y, x - Drawer.cellSize / 3, y, Drawer
                .elementsPaint);
        canvas.drawLine(x + Drawer.cellSize * 2, y, x + Drawer.cellSize / 3, y, Drawer
                .elementsPaint);
        canvas.drawLine(x - Drawer.cellSize / 3, y - Drawer.cellSize * 3 / 7, x - Drawer.cellSize
                / 3, y + Drawer.cellSize * 3 / 7, Drawer.elementsPaint);
        canvas.drawLine(x + Drawer.cellSize / 3, y - Drawer.cellSize * 3 / 4, x + Drawer.cellSize
                / 3, y + Drawer.cellSize * 3 / 4, Drawer.elementsPaint);
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
        this.setPosition(new Point(nx - 2 * Drawer.cellSize, ny), new Point(nx + 2 * Drawer
                .cellSize, ny));
    }
}

