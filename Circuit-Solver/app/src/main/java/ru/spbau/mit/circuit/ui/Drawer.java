package ru.spbau.mit.circuit.ui;

import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

import ru.spbau.mit.circuit.model.elements.Element;
import ru.spbau.mit.circuit.model.point.Point;
import ru.spbau.mit.circuit.ui.DrawableElements.Drawable;
import ru.spbau.mit.circuit.ui.DrawableElements.DrawableWire;

public class Drawer {
    public static final int CELL_SIZE = 80;
    public static final int FIELD_SIZE = 30;

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
        elementsPaint.setTextSize(50);

        highlightPaint.setColor(Color.rgb(0, 102, 153));
        highlightPaint.setStrokeWidth(6);
    }

    public static void drawBackground(MyCanvas canvas) {
        canvas.drawColor(Color.rgb(218, 195, 148));
        Paint paint = new Paint();
        paint.setColor(Color.rgb(239, 236, 174));
        paint.setStrokeWidth(1);
        for (int i = 0; i <= FIELD_SIZE * CELL_SIZE; i += CELL_SIZE) {
            canvas.drawLine(0, i, FIELD_SIZE * CELL_SIZE, i, paint);
        }
        for (int i = 0; i <= FIELD_SIZE * CELL_SIZE; i += CELL_SIZE) {
            canvas.drawLine(i, 0, i, FIELD_SIZE * CELL_SIZE, paint);
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
        if (highlighted != null) {
            canvas.drawCircle(highlighted.x(), highlighted.y(), CELL_SIZE / 5, highlightPaint);
        }

    }

    public static void showCurrents(MyCanvas canvas) {
        for (Drawable d : drawables) {
            Element e = (Element) d;
            canvas.drawText(Math.abs(e.getCurrent()) + "A", d.x() - CELL_SIZE / 4, d.y() -
                            CELL_SIZE / 2,
                    elementsPaint);
        }
    }

    static int round(int x) {
        int offset = x % Drawer.CELL_SIZE;
        x -= offset;
        return offset < (Drawer.CELL_SIZE / 2) ? x : x + Drawer.CELL_SIZE;
    }
}
