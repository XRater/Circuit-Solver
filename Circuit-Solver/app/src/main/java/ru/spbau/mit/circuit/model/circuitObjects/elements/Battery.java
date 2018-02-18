package ru.spbau.mit.circuit.model.circuitObjects.elements;

import android.support.annotation.NonNull;

import ru.spbau.mit.circuit.model.circuitObjects.nodes.Node;

public abstract class Battery extends Element {

    private ResultValue voltage;

    public Battery(@NonNull Node from, @NonNull Node to) {
        super(from, to);
    }

    @Override
    public ResultValue getVoltage() {
        return voltage;
    }

    @Override
    public ResultValue getCharacteristicValue() {
        return voltage;
    }

    @Override
    public void setCharacteristicValue(Expression voltage) {
        this.voltage = voltage;
    }

    @NonNull
    @Override
    public String getCharacteristicName() {
        return "voltage";
    }
}
