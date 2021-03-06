package ru.spbau.mit.circuit.storage;

import android.app.Activity;
import android.support.annotation.NonNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ru.spbau.mit.circuit.model.Model;

/**
 * Base class for working with storages.
 */
public class Converter {

    private final List<Storage> storageList = new ArrayList<>();

    public Converter(@NonNull Activity activity) {
        storageList.add(new Local(activity));
        storageList.add(new DriveStorage(activity));
    }

    private Storage getStorage(Mode mode) {
        return (mode == Mode.LOCAL) ? storageList.get(0) : storageList.get(1);
    }

    public boolean save(Mode mode, Model model, String name) throws StorageException {
        List<String> names = getStorage(mode).getCircuits();
        if (names.contains(name)) {
            return false;
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);) {
            objectOutputStream.writeObject(model);
            getStorage(mode).save(out.toByteArray(), name);
        } catch (@NonNull IOException | SQLException e) {
            throw new StorageException();
        }
        return true;
    }

    @NonNull
    public List<String> getCircuits(Mode mode) {
        return getStorage(mode).getCircuits();
    }

    public Model load(Mode mode, String name) throws StorageException {
        ByteArrayInputStream in = getStorage(mode).load(name);
        Model model;
        try (ObjectInputStream objectInputStream = new ObjectInputStream(in)) {
            model = (Model) objectInputStream.readObject();
        } catch (@NonNull IOException | ClassNotFoundException e) {
            throw new StorageException(e);
        }
        return model;
    }

    public boolean delete(Mode mode, String name) throws StorageException {
        try {
            getStorage(mode).delete(name);
        } catch (Exception e) {
            throw new StorageException(e);
        }
        return true;
    }

    /**
     * Choosing with which storage to work with.
     */
    public enum Mode {
        LOCAL,
        DRIVE
    }
}
