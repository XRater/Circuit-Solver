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
import ru.spbau.mit.circuit.model.Point;

import static ru.spbau.mit.circuit.ui.Drawer.drawables;
import static ru.spbau.mit.circuit.ui.Drawer.offsetX;
import static ru.spbau.mit.circuit.ui.Drawer.offsetY;

public class NewCircuitActivity extends Activity implements SurfaceHolder.Callback, OnTouchListener {
    private int startX, startY;
    private int oldOffsetX = 0, oldOffsetY = 0;
    private boolean inWireMode;
    private SurfaceHolder surfaceHolder;
    private WireController wireController = new WireController(this);
    private Drawable chosen = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_circuit);

        final SurfaceView surface = (SurfaceView) findViewById(R.id.surface);
        Button newResistor = findViewById(R.id.newResistor);
        newResistor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawableResistor r = new DrawableResistor(new Point(5 * Drawer.cellSize, 5 * Drawer.cellSize));
                drawables.add(r);
                redraw();
            }
        });

        Button newCapacitor = findViewById(R.id.newCapacitor);
        newCapacitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Canvas canvas = surfaceHolder.lockCanvas();
                MyCanvas myCanvas = new MyCanvas(canvas);
//                DrawableCapacitor c = new DrawableCapacitor();
//                c.x = 9 * Drawer.cellSize;
//                c.y = 9 * Drawer.cellSize;
//                drawables.add(c);
//                Drawer.drawEverything(myCanvas);
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        });

        Button drawWire = findViewById(R.id.drawWire);
        drawWire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!inWireMode) {
                    inWireMode = true;
                    surface.setOnTouchListener(wireController);
                } else {
                    inWireMode = false;
                    surface.setOnTouchListener(NewCircuitActivity.this);
                }
            }
        });

        surfaceHolder = surface.getHolder();
        surfaceHolder.addCallback(this);
        surface.setOnTouchListener(NewCircuitActivity.this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        redraw();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        redraw();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                chosen = null;
                startX = 0;
                startY = 0;
//                mX = motionEvent.getX();
//                mY = motionEvent.getY();
//                for (Drawable d : drawables) {
//                    Element e = (Element) d;
//                    if (abs(e.x - mX + offsetX) < 100 && abs(e.y - mY + offsetY) < 100)
//                        chosen = e;
//                }
                return true;
            }

            case MotionEvent.ACTION_MOVE: {
                float mX = motionEvent.getX();
                float mY = motionEvent.getY();
                for (Drawable d : drawables) {
                    if (d.getPoint().distance(mX - offsetX, mY - offsetY) < 2 * Drawer.cellSize) {
                        chosen = d;
                    }
                }
                if (chosen != null) {
                    chosen.setX((int) mX - offsetX);
                    chosen.setY((int) mY - offsetY);

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
                if (chosen != null) { // TODO More pretty.
                    chosen.setX(chosen.x() / Drawer.cellSize * Drawer.cellSize);
                    chosen.setY(chosen.y() / Drawer.cellSize * Drawer.cellSize);
                    redraw();
                    // TODO Notify controller.
                    chosen.updatePosition(chosen.x(), chosen.y());
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