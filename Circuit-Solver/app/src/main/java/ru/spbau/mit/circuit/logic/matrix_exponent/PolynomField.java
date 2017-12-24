package ru.spbau.mit.circuit.logic.matrix_exponent;


import org.apache.commons.math3.Field;
import org.apache.commons.math3.FieldElement;

import java.util.Collections;

class PolynomField<T extends FieldElement<T>> implements Field<Polynom<T>> {

    private final Field<T> field;

    PolynomField(Field<T> field) {
        this.field = field;
    }

    @Override
    public Polynom<T> getZero() {
        return new Polynom<>(field, Collections.singletonList(field.getZero()));
    }

    @Override
    public Polynom<T> getOne() {
        return new Polynom<>(field, Collections.singletonList(field.getOne()));
    }

    @Override
    public Class<? extends FieldElement<Polynom<T>>> getRuntimeClass() {
        throw new UnsupportedOperationException();
    }
}
