package ru.spbau.mit.circuit.ui;

import ru.spbau.mit.circuit.model.Element;

public class elementaryWire {
    public boolean have = false;
    public Element element1;
    public Element element2;

    public void addElementaryWire(Element chosen, Element other) {
        have = true;
        element1 = chosen;
        element2 = other;
    }
}
