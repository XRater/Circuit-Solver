package ru.spbau.mit.circuit;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import ru.spbau.mit.circuit.storage.Converter;


public class LoadActivity extends Activity {
    private Converter.Mode mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_activity);

        ListView names = findViewById(R.id.names);

        names.setOnItemClickListener((parent, view, position, id) -> {
            String name = ((TextView) view).getText().toString();
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Change aaction")
                    .setPositiveButton("Load", (dialog1, which) -> {
                        MainActivity.ui.load(mode, name);
                    })
                    .setNegativeButton("Delete", null)
                    .create();
            dialog.show();
        });

        Button local = findViewById(R.id.local);
        local.setOnClickListener(v -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_activated_1, MainActivity.ui.getCircuits(Converter
                    .Mode.LOCAL));

            names.setAdapter(adapter);
            mode = Converter.Mode.LOCAL;
        });

        Button drive = findViewById(R.id.drive);
        drive.setOnClickListener(v -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, MainActivity.ui.getCircuits(Converter
                    .Mode.DRIVE));
            names.setAdapter(adapter);
            mode = Converter.Mode.DRIVE;
        });
    }
}
