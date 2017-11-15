package ru.spbau.mit.circuit.ui.DrawableElements;


import ru.spbau.mit.circuit.model.elements.Element;
import ru.spbau.mit.circuit.model.point.Point;

public abstract class FiledElement extends Element implements Drawable {
    protected FiledElement(Point from, Point to) {
        super(from, to);
    }
}
