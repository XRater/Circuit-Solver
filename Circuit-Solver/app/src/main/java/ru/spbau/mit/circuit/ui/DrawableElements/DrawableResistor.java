package ru.spbau.mit.circuit.ui.DrawableElements;

import android.graphics.Canvas;

import ru.spbau.mit.circuit.model.elements.Resistor;
import ru.spbau.mit.circuit.model.node.Point;
import ru.spbau.mit.circuit.ui.DrawableNode;
import ru.spbau.mit.circuit.ui.Drawer;


public class DrawableResistor extends Resistor implements Drawable {
    //private int x;
    //private int y;

    protected DrawableResistor(Point from, Point to) {
        super(new DrawableNode(from), new DrawableNode(to));
    }

    public DrawableResistor(Point center) {
        super(new DrawableNode(center.x() - 2 * Drawer.CELL_SIZE, center.y()),
                new DrawableNode(center.x() + 2 * Drawer.CELL_SIZE, center.y()));
        //x = center.x();
        //y = center.y();
    }

    @Override
    public void draw(Canvas canvas) {
        //up and down
        canvas.drawLine(x() - Drawer.CELL_SIZE, y() + Drawer.CELL_SIZE / 2, x() + Drawer.CELL_SIZE, y() +
                Drawer.CELL_SIZE / 2, Drawer.ELEMENTS_PAINT);
        canvas.drawLine(x() - Drawer.CELL_SIZE, y() - Drawer.CELL_SIZE / 2, x() + Drawer.CELL_SIZE, y() -
                Drawer.CELL_SIZE / 2, Drawer.ELEMENTS_PAINT);
        //left and right
        canvas.drawLine(x() - Drawer.CELL_SIZE, y() - Drawer.CELL_SIZE / 2, x() - Drawer.CELL_SIZE, y() +
                Drawer.CELL_SIZE / 2, Drawer.ELEMENTS_PAINT);
        canvas.drawLine(x() + Drawer.CELL_SIZE, y() - Drawer.CELL_SIZE / 2, x() + Drawer.CELL_SIZE, y() +
                Drawer.CELL_SIZE / 2, Drawer.ELEMENTS_PAINT);
        //wires
        canvas.drawLine(x() - Drawer.CELL_SIZE * 2, y(), x() - Drawer.CELL_SIZE, y(), Drawer.ELEMENTS_PAINT);
        canvas.drawLine(x() + Drawer.CELL_SIZE * 2, y(), x() + Drawer.CELL_SIZE, y(), Drawer.ELEMENTS_PAINT);

        // TODO vertical

        canvas.drawText((int) getResistance() + "\u03A9", x() - Drawer.CELL_SIZE / 4, y() + Drawer
                .CELL_SIZE / 4, Drawer.ELEMENTS_PAINT);
    }
}
