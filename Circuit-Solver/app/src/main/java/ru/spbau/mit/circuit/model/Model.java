package ru.spbau.mit.circuit.model;


import java.util.ArrayList;

import ru.spbau.mit.circuit.model.elements.CircuitItem;

public class Model {
    private ArrayList<CircuitItem> circuitItems = new ArrayList<>();

    public ArrayList<CircuitItem> getCircuitItems() {
        return circuitItems;
    }

    public void addElement(CircuitItem circuitItem) {
        circuitItems.add(circuitItem);
    }

    public boolean removeElement(CircuitItem circuitItem) {
        return circuitItems.remove(circuitItem);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Model:\n");
        for (CircuitItem e : circuitItems) {
            sb.append(e).append("\n");
        }
        return sb.toString();
    }

    public void clear() {
        circuitItems.clear();
    }
}
