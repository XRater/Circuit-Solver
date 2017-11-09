package ru.spbau.mit.circuit.ui;

import ru.spbau.mit.circuit.model.Capacitor;

import static ru.spbau.mit.circuit.ui.Highlighted.NO;


public class DrawableCapacitor extends Capacitor implements Drawable {
    public Highlighted highlighted = NO;

    @Override
    public void draw(MyCanvas canvas) {
        canvas.drawLine(x - Drawer.cellSize * 2, y, x - Drawer.cellSize / 2, y, Drawer.elementsPaint);
        canvas.drawLine(x + Drawer.cellSize * 2, y, x + Drawer.cellSize / 2, y, Drawer.elementsPaint);
        canvas.drawLine(x - Drawer.cellSize / 2, y - Drawer.cellSize * 3 / 4, x - Drawer.cellSize / 2, y + Drawer.cellSize * 3 / 4, Drawer.elementsPaint);
        canvas.drawLine(x + Drawer.cellSize / 2, y - Drawer.cellSize * 3 / 4, x + Drawer.cellSize / 2, y + Drawer.cellSize * 3 / 4, Drawer.elementsPaint);
    }

}
