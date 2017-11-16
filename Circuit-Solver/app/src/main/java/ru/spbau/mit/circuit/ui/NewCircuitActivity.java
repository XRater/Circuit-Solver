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
import android.widget.Toast;

import ru.spbau.mit.circuit.MainActivity;
import ru.spbau.mit.circuit.R;
import ru.spbau.mit.circuit.logic.CircuitShortingException;
import ru.spbau.mit.circuit.model.elements.Element;
import ru.spbau.mit.circuit.model.node.Point;
import ru.spbau.mit.circuit.ui.DrawableElements.Drawable;
import ru.spbau.mit.circuit.ui.DrawableElements.DrawableBattery;
import ru.spbau.mit.circuit.ui.DrawableElements.DrawableCapacitor;
import ru.spbau.mit.circuit.ui.DrawableElements.DrawableResistor;

import static ru.spbau.mit.circuit.ui.Drawer.drawables;
import static ru.spbau.mit.circuit.ui.Drawer.offsetX;
import static ru.spbau.mit.circuit.ui.Drawer.offsetY;

public class NewCircuitActivity extends Activity implements SurfaceHolder.Callback,
        OnTouchListener {
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

        final SurfaceView surface = findViewById(R.id.surface);
        Button newResistor = findViewById(R.id.newResistor);
        newResistor.setOnClickListener(view -> {
            DrawableResistor r = new DrawableResistor(new Point(5 * Drawer.CELL_SIZE, 5 * Drawer
                    .CELL_SIZE));
            addElement(r);
        });

        Button newCapacitor = findViewById(R.id.newCapacitor);
        newCapacitor.setOnClickListener(view -> {
            DrawableCapacitor c = new DrawableCapacitor(new Point(5 * Drawer.CELL_SIZE, 5 * Drawer
                    .CELL_SIZE));
            addElement(c);
        });

        Button newBattery = findViewById(R.id.newBattery);
        newBattery.setOnClickListener(view -> {
            DrawableBattery b = new DrawableBattery(new Point(7 * Drawer.CELL_SIZE, 7 * Drawer
                    .CELL_SIZE));
            addElement(b);
        });

        Button drawWire = findViewById(R.id.drawWire);
        drawWire.setOnClickListener(view -> {
            if (!inWireMode) {
                inWireMode = true;
                surface.setOnTouchListener(wireController);
            } else {
                inWireMode = false;
                Drawer.highlighted = null;
                surface.setOnTouchListener(NewCircuitActivity.this);
            }
        });

        Button play = findViewById(R.id.play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    MainActivity.ui.calculateCurrents();
                } catch (CircuitShortingException e) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Battery is shorted.", Toast.LENGTH_SHORT);
                    toast.show();
                }
                Canvas canvas = surfaceHolder.lockCanvas();
                MyCanvas myCanvas = new MyCanvas(canvas);
                Drawer.drawEverything(myCanvas);
                Drawer.showCurrents(myCanvas);
                surfaceHolder.unlockCanvasAndPost(canvas);
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
                return true;
            }

            case MotionEvent.ACTION_MOVE: {
                int mX = Math.round(motionEvent.getX());
                int mY = Math.round(motionEvent.getY());
                if (chosen == null) {
                    for (Drawable d : drawables) {
                        if (d.clickedInside(mX - offsetX, mY - offsetY)) {
                            chosen = d;
                            System.out.println(chosen.toString());
                        }
                    }
                }
                if (chosen != null) {
                    chosen.replace(new Point(
                            Math.round(mX) - offsetX, Math.round(mY) - offsetY));
                } else {
                    if (startX == 0 && startY == 0) {
                        startX = Math.round(mX);
                        startY = Math.round(mY);
                        oldOffsetX = Drawer.offsetX;
                        oldOffsetY = Drawer.offsetY;
                    }
                    Drawer.offsetX = oldOffsetX + Math.round(mX) - startX;
                    Drawer.offsetY = oldOffsetY + Math.round(mY) - startY;
                }
                redraw();
                return true;
            }

            case MotionEvent.ACTION_UP: {
                if (chosen != null) {
                    chosen.replace(new Point(
                            Drawer.round(chosen.x()), Drawer.round(chosen.y())));
                    redraw();
                    chosen = null;
                    return true;
                }
                startX = 0;
                startY = 0;
                return true;
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

    private void addElement(Drawable e) {
        drawables.add(e);
        MainActivity.ui.addToModel((Element) e);
        redraw();
    }
}