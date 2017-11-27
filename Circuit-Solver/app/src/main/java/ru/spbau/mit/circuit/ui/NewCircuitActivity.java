package ru.spbau.mit.circuit.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
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
    private DrawableModel drawableModel;
    private Drawer drawer;

    private Drawable chosen;
    private int startX, startY;
    private int oldOffsetX = 0, oldOffsetY = 0;
    private Button delete;
    private Button changeValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_circuit);

        final SurfaceView surface = findViewById(R.id.surface);
        SurfaceHolder surfaceHolder = surface.getHolder();
        surfaceHolder.addCallback(this);
        drawer = new Drawer(surfaceHolder);
        drawableModel = new DrawableModel(this, drawer);
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

        Button play = findViewById(R.id.play);
        play.setOnClickListener(view -> {
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
        });

        delete = findViewById(R.id.delete);
        delete.setOnClickListener(v -> {
            drawableModel.removeElement(chosen);
            chosen = null;
        });

        changeValue = findViewById(R.id.changeValue);
        changeValue.setOnClickListener(v -> {
            Element element = (Element) chosen;
            final EditText taskEditText = new EditText(this);
            taskEditText.setText(String.valueOf(element.getCharacteristicValue()));
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Change " + element.getCharacteristicName())
                    .setView(taskEditText)
                    .setPositiveButton("Set new value", (dialog1, which) -> {
                        String value = String.valueOf(taskEditText.getText());
                        try {
                            element.setCharacteristicValue(Double.parseDouble(value));
                        } catch (NumberFormatException | NullPointerException e2) {
                            // No info
                        }
                        drawableModel.redraw();
                    })
                    .setNegativeButton("Cancel", null)
                    .create();
            dialog.show();
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
                if (chosen instanceof Element) {
                    delete.setVisibility(View.VISIBLE);
                    changeValue.setVisibility(View.VISIBLE);
                } else {
                    delete.setVisibility(View.INVISIBLE);
                    changeValue.setVisibility(View.INVISIBLE);
                }

                if (chosen instanceof WireEnd) {
                    if (drawableModel.holding()) {
                        drawableModel.connect((WireEnd) chosen);
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
                    drawableModel.unhold();
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
                    return true;
                }
                // ??? Node moving
                if (chosen instanceof Element) {
                    //Click on Element
                    Point point = getPoint(motionEvent.getX(), motionEvent.getY());


                    drawableModel.move(chosen, point);
                } else if (chosen instanceof Node) {
                } else {
                    //Click on WirePoint
                }
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
                //chosen = null;
                return true;
            }
        }
        return true;
    }

    private Point getPoint(float x, float y) {
        return Drawer.round(new Point(Math.round(x), Math.round(y)));
    }
}