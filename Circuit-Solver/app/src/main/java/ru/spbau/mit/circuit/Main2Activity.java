package ru.spbau.mit.circuit;


import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;

public class Main2Activity extends Activity implements SurfaceHolder.Callback, OnTouchListener {

    float mX, mY;
    private SurfaceHolder surfaceHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main2);

        SurfaceView surface = (SurfaceView) findViewById(R.id.surface);
        Button newResistor = findViewById(R.id.button1);
        newResistor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Canvas canvas = surfaceHolder.lockCanvas();
                Drawer.drawBackground(canvas);
                Paint paint = new Paint();
                paint.setColor(Color.YELLOW);
                canvas.drawRect(new Rect(50, 100, 200, 300), paint);
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
        Drawer.drawBackground(canvas);
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        Canvas canvas = surfaceHolder.lockCanvas();
        Drawer.drawBackground(canvas);
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
                Drawer.drawBackground(canvas);
                canvas.drawCircle(mX, mY, 50, new Paint());
                surfaceHolder.unlockCanvasAndPost(canvas);
        }
        return true;
    }
}