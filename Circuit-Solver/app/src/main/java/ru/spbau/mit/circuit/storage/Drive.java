package ru.spbau.mit.circuit.storage;


import android.app.Activity;

import java.io.ByteArrayInputStream;
import java.util.List;

public class Drive implements Storage {

    public Drive(Activity activity) {

    }

    @Override
    public void save(byte[] stream, String filename) {

    }

    @Override
    public List<String> getCircuits() {
        return null;
    }

    @Override
    public ByteArrayInputStream load(String filename) {
        return null;
    }
}
