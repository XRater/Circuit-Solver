package ru.spbau.mit.circuit.ui;

import ru.spbau.mit.circuit.MainActivity;
import ru.spbau.mit.circuit.model.Model;
import ru.spbau.mit.circuit.model.elements.Element;

public class Uploader {
    public static void load() {
        Model model = MainActivity.ui.getModel();
        for (Element element : model.elements()) {

        }
    }
}
