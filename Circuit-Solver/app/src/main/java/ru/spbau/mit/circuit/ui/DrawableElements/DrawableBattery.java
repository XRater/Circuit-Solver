package ru.spbau.mit.circuit.ui.DrawableElements;

import android.graphics.Canvas;

import ru.spbau.mit.circuit.model.elements.Battery;
import ru.spbau.mit.circuit.model.node.Point;
import ru.spbau.mit.circuit.ui.DrawableNode;
import ru.spbau.mit.circuit.ui.Drawer;


public class DrawableBattery extends Battery implements Drawable {
//    private int x;
//    private int y;

    protected DrawableBattery(DrawableNode from, DrawableNode to) {
        super(from, to);
    }

    public DrawableBattery(Point center) {
//        super(center);
        super(new DrawableNode(center.x() - 2 * Drawer.CELL_SIZE, center.y()),
                new DrawableNode(center.x() + 2 * Drawer.CELL_SIZE, center.y()));
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawLine(x() - Drawer.CELL_SIZE * 2, y(), x() - Drawer.CELL_SIZE / 3, y(), Drawer
                .ELEMENTS_PAINT);
        canvas.drawLine(x() + Drawer.CELL_SIZE * 2, y(), x() + Drawer.CELL_SIZE / 3, y(),
                Drawer
                        .ELEMENTS_PAINT);
        canvas.drawLine(x() - Drawer.CELL_SIZE / 3, y() - Drawer.CELL_SIZE * 3 / 7, x() - Drawer
                .CELL_SIZE
                / 3, y() + Drawer.CELL_SIZE * 3 / 7, Drawer.ELEMENTS_PAINT);
        canvas.drawLine(x() + Drawer.CELL_SIZE / 3, y() - Drawer.CELL_SIZE * 3 / 4, x() +
                Drawer.CELL_SIZE
                        / 3, y() + Drawer.CELL_SIZE * 3 / 4, Drawer.ELEMENTS_PAINT);
    }


//  Use replace(Point p) instead

//    @Override
//    public void updatePosition(int nx, int ny) {
//        x = nx;
//        y = ny;
//        this.setPosition(new Point(nx - 2 * Drawer.CELL_SIZE, ny), new Point(nx + 2 * Drawer
//                .CELL_SIZE, ny));
//        replace(new Point(nx, ny));
//    }
}

