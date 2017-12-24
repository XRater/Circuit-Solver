package ru.spbau.mit.circuit.logic.matrix_exponent;


import android.support.annotation.NonNull;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.FieldElement;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NullArgumentException;

import java.util.ArrayList;
import java.util.List;

public class Polynom<T extends FieldElement<T>> implements FieldElement<Polynom<T>> {

    private final ArrayList<T> monoms = new ArrayList<>();
    private final Field<T> field;

    public Polynom(Field<T> field) {
        this.field = field;
    }

    public Polynom(Field<T> field, List<T> cfs) {
        this.field = field;
        for (int i = 0; i < cfs.size(); i++) {
            addMonom(i, cfs.get(i));
        }
    }

    public Polynom(Polynom<T> p) {
        field = p.field;
        int index = 0;
        for (T t : p.monoms) {
            addMonom(index, t);
            index++;
        }
    }

    private void addMonom(int index, T value) {
        while (index >= monoms.size()) {
            monoms.add(field.getZero());
        }
        monoms.set(index, monoms.get(index).add(value));
    }

    @Override
    public Polynom<T> add(Polynom<T> p) throws NullArgumentException {
        Polynom<T> ans = new Polynom<>(this);
        int index = 0;
        for (T t : p.monoms) {
            ans.addMonom(index, t);
            index++;
        }
        return ans;
    }

    @Override
    public Polynom<T> subtract(Polynom<T> p) throws NullArgumentException {
        Polynom<T> ans = new Polynom<>(this);
        int index = 0;
        for (T t : p.monoms) {
            ans.addMonom(index, t.negate());
            index++;
        }
        return ans;
    }

    @Override
    public Polynom<T> negate() throws NullArgumentException {
        Polynom<T> ans = new Polynom<>(this);
        int index = 0;
        for (T t : monoms) {
            ans.addMonom(index, t.negate());
            index++;
        }
        return ans;
    }

    @Override
    public Polynom<T> multiply(int n) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Polynom<T> multiply(Polynom<T> p) throws NullArgumentException {
        Polynom<T> ans = new Polynom<>(field);
        for (int i = 0; i < monoms.size(); i++) {
            for (int j = 0; j < p.monoms.size(); j++) {
                ans.addMonom(i + j, monoms.get(i).multiply(p.monoms.get(j)));
            }
        }
        return ans;
    }

    @Override
    public Polynom<T> divide(Polynom<T> a) throws NullArgumentException, MathArithmeticException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Polynom<T> reciprocal() throws MathArithmeticException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Field<Polynom<T>> getField() {
//        return new PolynomField(field);
        throw new UnsupportedOperationException();
    }

    public T evaluate(T t) {
        T ans = field.getZero();
        T power = field.getOne();
        for (int i = 0; i < monoms.size(); i++) {
            ans = ans.add(power.multiply(monoms.get(i)));
            power = power.multiply(t);
        }
        return ans;
    }

    @NonNull
    public Matrix<T> evaluate(Matrix<T> matrix) {
        Matrix<T> ans = matrix.getZero();
        Matrix<T> power = matrix.getOne();
        for (int i = 0; i < monoms.size(); i++) {
            ans = ans.add(power.multiply(monoms.get(i)));
            power = power.multiply(matrix);
        }
        return ans;
    }

    @Override
    public String toString() {
        return monoms.toString();
    }
}
