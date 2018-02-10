package ru.spbau.mit.circuit.ui;

import android.graphics.Canvas;

import ru.spbau.mit.circuit.logic.math.functions.Function;
import ru.spbau.mit.circuit.logic.math.functions.PolyFunction;
import ru.spbau.mit.circuit.model.circuitObjects.elements.Element;

public class PrettyPrinter {
    public static void print(Canvas canvas, Element element) {
        Function current = element.getCurrent();
        if (!current.getDown().isIdentity()) {
            throw new UnsupportedOperationException();
        }
        print(canvas, current.getDown());
    }

    private static void print(Canvas canvas, PolyFunction function) {

    }
}
