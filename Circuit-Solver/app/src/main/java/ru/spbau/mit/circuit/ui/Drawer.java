package ru.spbau.mit.circuit.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.SurfaceHolder;

import ru.spbau.mit.circuit.model.circuitObjects.elements.Element;
import ru.spbau.mit.circuit.model.circuitObjects.nodes.Point;
import ru.spbau.mit.circuit.ui.DrawableElements.Drawable;
import ru.spbau.mit.circuit.ui.DrawableElements.DrawableWire;

public class Drawer {
    public static final int CELL_SIZE = 70;
    public static final int FIELD_SIZE = 25;
    public static final int NODE_RADIUS = CELL_SIZE / 7;

    public static final Paint ELEMENTS_PAINT = new Paint();
    public static final Paint WIRE_PAINT = new Paint();
    private static final int ELEMENTS_COLOR = Color.rgb(0, 119, 179);
    private static int offsetX = 0;
    private static int offsetY = 0;

    static {
        ELEMENTS_PAINT.setColor(ELEMENTS_COLOR);
        ELEMENTS_PAINT.setStrokeWidth(7.5f);
        ELEMENTS_PAINT.setTextSize(45);

        WIRE_PAINT.setColor(Color.rgb(0, 102, 153));
        WIRE_PAINT.setStrokeWidth(6);
    }

    private SurfaceHolder surfaceHolder;
    private MyCanvas canvas;

    Drawer(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
    }

    public static int getOffsetX() {
        return offsetX;
    }

    static void setOffsetX(int offsetX) {
        Drawer.offsetX = offsetX;
    }

    public static int getOffsetY() {
        return offsetY;
    }

    static void setOffsetY(int offsetY) {
        Drawer.offsetY = offsetY;
    }

    private static int round(float fx) {
        int x = Math.round(fx);
        int offset = x % Drawer.CELL_SIZE;
        x -= offset;
        return offset < (Drawer.CELL_SIZE / 2) ? x : x + Drawer.CELL_SIZE;
    }

    static Point round(Point p) {
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

    void drawModel(DrawableModel drawableModel) {
        Canvas simpleCanvas = surfaceHolder.lockCanvas();
        canvas = new MyCanvas(simpleCanvas);
        drawBackground();
        for (Drawable element : drawableModel.drawables()) {
            if (drawableModel.chosen() == element) {
                ELEMENTS_PAINT.setColor(Color.MAGENTA);
            }
            element.draw(canvas);
            ELEMENTS_PAINT.setColor(ELEMENTS_COLOR);
        }
        for (DrawableWire wire : DrawableModel.wires()) {
            wire.draw(canvas);
        }
        for (DrawableNode node : drawableModel.realNodes()) {
            node.draw(canvas);
        }
        if (drawableModel.isShowingCurrents()) {
            showCurrents(drawableModel);
        }
        if (drawableModel.holding()) {
            Paint highLightPaint = new Paint();
            highLightPaint.setColor(Color.YELLOW);
            DrawableNode highlighted = (DrawableNode) drawableModel.getHolded();
            canvas.drawCircle(highlighted.x(), highlighted.y(), CELL_SIZE / 5, highLightPaint);
        }
        surfaceHolder.unlockCanvasAndPost(simpleCanvas);
    }

    private void showCurrents(DrawableModel drawableModel) {
        for (Drawable d : drawableModel.drawables()) {
            Element e = (Element) d;
            String current = e.getCurrent() + "A";
            System.out.println(current);
            Rect textSize = new Rect();
            ELEMENTS_PAINT.getTextBounds(current, 0, current.length(), textSize);
            canvas.save();
            if (e.isVertical()) {
                canvas.translate(e.x() + Drawer.getOffsetX(), e.y() + Drawer.getOffsetY());
                canvas.rotate(90);
                canvas.translate(-e.x() - Drawer.getOffsetX(), -e.y() - Drawer.getOffsetY());
            }

            canvas.drawText(current, e.x() - textSize.width() / 2, e.y() - CELL_SIZE,
                    ELEMENTS_PAINT);
            canvas.restore();
        }
    }

    private class MyCanvas extends Canvas {
        private Canvas canvas;

        MyCanvas(Canvas canvas) {
            this.canvas = canvas;
        }

        @Override
        public void drawLine(float startX, float startY, float stopX, float stopY, @NonNull Paint
                paint) {
            canvas.drawLine(startX + Drawer.offsetX, startY + Drawer.offsetY,
                    stopX + Drawer.offsetX, stopY + Drawer.offsetY, paint);
        }

        @Override
        public void drawColor(int color) {
            canvas.drawColor(color);
        }

        @Override
        public void drawCircle(float cx, float cy, float radius, @NonNull Paint paint) {
            canvas.drawCircle(cx + Drawer.offsetX, cy + Drawer.offsetY, radius, paint);
        }

        @Override
        public void drawText(@NonNull String text, float x, float y, @NonNull Paint paint) {
            canvas.drawText(text, x + Drawer.offsetX, y + Drawer.offsetY, paint);
        }

        @Override
        public int save() {
            return canvas.save();
        }

        @Override
        public void restore() {
            canvas.restore();
        }

        @Override
        public void translate(float dx, float dy) {
            canvas.translate(dx, dy);
        }

        @Override
        public void rotate(float degrees) {
            canvas.rotate(degrees);
        }
    }
}
