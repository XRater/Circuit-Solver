package ru.spbau.mit.circuit.model.circuitObjects.elements;

import android.support.annotation.NonNull;

import ru.spbau.mit.circuit.logic.math.expressions.Expression;
import ru.spbau.mit.circuit.logic.math.expressions.Expressions;
import ru.spbau.mit.circuit.model.circuitObjects.nodes.Node;

public abstract class Battery extends Element {

    private Expression voltage = Expressions.constant(10);

    public Battery(@NonNull Node from, @NonNull Node to) {
        super(from, to);
    }

    @Override
    public Expression getCharacteristicValue() {
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
