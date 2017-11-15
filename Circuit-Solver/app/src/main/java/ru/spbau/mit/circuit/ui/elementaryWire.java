package ru.spbau.mit.circuit.ui;

import ru.spbau.mit.circuit.model.elements.CircuitItem;

public class elementaryWire {
    public boolean have = false;
    public CircuitItem circuitItem1;
    public CircuitItem circuitItem2;

    public void addElementaryWire(CircuitItem chosen, CircuitItem other) {
        have = true;
        circuitItem1 = chosen;
        circuitItem2 = other;
    }
}
