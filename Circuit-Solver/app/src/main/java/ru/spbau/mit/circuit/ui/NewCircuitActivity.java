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

public class NewCircuitActivity extends Activity implements SurfaceHolder.Callback, OnTouchListener {

    float mX, mY;
    private SurfaceHolder surfaceHolder;

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
                DrawableResistor r = new DrawableResistor();
                r.x = 400;
                r.y = 400;
                Drawer.drawables.add(r);
                Drawer.drawEverything(canvas);
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        });
        Button newCapacitor = findViewById(R.id.newCapacitor);
        newCapacitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Canvas canvas = surfaceHolder.lockCanvas();
                DrawableCapacitor c = new DrawableCapacitor();
                c.x = 800;
                c.y = 800;
                Drawer.drawables.add(c);
                Drawer.drawEverything(canvas);
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
        Drawer.drawEverything(canvas);
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        Canvas canvas = surfaceHolder.lockCanvas();
        Drawer.drawEverything(canvas);
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mX = motionEvent.getX();

                mY = motionEvent.getY();
                Canvas canvas = surfaceHolder.lockCanvas();
                Drawer.drawEverything(canvas);
                canvas.drawCircle(mX, mY, 50, Drawer.elementsPaint);
                surfaceHolder.unlockCanvasAndPost(canvas);
        }
        return true;
    }
}