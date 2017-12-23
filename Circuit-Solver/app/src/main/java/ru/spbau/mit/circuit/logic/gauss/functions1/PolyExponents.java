package ru.spbau.mit.circuit.logic.gauss.functions1;


import org.apache.commons.math3.Field;
import org.apache.commons.math3.FieldElement;

class PolyExponents implements Field<PolyExponent> {

    @Override
    public PolyExponent getZero() {
        return new PolyExponent(0, 0, 0);
    }

    @Override
    public PolyExponent getOne() {
        return new PolyExponent(1, 0, 0);
    }

    @Override
    public Class<? extends FieldElement<PolyExponent>> getRuntimeClass() {
        return getZero().getClass();
    }
}
