package ru.spbau.mit.circuit.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import ru.spbau.mit.circuit.MainActivity;
import ru.spbau.mit.circuit.R;
import ru.spbau.mit.circuit.logic.CircuitShortingException;
import ru.spbau.mit.circuit.logic.NotImplementedYetException;
import ru.spbau.mit.circuit.model.circuitObjects.elements.Capacitor;
import ru.spbau.mit.circuit.model.circuitObjects.elements.Element;
import ru.spbau.mit.circuit.model.circuitObjects.nodes.Node;
import ru.spbau.mit.circuit.model.circuitObjects.nodes.Point;
import ru.spbau.mit.circuit.storage.Converter;
import ru.spbau.mit.circuit.ui.DrawableElements.Drawable;
import ru.spbau.mit.circuit.ui.DrawableElements.DrawableBattery;
import ru.spbau.mit.circuit.ui.DrawableElements.DrawableCapacitor;
import ru.spbau.mit.circuit.ui.DrawableElements.DrawableResistor;

import static ru.spbau.mit.circuit.ui.DrawableModel.getByPoint;

public class NewCircuitActivity extends Activity implements SurfaceHolder.Callback,
        OnTouchListener {

    private static DrawableModel drawableModel; // static because after turning the screen onCreate is called.
    @Nullable
    private Drawable chosen;
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
        Drawer drawer = new Drawer(surfaceHolder);
        if (drawableModel == null) {
            drawableModel = new DrawableModel(this, drawer);
            MainActivity.ui.setDrawableModel(drawableModel);
        } else {
            drawableModel.setDrawer(drawer);
        }
        Button newResistor = findViewById(R.id.newResistor);
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
        play.setOnClickListener(view -> onPlayClicked());

        delete = findViewById(R.id.delete);
        delete.setOnClickListener(v -> onDeleteClicked());

        changeValue = findViewById(R.id.changeValue);
        changeValue.setOnClickListener(v -> onChangeValueClicked());

        rotate = findViewById(R.id.rotate);
        rotate.setOnClickListener(v -> {
            Element element = (Element) chosen;
            drawableModel.rotateElement(element);
            drawableModel.redraw();
        });

        Button save = findViewById(R.id.save);
        save.setOnClickListener(v -> onSaveClicked());
        surface.setOnTouchListener(NewCircuitActivity.this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (MainActivity.ui.circuitWasLoaded()) {
            Uploader.load(drawableModel);
        }
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
    public boolean onTouch(View view, @NonNull MotionEvent motionEvent) {
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
                    makeButtonsInvisible();
                    if (!node.isRealNode()) { // May be condition about how many wires it has.
                        delete.setVisibility(View.VISIBLE);
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

                if (chosen instanceof Element) {
                    Point point = getPoint(motionEvent.getX(), motionEvent.getY());
                    drawableModel.move(chosen, point);
                } else if (chosen instanceof Node) {
                    // May be we should do something.
                }
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

    private void onChangeValueClicked() {
        Element element = (Element) chosen;
        final EditText taskEditText = new EditText(this);
        taskEditText.setText(String.valueOf(element.getCharacteristicValue()));

        if (element instanceof Capacitor) {
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);

            final TextView cpName = new TextView(this);
            cpName.setText("Capacity");

            layout.addView(cpName);
            layout.addView(taskEditText);

            final TextView voltageName = new TextView(this);
            voltageName.setText("Voltage");
            layout.addView(voltageName);

            final EditText voltageField = new EditText(this);
            voltageField.setText(String.valueOf(element.getVoltage()));
            layout.addView(voltageField);

            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Change capacity and voltage")
                    .setView(layout)
                    .setPositiveButton("Set new values", (dialog1, which) -> {
                        String cp = String.valueOf(taskEditText.getText());
                        String voltage = String.valueOf(voltageField.getText());
                        try {
                            element.setCharacteristicValue(Double.parseDouble(cp));
                            element.setVoltage(Double.parseDouble(voltage));
                        } catch (@NonNull NumberFormatException | NullPointerException e2) {
                            // No info
                        }
                        drawableModel.redraw();
                    })
                    .setNegativeButton("Cancel", null)
                    .create();
            dialog.show();
        } else {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Change " + element.getCharacteristicName())
                    .setView(taskEditText)
                    .setPositiveButton("Set new value", (dialog1, which) -> {
                        String value = String.valueOf(taskEditText.getText());
                        try {
                            element.setCharacteristicValue(Double.parseDouble(value));
                        } catch (@NonNull NumberFormatException | NullPointerException e2) {
                            // No info
                        }
                        drawableModel.redraw();
                    })
                    .setNegativeButton("Cancel", null)
                    .create();
            dialog.show();
        }
    }

    private void onPlayClicked() {
        try {
            MainActivity.ui.calculateCurrents();
            drawableModel.changeShowingCurrents();
            drawableModel.redraw();
            drawableModel.changeShowingCurrents();
        } catch (CircuitShortingException e) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Battery is shorted.", Toast.LENGTH_SHORT);
            toast.show();
        } catch (NotImplementedYetException e) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Unsupported circuit.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void onSaveClicked() {
        final EditText taskEditText = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Choose where you want to save this circuit.")
                .setView(taskEditText)
                .setMessage("Name this circuit")
                .setPositiveButton("This device", (dialog1, which) -> {
                    if (!MainActivity.ui.save(Converter.Mode.LOCAL,
                            String.valueOf(taskEditText.getText()))) {
                        Toast.makeText(getApplicationContext(),
                                "This name already exists, please choose another one.",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Google Drive", (dialog1, which) -> {
                    if (!MainActivity.ui.save(Converter.Mode.DRIVE,
                            String.valueOf(taskEditText.getText()))) {
                        Toast.makeText(getApplicationContext(),
                                "This name already exists, please choose another one.",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .create();
        dialog.show();
    }

    private void onDeleteClicked() {
        if (chosen instanceof Element) {
            drawableModel.removeElement(chosen);
        }
        if (chosen instanceof DrawableNode) {
            drawableModel.removeWire((DrawableNode) chosen);
        }
        makeButtonsInvisible();
        drawableModel.redraw();
        chosen = null;
    }
}