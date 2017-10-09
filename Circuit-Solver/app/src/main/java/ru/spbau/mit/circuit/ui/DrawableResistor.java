package ru.spbau.mit.circuit.ui;

import android.graphics.Canvas;

import ru.spbau.mit.circuit.model.Resistor;


public class DrawableResistor extends Resistor implements Drawable {
    @Override
    public void draw(Canvas canvas) {
        //up and down
        canvas.drawLine(x - Drawer.cellSize, y + Drawer.cellSize / 2, x + Drawer.cellSize, y + Drawer.cellSize / 2, Drawer.elementsPaint);
        canvas.drawLine(x - Drawer.cellSize, y - Drawer.cellSize / 2, x + Drawer.cellSize, y - Drawer.cellSize / 2, Drawer.elementsPaint);
        //left and right
        canvas.drawLine(x - Drawer.cellSize, y - Drawer.cellSize / 2, x - Drawer.cellSize, y + Drawer.cellSize / 2, Drawer.elementsPaint);
        canvas.drawLine(x + Drawer.cellSize, y - Drawer.cellSize / 2, x + Drawer.cellSize, y + Drawer.cellSize / 2, Drawer.elementsPaint);
        //wires
        canvas.drawLine(x - Drawer.cellSize * 2, y, x - Drawer.cellSize, y, Drawer.elementsPaint);
        canvas.drawLine(x + Drawer.cellSize * 2, y, x + Drawer.cellSize, y, Drawer.elementsPaint);

        // TODO vertical
    }
}
