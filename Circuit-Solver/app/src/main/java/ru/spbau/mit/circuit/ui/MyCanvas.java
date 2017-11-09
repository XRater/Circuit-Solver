package ru.spbau.mit.circuit.ui;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;


public class MyCanvas {
    private Canvas canvas;

    MyCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public void drawLine(float startX, float startY, float stopX, float stopY, @NonNull Paint paint) {
        canvas.drawLine(startX + Drawer.offsetX, startY + Drawer.offsetY, stopX + Drawer.offsetX, stopY + Drawer.offsetY, paint);
    }

    public void drawColor(int color) {
        canvas.drawColor(color);
    }

    public float getHeight() {
        return canvas.getHeight();
    }

    public float getWidth() {
        return canvas.getWidth();
    }
}