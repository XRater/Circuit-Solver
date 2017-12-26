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
import android.widget.ImageButton;
import android.widget.Toast;

import ru.spbau.mit.circuit.MainActivity;
import ru.spbau.mit.circuit.R;
import ru.spbau.mit.circuit.logic.CircuitShortingException;
import ru.spbau.mit.circuit.model.elements.Element;
import ru.spbau.mit.circuit.model.interfaces.CircuitObject;
import ru.spbau.mit.circuit.model.node.Node;
import ru.spbau.mit.circuit.model.node.Point;
import ru.spbau.mit.circuit.ui.DrawableElements.Drawable;
import ru.spbau.mit.circuit.ui.DrawableElements.DrawableBattery;
import ru.spbau.mit.circuit.ui.DrawableElements.DrawableCapacitor;
import ru.spbau.mit.circuit.ui.DrawableElements.DrawableResistor;

import static ru.spbau.mit.circuit.ui.DrawableModel.getByPoint;

public class NewCircuitActivity extends Activity implements SurfaceHolder.Callback,
        OnTouchListener {
    public Drawable chosen;
    private DrawableModel drawableModel;
    private Drawer drawer;
    private int startX, startY;
    private int oldOffsetX = 0, oldOffsetY = 0;
    private Button delete;
    private Button changeValue;
    private Button rotate;

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

        ImageButton newResistor = findViewById(R.id.newResistor);
        newResistor.setOnClickListener(view -> {
            DrawableResistor r = new DrawableResistor(drawableModel.getPossiblePosition());
            drawableModel.addElement(r);
        });

        Button newCapacitor = findViewById(R.id.newCapacitor);
        newCapacitor.setOnClickListener(view -> {
            DrawableCapacitor c = new DrawableCapacitor(drawableModel.getPossiblePosition());
            drawableModel.addElement(c);
        });

        Button newBattery = findViewById(R.id.newBattery);
        newBattery.setOnClickListener(view -> {
            DrawableBattery b = new DrawableBattery(drawableModel.getPossiblePosition());
            drawableModel.addElement(b);
        });

        Button play = findViewById(R.id.play);
        play.setOnClickListener(view -> {
            try {
                MainActivity.ui.calculateCurrents();
                drawableModel.changeShowingCurrents();
                drawableModel.redraw();
                drawableModel.changeShowingCurrents();
            } catch (CircuitShortingException e) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Battery is shorted.", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        delete = findViewById(R.id.delete);
        delete.setOnClickListener(v -> {
            if (chosen instanceof Element) {
                drawableModel.removeElement(chosen);
            }
            if (chosen instanceof DrawableNode) {
                drawableModel.removeWire((DrawableNode) chosen);
            }
            makeButtonsInvisible();
            drawableModel.redraw();
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

        rotate = findViewById(R.id.rotate);
        rotate.setOnClickListener(v -> {
            Element element = (Element) chosen;
            drawableModel.rotateElement(element);
            drawableModel.redraw();
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
                chosen = getByPoint(point);
                if (chosen instanceof Element) {
                    delete.setVisibility(View.VISIBLE);
                    changeValue.setVisibility(View.VISIBLE);
                    rotate.setVisibility(View.VISIBLE);
                    drawableModel.setChosen(chosen);
                } else if (chosen instanceof DrawableNode) {
                    DrawableNode node = (DrawableNode) chosen;
                    if (!node.isRealNode()) { // May be condition about how many wires it has.
                        delete.setVisibility(View.VISIBLE);
                    } else {
                        makeButtonsInvisible();
                    }
                } else {
                    makeButtonsInvisible();
                    drawableModel.setChosen(null);
                }

                if (chosen instanceof DrawableNode) {
                    if (drawableModel.holding()) {
                        drawableModel.connect((DrawableNode) chosen);
                    } else {
                        drawableModel.hold((DrawableNode) chosen);
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

    private void makeButtonsInvisible() {
        delete.setVisibility(View.INVISIBLE);
        changeValue.setVisibility(View.INVISIBLE);
        rotate.setVisibility(View.INVISIBLE);
    }
}