package ru.spbau.mit.circuit;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import ru.spbau.mit.circuit.storage.Converter;
import ru.spbau.mit.circuit.storage.StorageException;


public class LoadActivity extends Activity {
    private Converter.Mode mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_activity);

        ListView names = findViewById(R.id.names);

        Button local = findViewById(R.id.local);
        Button drive = findViewById(R.id.drive);

        names.setOnItemClickListener((parent, view, position, id) -> {
            String name = ((TextView) view).getText().toString();
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Chose action")
                    .setPositiveButton("Load", (dialog1, which) ->
                            checkForExceptions(MainActivity.ui.load(mode, name)))
                    .setPositiveButton("Load", (dialog1, which) -> {
                        try {
                            MainActivity.ui.load(mode, name);
                        } catch (StorageException e) {
                            showErrorToast(mode);
                        }
                    })
                    .setNegativeButton("Delete", (dialog1, which) -> {
                        checkForExceptions(MainActivity.ui.removeFromStorage(mode, name));
                        try {
                            MainActivity.ui.removeFromStorage(mode, name);
                        } catch (StorageException e) {
                            showErrorToast(mode);
                        }
                        if (mode == Converter.Mode.LOCAL) {
                            local.callOnClick();
                        } else {
                            drive.callOnClick();
                        }
                    })
                    .create();
            dialog.show();
        });

        local.setOnClickListener(v -> {
            ArrayAdapter<String> adapter = null;
            try {
                adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_activated_1,
                        MainActivity.ui.getCircuits(Converter.Mode.LOCAL));
            } catch (StorageException e) {
                showErrorToastLocal();
            }
            names.setAdapter(adapter);
            mode = Converter.Mode.LOCAL;
        });

        drive.setOnClickListener(v -> {
            ArrayAdapter<String> adapter = null;
            try {
                adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_activated_1,
                        MainActivity.ui.getCircuits(Converter.Mode.DRIVE));
            } catch (StorageException e) {
                showErrorToastDrive();
            }
            names.setAdapter(adapter);
            mode = Converter.Mode.DRIVE;
        });
    }

    private void checkForExceptions(Boolean result) {
        if (result == null) {
            Toast.makeText(getApplicationContext(),
                    "Something went wrong. Please check your Internet connection.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void showErrorToast(Converter.Mode mode) {
        if (mode == Converter.Mode.LOCAL) {
            showErrorToastLocal();
        } else {
            showErrorToastDrive();
        }
    }

    private void showErrorToastLocal() {
        Toast toast = Toast.makeText(getApplicationContext(),
                "An error occurred.", Toast.LENGTH_SHORT);
        toast.show();
    }

    private void showErrorToastDrive() {
        Toast toast = Toast.makeText(getApplicationContext(),
                "An error occurred. Check your Internet connection and try again.",
                Toast.LENGTH_SHORT);
        toast.show();
    }
}
