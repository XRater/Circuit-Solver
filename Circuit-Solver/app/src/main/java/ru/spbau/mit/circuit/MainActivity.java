package ru.spbau.mit.circuit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import ru.spbau.mit.circuit.controler.Controller;

public class MainActivity extends AppCompatActivity {
    public static Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        controller = new Controller();
    }

    public void onNewCircuit(View view) {
//        Intent intent = new Intent(MainActivity.this, NewCircuitActivity.class);
//        startActivity(intent);
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
