package ru.spbau.mit.circuit.ui.DrawableElements;

import android.graphics.Canvas;

import ru.spbau.mit.circuit.model.elements.Resistor;
import ru.spbau.mit.circuit.model.node.Point;
import ru.spbau.mit.circuit.ui.DrawableNode;
import ru.spbau.mit.circuit.ui.Drawer;

import static ru.spbau.mit.circuit.ui.Drawer.CELL_SIZE;
import static ru.spbau.mit.circuit.ui.Drawer.NODE_RADIUS;
import static ru.spbau.mit.circuit.ui.Drawer.WIRE_PAINT;

public class DrawableResistor extends Resistor implements Drawable {

    protected DrawableResistor(Point from, Point to) {
        super(new DrawableNode(from), new DrawableNode(to));
    }

    public DrawableResistor(Point center) {
        super(new DrawableNode(center.x() - 2 * CELL_SIZE, center.y()),
                new DrawableNode(center.x() + 2 * CELL_SIZE, center.y()));
    }

    @Override
    public void draw(Canvas canvas) {
        if (this.isHorizontal()) {
            //up and down
            canvas.drawLine(x() - CELL_SIZE, y() + CELL_SIZE / 2, x() + CELL_SIZE, y() +
                    CELL_SIZE / 2, Drawer.ELEMENTS_PAINT);
            canvas.drawLine(x() - CELL_SIZE, y() - CELL_SIZE / 2, x() + CELL_SIZE, y() -
                    CELL_SIZE / 2, Drawer.ELEMENTS_PAINT);
            //left and right
            canvas.drawLine(x() - CELL_SIZE, y() - CELL_SIZE / 2, x() - CELL_SIZE, y() +
                    CELL_SIZE / 2, Drawer.ELEMENTS_PAINT);
            canvas.drawLine(x() + CELL_SIZE, y() - CELL_SIZE / 2, x() + CELL_SIZE, y() +
                    CELL_SIZE / 2, Drawer.ELEMENTS_PAINT);
            //wires
            canvas.drawLine(x() - CELL_SIZE * 2, y(), x() - CELL_SIZE, y(), Drawer.ELEMENTS_PAINT);
            canvas.drawLine(x() + CELL_SIZE * 2, y(), x() + CELL_SIZE, y(), Drawer.ELEMENTS_PAINT);

            canvas.drawText((int) getResistance() + "\u03A9", x() - CELL_SIZE / 4, y() +
                    CELL_SIZE / 4, Drawer.ELEMENTS_PAINT);

        } else {
            //left and right
            canvas.drawLine(x() - CELL_SIZE / 2, y() + CELL_SIZE, x() - CELL_SIZE / 2, y() -
                    CELL_SIZE, Drawer.ELEMENTS_PAINT);
            canvas.drawLine(x() + CELL_SIZE / 2, y() + CELL_SIZE, x() + CELL_SIZE / 2, y() -
                    CELL_SIZE, Drawer.ELEMENTS_PAINT);
            //up and down
            canvas.drawLine(x() - CELL_SIZE / 2, y() + CELL_SIZE, x() + CELL_SIZE / 2, y() +
                    CELL_SIZE, Drawer.ELEMENTS_PAINT);
            canvas.drawLine(x() - CELL_SIZE / 2, y() - CELL_SIZE, x() + CELL_SIZE / 2, y() -
                    CELL_SIZE, Drawer.ELEMENTS_PAINT);
            //wires
            canvas.drawLine(x(), y() - CELL_SIZE * 2, x(), y() - CELL_SIZE, Drawer.ELEMENTS_PAINT);
            canvas.drawLine(x(), y() + CELL_SIZE * 2, x(), y() + CELL_SIZE, Drawer.ELEMENTS_PAINT);

            canvas.drawText((int) getResistance() + "\u03A9", x() - CELL_SIZE / 4, y() +
                    CELL_SIZE / 4, Drawer.ELEMENTS_PAINT);
        }
        canvas.drawCircle(from.x(), from.y(), NODE_RADIUS, WIRE_PAINT);
        canvas.drawCircle(to.x(), to.y(), NODE_RADIUS, WIRE_PAINT);
    }
}
