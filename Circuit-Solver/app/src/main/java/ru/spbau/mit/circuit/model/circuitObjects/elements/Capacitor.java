package ru.spbau.mit.circuit.model.circuitObjects.elements;

import android.support.annotation.NonNull;

import ru.spbau.mit.circuit.logic.math.expressions.Expression;
import ru.spbau.mit.circuit.logic.math.expressions.Expressions;
import ru.spbau.mit.circuit.model.circuitObjects.nodes.Node;

public abstract class Capacitor extends Element {

    private Expression capacity = Expressions.constant(1);
    private Expression initialVoltage = Expressions.zero();

    protected Capacitor(@NonNull Node from, @NonNull Node to) {
        super(from, to);
    }

    @Override
    public Expression getCharacteristicValue() {
        return capacity;
    }

    @Override
    public void setCharacteristicValue(Expression capacity) {
        this.capacity = capacity;
    }

    public Expression getInitialVoltage() {
        return initialVoltage;
    }

    public void setInitialVoltage(Expression initialVoltage) {
        this.initialVoltage = initialVoltage;
    }

    @NonNull
    @Override
    public String getCharacteristicName() {
        return "capacity";
    }
}
