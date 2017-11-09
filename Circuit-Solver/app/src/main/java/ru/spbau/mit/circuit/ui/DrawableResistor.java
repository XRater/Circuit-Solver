package ru.spbau.mit.circuit.ui;

import ru.spbau.mit.circuit.model.Resistor;



public class DrawableResistor extends Resistor implements Drawable {

    @Override
    public void draw(MyCanvas canvas, HighlightedWire side) {
        //up and down
        canvas.drawLine(x - Drawer.cellSize, y + Drawer.cellSize / 2, x + Drawer.cellSize, y + Drawer.cellSize / 2, Drawer.elementsPaint);
        canvas.drawLine(x - Drawer.cellSize, y - Drawer.cellSize / 2, x + Drawer.cellSize, y - Drawer.cellSize / 2, Drawer.elementsPaint);
        //left and right
        canvas.drawLine(x - Drawer.cellSize, y - Drawer.cellSize / 2, x - Drawer.cellSize, y + Drawer.cellSize / 2, Drawer.elementsPaint);
        canvas.drawLine(x + Drawer.cellSize, y - Drawer.cellSize / 2, x + Drawer.cellSize, y + Drawer.cellSize / 2, Drawer.elementsPaint);
        //wires
        if (side == HighlightedWire.LEFT) {
            canvas.drawLine(x - Drawer.cellSize * 2, y, x - Drawer.cellSize, y, Drawer.hightligthPaint);
        } else {
            canvas.drawLine(x - Drawer.cellSize * 2, y, x - Drawer.cellSize, y, Drawer.elementsPaint);
        }
        if (side == HighlightedWire.RIGHT) {
            canvas.drawLine(x + Drawer.cellSize * 2, y, x + Drawer.cellSize, y, Drawer.hightligthPaint);
        } else {
            canvas.drawLine(x + Drawer.cellSize * 2, y, x + Drawer.cellSize, y, Drawer.elementsPaint);
        }
        // TODO vertical
    }
}
