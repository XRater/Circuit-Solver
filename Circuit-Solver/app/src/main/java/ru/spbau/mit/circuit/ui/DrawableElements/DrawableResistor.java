package ru.spbau.mit.circuit.ui.DrawableElements;

import ru.spbau.mit.circuit.model.point.Point;
import ru.spbau.mit.circuit.ui.Drawer;
import ru.spbau.mit.circuit.ui.MyCanvas;


public class DrawableResistor extends FiledElement {
    private int x;
    private int y;

    protected DrawableResistor(Point from, Point to) {
        super(from, to);
    }

//    public DrawableResistor(Node node) {
//        super(new Point(node.point().x() - 2 * Drawer.CELL_SIZE, node.point().y()),
//                new Point(node.point().x() + 2 * Drawer.CELL_SIZE, node.point().y()));
//        x = node.point().x();
//        y = node.point().y();
//        this.node = node;
//    }

    public DrawableResistor(Point center) {
        super(new Point(center.x() - 2 * Drawer.CELL_SIZE, center.y()),
                new Point(center.x() + 2 * Drawer.CELL_SIZE, center.y()));
        x = center.x();
        y = center.y();
    }

    @Override
    public void draw(MyCanvas canvas) {
        //up and down
        canvas.drawLine(x - Drawer.CELL_SIZE, y + Drawer.CELL_SIZE / 2, x + Drawer.CELL_SIZE, y +
                Drawer.CELL_SIZE / 2, Drawer.elementsPaint);
        canvas.drawLine(x - Drawer.CELL_SIZE, y - Drawer.CELL_SIZE / 2, x + Drawer.CELL_SIZE, y -
                Drawer.CELL_SIZE / 2, Drawer.elementsPaint);
        //left and right
        canvas.drawLine(x - Drawer.CELL_SIZE, y - Drawer.CELL_SIZE / 2, x - Drawer.CELL_SIZE, y +
                Drawer.CELL_SIZE / 2, Drawer.elementsPaint);
        canvas.drawLine(x + Drawer.CELL_SIZE, y - Drawer.CELL_SIZE / 2, x + Drawer.CELL_SIZE, y +
                Drawer.CELL_SIZE / 2, Drawer.elementsPaint);
        //wires
        canvas.drawLine(x - Drawer.CELL_SIZE * 2, y, x - Drawer.CELL_SIZE, y, Drawer.elementsPaint);
        canvas.drawLine(x + Drawer.CELL_SIZE * 2, y, x + Drawer.CELL_SIZE, y, Drawer.elementsPaint);

        // TODO vertical

        canvas.drawText((int) getResistance() + "\u03A9", x - Drawer.CELL_SIZE / 4, y + Drawer
                .CELL_SIZE / 4, Drawer.elementsPaint);
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

//    @Override
//    public void updatePosition(int nx, int ny) {
//        x = nx;
//        y = ny;
//        this.setPosition(new Point(nx - 2 * Drawer.CELL_SIZE, ny), new Point(nx + 2 * Drawer
//                .CELL_SIZE, ny));
//    }
}
