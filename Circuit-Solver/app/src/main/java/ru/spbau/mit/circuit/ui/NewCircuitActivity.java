package ru.spbau.mit.circuit.ui;

import android.app.Activity;
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
import ru.spbau.mit.circuit.model.interfaces.CircuitObject;
import ru.spbau.mit.circuit.model.interfaces.WireEnd;
import ru.spbau.mit.circuit.model.node.Node;
import ru.spbau.mit.circuit.model.node.Point;
import ru.spbau.mit.circuit.ui.DrawableElements.Drawable;
import ru.spbau.mit.circuit.ui.DrawableElements.DrawableBattery;
import ru.spbau.mit.circuit.ui.DrawableElements.DrawableCapacitor;
import ru.spbau.mit.circuit.ui.DrawableElements.DrawableResistor;

public class NewCircuitActivity extends Activity implements SurfaceHolder.Callback,
        OnTouchListener {
    //    private WireController wireController = new WireController(this);
    private DrawableModel drawableModel;
    private Drawer drawer;

    private Drawable chosen;
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
        drawableModel = new DrawableModel(drawer);
        MainActivity.ui.setDrawableModel(drawableModel);

        Button newResistor = findViewById(R.id.newResistor);
        newResistor.setOnClickListener(view -> {
            DrawableResistor r = new DrawableResistor(new Point(5 * Drawer.CELL_SIZE, 5 * Drawer
                    .CELL_SIZE));
            drawableModel.addElement(r);
        });

        Button newCapacitor = findViewById(R.id.newCapacitor);
        newCapacitor.setOnClickListener(view -> {
            DrawableCapacitor c = new DrawableCapacitor(new Point(5 * Drawer.CELL_SIZE, 5 * Drawer
                    .CELL_SIZE));
            drawableModel.addElement(c);
        });

        Button newBattery = findViewById(R.id.newBattery);
        newBattery.setOnClickListener(view -> {
            DrawableBattery b = new DrawableBattery(new Point(7 * Drawer.CELL_SIZE, 7 * Drawer
                    .CELL_SIZE));
            drawableModel.addElement(b);
        });
//
//        Button drawWire = findViewById(R.id.drawWire);
//        drawWire.setOnClickListener(view -> {
//            if (!inWireMode) {
//                inWireMode = true;
//                surface.setOnTouchListener(wireController);
//            } else {
//                inWireMode = false;
//                drawableModel.highlighted = null;
//                surface.setOnTouchListener(NewCircuitActivity.this);
//            }
//        });

        Button play = findViewById(R.id.play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    MainActivity.ui.calculateCurrents();
                    drawableModel.changeShowingCurrents();
                } catch (CircuitShortingException e) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Battery is shorted.", Toast.LENGTH_SHORT);
                    toast.show();
                }
                drawableModel.redraw();
                drawableModel.changeShowingCurrents();
            }
        });
        surface.setOnTouchListener(NewCircuitActivity.this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        drawableModel.redraw();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        drawableModel.redraw();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                Point point = getPoint(motionEvent.getX(), motionEvent.getY());
                chosen = drawableModel.getByPoint(point);

                if (chosen instanceof WireEnd) {
                    if (drawableModel.holding()) {
                        drawableModel.connect((WireEnd) chosen);
                        drawableModel.unhold();
                    } else {
                        drawableModel.hold((WireEnd) chosen);
                    }
                    //chosen = null;
                    return true;
                }

                //store point
                if (chosen == null) {
                    startX = Math.round(motionEvent.getX());
                    startY = Math.round(motionEvent.getY());
                    oldOffsetX = Drawer.getOffsetX();
                    oldOffsetY = Drawer.getOffsetY();
                }
                return true;
            }

            case MotionEvent.ACTION_MOVE: {
                System.out.println(motionEvent.getX() + " " + motionEvent.getY());
                if (chosen == null) {
                    //Move field
                    Drawer.setOffsetX(oldOffsetX + Math.round(motionEvent.getX()) - startX);
                    Drawer.setOffsetY(oldOffsetY + Math.round(motionEvent.getY()) - startY);
                    drawableModel.redraw();
                    System.out.println(oldOffsetX);
                    return true;
                }
                // ??? Node moving
                if (chosen instanceof Element) {
                    //Click on Element
                    Point point = getPoint(motionEvent.getX(), motionEvent.getY());
                    System.out.println(point);


                    drawableModel.move(chosen, point);
                } else if (chosen instanceof Node) {
                } else {
                    //Click on WirePoint
                }

//                CircuitObject clicked = drawableModel.getByPoint(new Point(
//                        Math.round(motionEvent.getX()), Math.round(motionEvent.getY())));
/*                int mX = Math.round(motionEvent.getX());
                int mY = Math.round(motionEvent.getY());
                if (chosen == null) {
                    for (Drawable d : drawableModel.drawables) {
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
                }*/
                return true;
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
        return Drawer.round(new Point(Math.round(x), Math.round(y)));
    }

}