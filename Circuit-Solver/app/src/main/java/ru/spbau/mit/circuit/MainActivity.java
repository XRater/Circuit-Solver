package ru.spbau.mit.circuit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onNewCircuit(View view) {
//        Intent intent = new Intent(MainActivity.this, Main2Activity.class);
//        startActivity(intent);
    }

    public void onLoadCircuit(View view) {
//        Toast toast = Toast.makeText(getApplicationContext(),
//                "Load circuit", Toast.LENGTH_SHORT);
//        toast.show();
    }

    public void onSettings(View view) {
//        Toast toast = Toast.makeText(getApplicationContext(),
//                "Settings", Toast.LENGTH_SHORT);
//        toast.show();
    }
}
