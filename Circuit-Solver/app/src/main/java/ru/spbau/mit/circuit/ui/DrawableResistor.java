package ru.spbau.mit.circuit.ui;

import ru.spbau.mit.circuit.model.Point;
import ru.spbau.mit.circuit.model.Resistor;


public class DrawableResistor extends Resistor implements Drawable {
    private int x;
    private int y;

    protected DrawableResistor(Point from, Point to) {
        super(from, to);
    }

    public DrawableResistor(Point center) {
        super(new Point(center.x() - 2 * Drawer.cellSize, center.y()),
                new Point(center.x() + 2 * Drawer.cellSize, center.y()));
        x = center.x();
        y = center.y();
    }

    @Override
    public void draw(MyCanvas canvas) {
        //up and down
        canvas.drawLine(x - Drawer.cellSize, y + Drawer.cellSize / 2, x + Drawer.cellSize, y +
                Drawer.cellSize / 2, Drawer.elementsPaint);
        canvas.drawLine(x - Drawer.cellSize, y - Drawer.cellSize / 2, x + Drawer.cellSize, y -
                Drawer.cellSize / 2, Drawer.elementsPaint);
        //left and right
        canvas.drawLine(x - Drawer.cellSize, y - Drawer.cellSize / 2, x - Drawer.cellSize, y +
                Drawer.cellSize / 2, Drawer.elementsPaint);
        canvas.drawLine(x + Drawer.cellSize, y - Drawer.cellSize / 2, x + Drawer.cellSize, y +
                Drawer.cellSize / 2, Drawer.elementsPaint);
        //wires
        canvas.drawLine(x - Drawer.cellSize * 2, y, x - Drawer.cellSize, y, Drawer.elementsPaint);
        canvas.drawLine(x + Drawer.cellSize * 2, y, x + Drawer.cellSize, y, Drawer.elementsPaint);

        // TODO vertical

        canvas.drawText(getResistance() + "Om", x, y, Drawer.elementsPaint);
    }

    @Override
    public int x() {
        return x;
    }

    @Override
    public int y() {
        return y;
    }

//    public void setX(int x) {
//        this.x = x;
//    }
//
//    public void setY(int y) {
//        this.y = y;
//    }

    @Override
    public void updatePosition(int nx, int ny) {
        x = nx;
        y = ny;
        this.setPosition(new Point(nx - 2 * Drawer.cellSize, ny), new Point(nx + 2 * Drawer
                .cellSize, ny));
    }
}
