package ru.spbau.mit.circuit.model.circuitObjects.elements;

import android.support.annotation.NonNull;

import ru.spbau.mit.circuit.model.circuitObjects.nodes.Node;

public abstract class Resistor extends Element {
    private double resistance = 1;

    public Resistor(@NonNull Node from, @NonNull Node to) {
        super(from, to);
    }

    @SuppressWarnings("WeakerAccess")
    public double getResistance() {
        return resistance;
    }

    @Override
    public double getCharacteristicValue() {
        return resistance;
    }

    @Override
    public void setCharacteristicValue(double resistance) {
        this.resistance = resistance;
    }

    @NonNull
    @Override
    public String getCharacteristicName() {
        return "resistance";
    }
}
