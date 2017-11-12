package ru.spbau.mit.circuit.ui;

import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

public class Drawer {
    public static ArrayList<Drawable> drawables = new ArrayList<>();
    public static int cellSize = 75;
    public static Paint elementsPaint;
    public static Paint hightligthPaint;
    public static int highlighted;
    public static int offsetX = 300, offsetY = 300;
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
        for (int i = -25 * cellSize; i <= 25 * cellSize; i += cellSize) {
            canvas.drawLine(-25 * cellSize, i, 25 * cellSize, i, paint);
        }
        for (int i = -25 * cellSize; i <= 25 * cellSize; i += cellSize) {
            canvas.drawLine(i, -25 * cellSize, i, 25 * cellSize, paint);
        }
    }

    public static void drawEverything(MyCanvas canvas) {
        //ArrayList<Element> elements = controller.getElements();

        drawBackground(canvas);
        for (Drawable element : drawables) {
            element.draw(canvas, HighlightedWire.NO);
        }
    }

}
