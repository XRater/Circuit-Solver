package ru.spbau.mit.circuit.ui;


import android.app.Activity;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;

import ru.spbau.mit.circuit.R;
import ru.spbau.mit.circuit.model.Element;

import static java.lang.Math.abs;
import static ru.spbau.mit.circuit.ui.Drawer.drawables;
import static ru.spbau.mit.circuit.ui.Drawer.offsetX;
import static ru.spbau.mit.circuit.ui.Drawer.offsetY;

public class NewCircuitActivity extends Activity implements SurfaceHolder.Callback, OnTouchListener {

    float mX, mY;
    int startX, startY;
    int oldOffsetX = 0, oldOffsetY = 0;
    private SurfaceHolder surfaceHolder;
    private Element choosen = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_circuit);

        SurfaceView surface = (SurfaceView) findViewById(R.id.surface);
        Button newResistor = findViewById(R.id.newResistor);
        newResistor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Canvas canvas = surfaceHolder.lockCanvas();
                MyCanvas myCanvas = new MyCanvas(canvas);
                DrawableResistor r = new DrawableResistor();
                r.x = 400;
                r.y = 400;
                drawables.add(r);
                Drawer.drawEverything(myCanvas);
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        });
        Button newCapacitor = findViewById(R.id.newCapacitor);
        newCapacitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Canvas canvas = surfaceHolder.lockCanvas();
                MyCanvas myCanvas = new MyCanvas(canvas);
                DrawableCapacitor c = new DrawableCapacitor();
                c.x = 800;
                c.y = 800;
                drawables.add(c);
                Drawer.drawEverything(myCanvas);
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        });
        surfaceHolder = surface.getHolder();
        surfaceHolder.addCallback(this);
        surface.setOnTouchListener(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Canvas canvas = surfaceHolder.lockCanvas();
        MyCanvas myCanvas = new MyCanvas(canvas);
        Drawer.drawEverything(myCanvas);
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        Canvas canvas = surfaceHolder.lockCanvas();
        MyCanvas myCanvas = new MyCanvas(canvas);
        Drawer.drawEverything(myCanvas);
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                choosen = null;
                startX = 0;
                startY = 0;
//                mX = motionEvent.getX();
//                mY = motionEvent.getY();
//                for (Drawable d : drawables) {
//                    Element e = (Element) d;
//                    if (abs(e.x - mX + offsetX) < 100 && abs(e.y - mY + offsetY) < 100)
//                        choosen = e;
//                }
                return true;
            }

            case MotionEvent.ACTION_MOVE: {
                mX = motionEvent.getX();
                mY = motionEvent.getY();
                for (Drawable d : drawables) {
                    Element e = (Element) d;
                    if (abs(e.x - mX + offsetX) < 100 && abs(e.y - mY + offsetY) < 100) {
                        choosen = e;
                    }
                }
                if (choosen != null) {
                    choosen.x = (int) mX - offsetX;
                    choosen.y = (int) mY - offsetY;

                } else {
                    if (startX == 0 && startY == 0) {
                        startX = (int) mX;
                        startY = (int) mY;
                        oldOffsetX = Drawer.offsetX;
                        oldOffsetY = Drawer.offsetY;
                    }
                    Drawer.offsetX = oldOffsetX + (int) mX - startX;
                    Drawer.offsetY = oldOffsetY + (int) mY - startY;
                }
                redraw();
                return true;
            }

            case MotionEvent.ACTION_UP: {
                if (choosen != null) { // TODO More pretty.
                    choosen.x = choosen.x / Drawer.cellSize * Drawer.cellSize;
                    choosen.y = choosen.y / Drawer.cellSize * Drawer.cellSize;
                    redraw();
                    // TODO Notify controller.
                    return true;
                }
                startX = 0;
                startY = 0;
            }
        }
        return true;
    }

    public void redraw() {
        Canvas canvas = surfaceHolder.lockCanvas();
        MyCanvas myCanvas = new MyCanvas(canvas);
        Drawer.drawEverything(myCanvas);
        surfaceHolder.unlockCanvasAndPost(canvas);
    }
}