package ru.spbau.mit.circuit.storage;


import java.io.ByteArrayInputStream;
import java.util.Collection;

public interface Storage {

    void saveToNewFile(byte[] stream, String filename);

    Collection<String> getFiles();

    ByteArrayInputStream loadFromFile(String filename);

}
