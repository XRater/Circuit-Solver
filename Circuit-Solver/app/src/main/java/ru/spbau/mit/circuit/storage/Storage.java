package ru.spbau.mit.circuit.storage;


import java.io.ByteArrayInputStream;
import java.sql.SQLException;
import java.util.List;

public interface Storage {

    void save(byte[] stream, String name) throws SQLException;

    List<String> getCircuits();

    ByteArrayInputStream load(String name);

    void delete(String name) throws LoadingException;

}
