package ru.spbau.mit.circuit.storage;

import android.app.Activity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import ru.spbau.mit.circuit.model.Model;

public class Converter {

    private final List<Storage> storageList = new ArrayList<>();

    public Converter(Activity activity) {
        storageList.add(new Local(activity));
        storageList.add(new DriveStorage(activity));
    }

    private Storage getStorage(Mode mode) {
        return (mode == Mode.LOCAL) ? storageList.get(0) : storageList.get(1);
    }

    public boolean save(Mode mode, Model model, String name) throws IOException {
        List<String> names = getStorage(mode).getCircuits();
        if (names.contains("name")) {
            return false;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
        objectOutputStream.writeObject(model);
        objectOutputStream.close();
        try {
            getStorage(mode).save(out.toByteArray(), name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public List<String> getCircuits(Mode mode) {
        return getStorage(mode).getCircuits();
    }

    public Model load(Mode mode, String name) {
        ByteArrayInputStream in = getStorage(mode).load(name);
        Model model = null;
        try (ObjectInputStream objectInputStream = new ObjectInputStream(in)) {
            model = (Model) objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return model;
    }

    public enum Mode {
        LOCAL,
        DRIVE
    }
}
