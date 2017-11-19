package ru.spbau.mit.circuit.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.view.SurfaceHolder;

import ru.spbau.mit.circuit.model.elements.Element;
import ru.spbau.mit.circuit.model.node.Point;
import ru.spbau.mit.circuit.ui.DrawableElements.Drawable;
import ru.spbau.mit.circuit.ui.DrawableElements.DrawableWire;


public class Drawer {
    public static final int CELL_SIZE = 80;
    public static final int FIELD_SIZE = 30;

    public static final Paint ELEMENTS_PAINT = new Paint();
    public static final Paint HIGHLIGHT_PAINT = new Paint();


    private static int offsetX = 0;
    private static int offsetY = 0;

    static {
        ELEMENTS_PAINT.setColor(Color.rgb(0, 119, 179));
        ELEMENTS_PAINT.setStrokeWidth(7.5f);
        ELEMENTS_PAINT.setTextSize(50);

        HIGHLIGHT_PAINT.setColor(Color.rgb(0, 102, 153));
        HIGHLIGHT_PAINT.setStrokeWidth(6);
    }

    private SurfaceHolder surfaceHolder;
    private MyCanvas canvas;

    public Drawer(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
    }

    public static int getOffsetX() {
        return offsetX;
    }

    public static void setOffsetX(int offsetX) {
        Drawer.offsetX = offsetX;
    }

    public static int getOffsetY() {
        return offsetY;
    }

    public static void setOffsetY(int offsetY) {
        Drawer.offsetY = offsetY;
    }

    private static int round(float fx) {
        int x = Math.round(fx);
        int offset = x % Drawer.CELL_SIZE;
        x -= offset;
        return offset < (Drawer.CELL_SIZE / 2) ? x : x + Drawer.CELL_SIZE;
    }

    public static Point round(Point p) {
        return new Point(round(p.x() - offsetX), round(p.y() - offsetY));
    }

    private void drawBackground() {
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

    public void drawModel(DrawableModel drawableModel) {
//        ArrayList<Element> elements = controller.elements();
        Canvas simpleCanvas = surfaceHolder.lockCanvas();
        canvas = new MyCanvas(simpleCanvas);
        drawBackground();
        for (Drawable element : drawableModel.drawables()) {
            element.draw(canvas);
        }
        for (DrawableWire wire : drawableModel.wires()) {
            wire.draw(canvas);
        }
        if (drawableModel.isShowingCurrents()) {
            showCurrents(drawableModel);
        }
//        if (drawableModel.highlighted != null) {
//            canvas.drawCircle(drawableModel.highlighted.x(), drawableModel.highlighted.y(), CELL_SIZE / 5, HIGHLIGHT_PAINT);
//        }
        surfaceHolder.unlockCanvasAndPost(simpleCanvas);
    }

    private void showCurrents(DrawableModel drawableModel) {
        for (Drawable d : drawableModel.drawables()) {
            Element e = (Element) d;
            String current = String.format("%.2f", Math.abs(e.getCurrent()));
            canvas.drawText(current + "A", e.x() - CELL_SIZE / 4, e.y() -
                            CELL_SIZE / 3 * 2,
                    ELEMENTS_PAINT);
        }
    }

    private class MyCanvas extends Canvas {
        private Canvas canvas;

        MyCanvas(Canvas canvas) {
            this.canvas = canvas;
        }

        public void drawLine(float startX, float startY, float stopX, float stopY, @NonNull Paint
                paint) {
            canvas.drawLine(startX + Drawer.offsetX, startY + Drawer.offsetY, stopX + Drawer.offsetX,
                    stopY + Drawer.offsetY, paint);
        }

        public void drawColor(int color) {
            canvas.drawColor(color);
        }

        public void drawCircle(float cx, float cy, float radius, @NonNull Paint paint) {
            canvas.drawCircle(cx + Drawer.offsetX, cy + Drawer.offsetY, radius, paint);
        }

        public void drawText(String text, float x, float y, @NonNull Paint paint) {
            canvas.drawText(text, x + Drawer.offsetX, y + Drawer.offsetY, paint);
        }
    }
}
