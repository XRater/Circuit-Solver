package ru.spbau.mit.circuit;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Drawer {
    private static int cellSize = 50;
    public static void drawBackground(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        for (int i = 0; i < canvas.getHeight(); i += cellSize) {
            canvas.drawLine(0, i, canvas.getWidth(), i, paint);
        }
        for (int i = 0; i < canvas.getWidth(); i += cellSize) {
            canvas.drawLine(i, 0, i, canvas.getHeight(), paint);
        }
    }
}
