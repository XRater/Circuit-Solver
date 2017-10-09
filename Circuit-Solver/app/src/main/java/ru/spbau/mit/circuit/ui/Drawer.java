package ru.spbau.mit.circuit.ui;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Drawer {
    public static void drawBackground(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        for (int i = 0; i < canvas.getHeight(); i += 40) {
            canvas.drawLine(0, i, canvas.getWidth(), i, paint);
        }
        for (int i = 0; i < canvas.getWidth(); i += 40) {
            canvas.drawLine(i, 0, i, canvas.getHeight(), paint);
        }
    }
}
