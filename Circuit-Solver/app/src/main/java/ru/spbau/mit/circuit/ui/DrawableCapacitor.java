package ru.spbau.mit.circuit.ui;

import ru.spbau.mit.circuit.model.Capacitor;
import ru.spbau.mit.circuit.model.Point;


public class DrawableCapacitor extends Capacitor implements Drawable {
    private int x;
    private int y;

    protected DrawableCapacitor(Point from, Point to) {
        super(from, to);
    }

    @Override
    public void draw(MyCanvas canvas) {
//        canvas.drawLine(x - Drawer.cellSize * 2, y, x - Drawer.cellSize / 2, y, Drawer.elementsPaint);
//        canvas.drawLine(x + Drawer.cellSize * 2, y, x + Drawer.cellSize / 2, y, Drawer.elementsPaint);
//        canvas.drawLine(x - Drawer.cellSize / 2, y - Drawer.cellSize * 3 / 4, x - Drawer.cellSize / 2, y + Drawer.cellSize * 3 / 4, Drawer.elementsPaint);
//        canvas.drawLine(x + Drawer.cellSize / 2, y - Drawer.cellSize * 3 / 4, x + Drawer.cellSize / 2, y + Drawer.cellSize * 3 / 4, Drawer.elementsPaint);
    }

    @Override
    public int x() {
        return 0;
    }

    @Override
    public int y() {
        return 0;
    }

    @Override
    public void setX(int x) {

    }

    @Override
    public void setY(int y) {

    }

    @Override
    public void updatePosition(int nx, int ny) {
        this.setPosition(new Point(nx - 2 * Drawer.cellSize, ny), new Point(nx + 2 * Drawer.cellSize, ny));
    }

    @Override
    public Point getPoint() {
        return new Point(x, y);
    }
}
