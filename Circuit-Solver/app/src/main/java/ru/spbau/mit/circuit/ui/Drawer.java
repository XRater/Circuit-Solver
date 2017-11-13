package ru.spbau.mit.circuit.ui;

import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

import ru.spbau.mit.circuit.model.Point;

public class Drawer {
    public static final int cellSize = 100;
    public static final int fieldSize = 30;
    public static ArrayList<Drawable> drawables = new ArrayList<>();
    public static ArrayList<DrawableWire> wires = new ArrayList<>();
    public static Paint elementsPaint;
    public static Paint hightligthPaint;
    public static Point highlighted = new Point(300, 300);
    public static int offsetX = 0;
    public static int offsetY = 0; //fieldSize * cellSize / 2;
    static {
        elementsPaint = new Paint();
        elementsPaint.setColor(Color.RED);
        elementsPaint.setStrokeWidth(5);

        hightligthPaint = new Paint();
        hightligthPaint.setColor(Color.YELLOW);
        hightligthPaint.setStrokeWidth(5);
    }

    public static void drawBackground(MyCanvas canvas) {
        canvas.drawColor(Color.BLACK);
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        for (int i = 0; i <= fieldSize * cellSize; i += cellSize) {
            canvas.drawLine(0, i, fieldSize * cellSize, i, paint);
        }
        for (int i = 0; i <= fieldSize * cellSize; i += cellSize) {
            canvas.drawLine(i, 0, i, fieldSize * cellSize, paint);
        }
    }

    public static void drawEverything(MyCanvas canvas) {
        //ArrayList<Element> elements = controller.getElements();

        drawBackground(canvas);
        for (Drawable element : drawables) {
            element.draw(canvas);
        }
        for (DrawableWire wire : wires) {
            wire.draw(canvas);
        }
        if (highlighted != null)
            canvas.drawCircle(highlighted.x(), highlighted.y(), cellSize / 5, hightligthPaint);

    }

}
