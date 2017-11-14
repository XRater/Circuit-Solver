package ru.spbau.mit.circuit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import ru.spbau.mit.circuit.controler.Controller;
import ru.spbau.mit.circuit.ui.NewCircuitActivity;
import ru.spbau.mit.circuit.ui.UI;

public class MainActivity extends AppCompatActivity {
    private static final Controller controller = new Controller();
    public static final UI ui = controller.getUi();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onNewCircuit(View view) {
        Intent intent = new Intent(MainActivity.this, NewCircuitActivity.class);
        startActivity(intent);
    }

    public void onLoadCircuit(View view) {
        Toast toast = Toast.makeText(getApplicationContext(),
                "Load circuit", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void onSettings(View view) {
        Toast toast = Toast.makeText(getApplicationContext(),
                "Settings", Toast.LENGTH_SHORT);
        toast.show();
    }
}
