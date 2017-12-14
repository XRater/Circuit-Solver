package ru.spbau.mit.circuit.Storage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import ru.spbau.mit.circuit.model.Model;

public class Converter {
    public static byte[] serialize(Model model) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
        objectOutputStream.writeObject(model);
        objectOutputStream.close();
        return out.toByteArray();
    }

    public static Model deserialize(ByteArrayInputStream in) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(in);
        Model model = (Model) objectInputStream.readObject();
        return model;
    }
}
