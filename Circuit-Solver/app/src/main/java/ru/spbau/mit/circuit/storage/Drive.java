package ru.spbau.mit.circuit.storage;


import android.app.Activity;

import java.io.ByteArrayInputStream;
import java.util.Collection;

public class Drive implements Storage {

    public Drive(Activity activity) {

    }

    @Override
    public void saveToNewFile(byte[] stream, String filename) {

    }

    @Override
    public Collection<String> getFiles() {
        return null;
    }

    @Override
    public ByteArrayInputStream loadFromFile(String filename) {
        return null;
    }
}
