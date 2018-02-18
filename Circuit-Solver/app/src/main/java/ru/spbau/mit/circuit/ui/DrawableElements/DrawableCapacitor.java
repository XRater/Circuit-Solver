package ru.spbau.mit.circuit.ui.DrawableElements;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.NonNull;

import ru.spbau.mit.circuit.model.circuitObjects.elements.Capacitor;
import ru.spbau.mit.circuit.model.circuitObjects.nodes.Point;
import ru.spbau.mit.circuit.ui.DrawableNode;
import ru.spbau.mit.circuit.ui.Drawer;

import static ru.spbau.mit.circuit.ui.Drawer.CELL_SIZE;
import static ru.spbau.mit.circuit.ui.Drawer.NODE_RADIUS;
import static ru.spbau.mit.circuit.ui.Drawer.WIRE_PAINT;

public class DrawableCapacitor extends Capacitor implements Drawable {

    protected DrawableCapacitor(@NonNull DrawableNode from, @NonNull DrawableNode to) {
        super(from, to);
    }

    public DrawableCapacitor(@NonNull Point center) {
        super(new DrawableNode(center.x() - 2 * Drawer.CELL_SIZE, center.y()),
                new DrawableNode(center.x() + 2 * Drawer.CELL_SIZE, center.y()));
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
//        String capacity = String.format("%.2f", getCharacteristicValue()) + "F";
        String capacity = getCharacteristicValue().toString();
        Rect capacitySize = new Rect();
        Drawer.ELEMENTS_PAINT.getTextBounds(capacity, 0, capacity.length(), capacitySize);

        canvas.save();
        if (!isHorizontal()) {
            canvas.translate(x() + Drawer.getOffsetX(), y() + Drawer.getOffsetY());
            canvas.rotate(90);
            canvas.translate(-x() - Drawer.getOffsetX(), -y() - Drawer.getOffsetY());
        }

        canvas.drawLine(x() - CELL_SIZE * 2, y(), x() - CELL_SIZE / 4, y(),
                Drawer.ELEMENTS_PAINT);
        canvas.drawLine(x() + Drawer.CELL_SIZE * 2, y(), x() + Drawer.CELL_SIZE / 4, y(),
                Drawer.ELEMENTS_PAINT);
        canvas.drawLine(x() - Drawer.CELL_SIZE / 4, y() - Drawer.CELL_SIZE * 3 / 4,
                x() - Drawer.CELL_SIZE / 4, y() + Drawer.CELL_SIZE * 3 / 4, Drawer.ELEMENTS_PAINT);
        canvas.drawLine(x() + Drawer.CELL_SIZE / 4, y() - Drawer.CELL_SIZE * 3 / 4,
                x() + Drawer.CELL_SIZE / 4, y() + Drawer.CELL_SIZE * 3 / 4, Drawer.ELEMENTS_PAINT);

        canvas.drawText(capacity, x() - capacitySize.width() / 2, y() + CELL_SIZE / 4 * 5, Drawer
                .ELEMENTS_PAINT);

        canvas.restore();
        canvas.drawCircle(from.x(), from.y(), NODE_RADIUS, WIRE_PAINT);
        canvas.drawCircle(to.x(), to.y(), NODE_RADIUS, WIRE_PAINT);
    }
}