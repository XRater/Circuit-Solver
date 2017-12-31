package ru.spbau.mit.circuit.storage;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.ByteArrayInputStream;
import java.sql.SQLException;
import java.util.List;

public interface Storage {

    void save(byte[] stream, String name) throws SQLException;

    @NonNull
    List<String> getCircuits();

    @Nullable
    ByteArrayInputStream load(String name);

    void delete(String name) throws LoadingException;

}
