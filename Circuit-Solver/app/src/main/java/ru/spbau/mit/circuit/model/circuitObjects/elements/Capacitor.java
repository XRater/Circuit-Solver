package ru.spbau.mit.circuit.model.circuitObjects.elements;

import android.support.annotation.NonNull;

import ru.spbau.mit.circuit.model.circuitObjects.nodes.Node;

public abstract class Capacitor extends Element {
    private double capacity = 1;

    protected Capacitor(@NonNull Node from, @NonNull Node to) {
        super(from, to);
    }

    @Override
    public double getCharacteristicValue() {
        return capacity;
    }

    @Override
    public void setCharacteristicValue(double capacity) {
        this.capacity = capacity;
    }

    @NonNull
    @Override
    public String getCharacteristicName() {
        return "capacity";
    }
}
