package ru.spbau.mit.circuit.model.circuitObjects.elements;

import ru.spbau.mit.circuit.logic.math.ResultValue;

import android.support.annotation.NonNull;

import ru.spbau.mit.circuit.model.circuitObjects.nodes.Node;

public abstract class Resistor extends Element {
    private ResultValue resistance;

    public Resistor(@NonNull Node from, @NonNull Node to) {
        super(from, to);
    }

    @SuppressWarnings("WeakerAccess")
    public ResultValue getResistance() {
        return resistance;
    }

    @Override
    public ResultValue getCharacteristicValue() {
        return resistance;
    }

    @Override
    public void setCharacteristicValue(ResultValue resistance) {
        this.resistance = resistance;
    }

    @NonNull
    @Override
    public String getCharacteristicName() {
        return "resistance";
    }
}
