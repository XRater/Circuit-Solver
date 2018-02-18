package ru.spbau.mit.circuit.model.circuitObjects.elements;

import android.support.annotation.NonNull;

import ru.spbau.mit.circuit.logic.math.expressions.Expression;
import ru.spbau.mit.circuit.logic.math.expressions.Expressions;
import ru.spbau.mit.circuit.model.circuitObjects.nodes.Node;

public abstract class Resistor extends Element {
    private Expression resistance = Expressions.constant(1);

    public Resistor(@NonNull Node from, @NonNull Node to) {
        super(from, to);
    }

    @SuppressWarnings("WeakerAccess")
    public Expression getResistance() {
        return resistance;
    }

    @Override
    public Expression getCharacteristicValue() {
        return resistance;
    }

    @Override
    public void setCharacteristicValue(Expression resistance) {
        this.resistance = resistance;
    }

    @NonNull
    @Override
    public String getCharacteristicName() {
        return "resistance";
    }
}
