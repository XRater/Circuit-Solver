package ru.spbau.mit.circuit.ui;

import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

import ru.spbau.mit.circuit.model.Element;

import static ru.spbau.mit.circuit.MainActivity.controller;

public class Drawer {
    public static ArrayList<Drawable> drawables = new ArrayList<>();
    public static int cellSize = 60;
    public static Paint elementsPaint;
    public static int highlighted;
    public static int offsetX = 300, offsetY = 300;
    static {
        elementsPaint = new Paint();
        elementsPaint.setColor(Color.RED);
        elementsPaint.setStrokeWidth(5);
    }

    public static void drawBackground(MyCanvas canvas) {
        canvas.drawColor(Color.BLACK);
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        for (int i = -1000; i < canvas.getHeight(); i += cellSize) {
            canvas.drawLine(-canvas.getWidth(), i, canvas.getWidth(), i, paint);
        }
        for (int i = -1000; i < canvas.getWidth(); i += cellSize) {
            canvas.drawLine(i, -canvas.getHeight(), i, canvas.getHeight(), paint);
        }
    }

    public static void drawEverything(MyCanvas canvas) {
        ArrayList<Element> elements = controller.getElements();

        drawBackground(canvas);
        for (Drawable element : drawables) {
            element.draw(canvas);
        }
    }
}
