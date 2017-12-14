package ru.spbau.mit.circuit.storage;

import android.app.Activity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ru.spbau.mit.circuit.model.Model;

public class Converter {

    public static final int LOCAL = 0;
    public static final int DRIVE = 1;

    private final List<Storage> storageList = new ArrayList<>();

    public Converter(Activity activity) {
        storageList.add(new Local(activity));
        storageList.add(new DriveStorage(activity));
    }

    public void saveToNewFile(int storageNumber, Model model, String filename) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
        objectOutputStream.writeObject(model);
        objectOutputStream.close();
        storageList.get(storageNumber).saveToNewFile(out.toByteArray(), filename);
    }

    public Collection<String> getFiles(int storageNumber) {
        return storageList.get(storageNumber).getFiles();
    }

    public Model loadFromFile(int storageNumber, String filename) throws IOException,
            ClassNotFoundException {
        ByteArrayInputStream in = storageList.get(storageNumber).loadFromFile(filename);
        ObjectInputStream objectInputStream = new ObjectInputStream(in);
        Model model = (Model) objectInputStream.readObject();
        return model;
    }
}
