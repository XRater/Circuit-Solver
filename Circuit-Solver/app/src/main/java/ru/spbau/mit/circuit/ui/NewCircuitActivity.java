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
import ru.spbau.mit.circuit.model.CircuitObject;
import ru.spbau.mit.circuit.model.elements.Movable;
import ru.spbau.mit.circuit.model.node.Point;
import ru.spbau.mit.circuit.ui.DrawableElements.DrawableBattery;
import ru.spbau.mit.circuit.ui.DrawableElements.DrawableCapacitor;
import ru.spbau.mit.circuit.ui.DrawableElements.DrawableResistor;

public class NewCircuitActivity extends Activity implements SurfaceHolder.Callback,
        OnTouchListener {
    //    private WireController wireController = new WireController(this);
    private Model model;
    private Drawer drawer;

    private CircuitObject chosen;
    private int startX, startY;
    private int oldOffsetX = 0, oldOffsetY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_circuit);

        final SurfaceView surface = findViewById(R.id.surface);
        SurfaceHolder surfaceHolder = surface.getHolder();
        surfaceHolder.addCallback(this);
        drawer = new Drawer(surfaceHolder);
        model = new Model(drawer);

        Button newResistor = findViewById(R.id.newResistor);
        newResistor.setOnClickListener(view -> {
            DrawableResistor r = new DrawableResistor(new Point(5 * Drawer.CELL_SIZE, 5 * Drawer
                    .CELL_SIZE));
            model.addElement(r);
        });

        Button newCapacitor = findViewById(R.id.newCapacitor);
        newCapacitor.setOnClickListener(view -> {
            DrawableCapacitor c = new DrawableCapacitor(new Point(5 * Drawer.CELL_SIZE, 5 * Drawer
                    .CELL_SIZE));
            model.addElement(c);
        });

        Button newBattery = findViewById(R.id.newBattery);
        newBattery.setOnClickListener(view -> {
            DrawableBattery b = new DrawableBattery(new Point(7 * Drawer.CELL_SIZE, 7 * Drawer
                    .CELL_SIZE));
            model.addElement(b);
        });

        Button drawWire = findViewById(R.id.drawWire);
        drawWire.setOnClickListener(view -> {
//            if (!inWireMode) {
//                inWireMode = true;
//                surface.setOnTouchListener(wireController);
//            } else {
//                inWireMode = false;
//                model.highlighted = null;
//                surface.setOnTouchListener(NewCircuitActivity.this);
//            }
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
                model.showCurrents();
                model.redraw();
                //                `.drawModel(model, myCanvas);
//                Drawer.showCurrents(model, myCanvas);
//                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        });
        surface.setOnTouchListener(NewCircuitActivity.this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        model.redraw();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        model.redraw();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                Point point = getPoint(motionEvent.getX(), motionEvent.getY());
                if (chosen != null) {
                    throw new RuntimeException();
                }
                chosen = model.getByPoint(point);
                if (model.holding()) {
                    if (chosen instanceof WireEnd) {
                        model.connect((WireEnd) chosen);
                    }
                    model.unhold();
                    chosen = null;
                } else {
                    if (chosen instanceof WireEnd) {
                        model.hold((WireEnd) chosen);
                    }
                }
                //store point
                //                startX = 0;
//                startY = 0;
                return true;
            }

            case MotionEvent.ACTION_MOVE: {
                if (chosen == null) {
                    //Move field
                    return true;
                }
                if (chosen instanceof Movable) {
                    //Click on Node/Element
                    Point point = getPoint(motionEvent.getX(), motionEvent.getY());
                    model.move((Movable) chosen, point);
                } else {
                    //Click on WirePoint
                }
//                CircuitObject clicked = model.getByPoint(new Point(
//                        Math.round(motionEvent.getX()), Math.round(motionEvent.getY())));
/*                int mX = Math.round(motionEvent.getX());
                int mY = Math.round(motionEvent.getY());
                if (chosen == null) {
                    for (Drawable d : model.drawables) {
                        if (d.clickedInside(mX, mY)) {
                            chosen = d;
                            System.out.println(chosen.toString());
                        }
                    }
                }
                if (chosen != null) {
                    chosen.replace(new Point(
                            Math.round(mX), Math.round(mY)));
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
                return true;*/
            }

            case MotionEvent.ACTION_UP: {
                if (chosen == null) {
                    // Move field
                }
                if (chosen instanceof CircuitObject) {
                    // Seems like do nothing
                } else {
                    // WirePoint
                }
                chosen = null;
                return true;
                /*                if (chosen != null) {
                    chosen.replace(new Point(
                            Drawer.round(chosen.x()), Drawer.round(chosen.y())));
                    redraw();
                    chosen = null;
                    return true;
                }
                startX = 0;
                startY = 0;
                return true;*/
            }
        }
        return true;
    }

    private Point getPoint(float x, float y) {
        return new Point(Drawer.round(x), Drawer.round(y));
    }
//
//    public void redraw() {
//        Canvas canvas = surfaceHolder.lockCanvas();
//        MyCanvas myCanvas = new MyCanvas(canvas);
//        Drawer.drawModel(model, myCanvas);
//        surfaceHolder.unlockCanvasAndPost(canvas);
//    }

}