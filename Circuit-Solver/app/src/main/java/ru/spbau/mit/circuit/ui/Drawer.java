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

    public static Paint elementsPaint = new Paint();
    public static Paint highlightPaint = new Paint();

    public static Point highlighted;

    public static int offsetX = 0;
    public static int offsetY = 0;

    static {
        elementsPaint.setColor(Color.rgb(0, 119, 179));
        elementsPaint.setStrokeWidth(7.5f);

        highlightPaint.setColor(Color.rgb(0, 102, 153));
        highlightPaint.setStrokeWidth(6.02f);
    }

    public static void drawBackground(MyCanvas canvas) {
        canvas.drawColor(Color.rgb(218, 195, 148));
        Paint paint = new Paint();
        paint.setColor(Color.rgb(239, 236, 174));
        paint.setStrokeWidth(1);
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
            canvas.drawCircle(highlighted.x(), highlighted.y(), cellSize / 5, highlightPaint);

        canvas.drawText("hello", 400, 400, highlightPaint);
    }

}
