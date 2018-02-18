package ru.spbau.mit.circuit.ui;

import android.support.annotation.NonNull;

import ru.spbau.mit.circuit.MainActivity;
import ru.spbau.mit.circuit.model.Model;
import ru.spbau.mit.circuit.model.circuitObjects.elements.Element;
import ru.spbau.mit.circuit.model.circuitObjects.wires.Wire;
import ru.spbau.mit.circuit.ui.DrawableElements.Drawable;
import ru.spbau.mit.circuit.ui.DrawableElements.DrawableWire;

public class Uploader {
    public static void load(@NonNull DrawableModel drawableModel) {
        Model model = MainActivity.ui.getModel();
        for (Element element : model.elements()) {
            drawableModel.loadElement((Drawable) element);
        }
        for (Wire wire : model.wires()) {
            DrawableNode to = (DrawableNode) DrawableModel.getByPoint(wire.to().position());
            if (to == null) {
                drawableModel.addRealNode((DrawableNode) wire.to());
            }
            DrawableNode from = (DrawableNode) DrawableModel.getByPoint(wire.from().position());
            if (from == null) {
                drawableModel.addRealNode((DrawableNode) wire.from());
            }
            drawableModel.loadWire((DrawableWire) wire);
        }
    }
}
