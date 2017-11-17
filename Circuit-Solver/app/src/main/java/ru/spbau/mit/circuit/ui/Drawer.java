package ru.spbau.mit.circuit.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import ru.spbau.mit.circuit.model.elements.Element;
import ru.spbau.mit.circuit.model.node.Point;
import ru.spbau.mit.circuit.ui.DrawableElements.Drawable;
import ru.spbau.mit.circuit.ui.DrawableElements.DrawableWire;


public class Drawer {
    public static final int CELL_SIZE = 80;
    public static final int FIELD_SIZE = 30;

    public static final Paint elementsPaint = new Paint();
    public static final Paint highlightPaint = new Paint();

    public static int offsetX = 0;
    public static int offsetY = 0;

    private SurfaceHolder surfaceHolder;
    private MyCanvas canvas;

    {
        elementsPaint.setColor(Color.rgb(0, 119, 179));
        elementsPaint.setStrokeWidth(7.5f);
        elementsPaint.setTextSize(50);

        highlightPaint.setColor(Color.rgb(0, 102, 153));
        highlightPaint.setStrokeWidth(6);
    }

    public Drawer(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
    }

    public static int round(float fx) {
        int x = Math.round(fx);
        int offset = x % Drawer.CELL_SIZE;
        x -= offset;
        return offset < (Drawer.CELL_SIZE / 2) ? x : x + Drawer.CELL_SIZE;
    }

    public static Point round(Point p) {
        return new Point(round(p.x()), round(p.y()));
    }

    public void drawBackground() {
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

    public void drawModel(Model model) {
        //ArrayList<Element> elements = controller.elements();
        Canvas simpleCanvas = surfaceHolder.lockCanvas();
        canvas = new MyCanvas(simpleCanvas);
        drawBackground();
        for (Drawable element : model.drawables) {
            element.draw(canvas);
        }
        for (DrawableWire wire : model.wires) {
            wire.draw(canvas);
        }
        if (model.highlighted != null) {
            canvas.drawCircle(model.highlighted.x(), model.highlighted.y(), CELL_SIZE / 5, highlightPaint);
        }
        surfaceHolder.unlockCanvasAndPost(simpleCanvas);
    }

    public void showCurrents(Model model) {
        Canvas canvas = surfaceHolder.lockCanvas();
        this.canvas = new MyCanvas(canvas);
        for (Drawable d : model.drawables) {
            Element e = (Element) d;
            String current = String.format("%.2f", Math.abs(e.getCurrent()));
            canvas.drawText(current + "A", d.x() - CELL_SIZE / 4, d.y() -
                            CELL_SIZE / 3 * 2,
                    elementsPaint);
        }
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

}
