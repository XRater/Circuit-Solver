package ru.spbau.mit.circuit.logic.math.linearContainers;


import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.spbau.mit.circuit.logic.math.algebra.Field;
import ru.spbau.mit.circuit.logic.math.algebra.Linear;
import ru.spbau.mit.circuit.logic.math.matrices.Matrix;

@SuppressWarnings({"WeakerAccess", "unused"})
public class Polynom<T extends Field<T>> implements Field<Polynom<T>>, Linear<T, Polynom<T>> {

    private final ArrayList<T> monoms = new ArrayList<>();
    private final T zero;

    public Polynom(T zero) {
        this.zero = zero;
    }

    public Polynom(T zero, List<T> cfs) {
        this.zero = zero;
        for (int i = 0; i < cfs.size(); i++) {
            addMonom(i, cfs.get(i));
        }
    }

    public Polynom(Polynom<T> p) {
        zero = p.zero;
        int index = 0;
        for (T t : p.monoms) {
            addMonom(index, t);
            index++;
        }
    }

    private void addMonom(int index, T value) {
        while (index >= monoms.size()) {
            monoms.add(zero.getZero());
        }
        monoms.set(index, monoms.get(index).add(value));
    }

    @Override
    public Polynom<T> add(Polynom<T> p) {
        Polynom<T> ans = new Polynom<>(this);
        int index = 0;
        for (T t : p.monoms) {
            ans.addMonom(index, t);
            index++;
        }
        return ans;
    }

    @Override
    public Polynom<T> subtract(Polynom<T> p) {
        Polynom<T> ans = new Polynom<>(this);
        int index = 0;
        for (T t : p.monoms) {
            ans.addMonom(index, t.negate());
            index++;
        }
        return ans;
    }

    @Override
    public Polynom<T> multiply(Polynom<T> p) {
        Polynom<T> ans = new Polynom<>(zero);
        for (int i = 0; i < monoms.size(); i++) {
            for (int j = 0; j < p.monoms.size(); j++) {
                ans.addMonom(i + j, monoms.get(i).multiply(p.monoms.get(j)));
            }
        }
        return ans;
    }

    @Override
    public Polynom<T> negate() {
        return this.multiplyConstant(zero.getIdentity().negate());
    }

    @Override
    public Polynom<T> multiplyConstant(T cf) {
        Polynom<T> ans = new Polynom<>(this);
        int index = 0;
        for (T t : monoms) {
            ans.addMonom(index, t.multiply(cf));
            index++;
        }
        return ans;
    }

    @Override
    public boolean isZero() {
        for (int i = 0; i < monoms.size(); i++) {
            if (!monoms.get(i).isZero()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isIdentity() {
        for (int i = 0; i < monoms.size(); i++) {
            if (i == 0 && !monoms.get(i).isIdentity()) {
                return false;
            }
            if (i != 0 && !monoms.get(i).isZero()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Polynom<T> getZero() {
        return new Polynom<>(zero.getZero());
    }

    @Override
    public Polynom<T> getIdentity() {
        return new Polynom<>(zero, Collections.singletonList(zero.getIdentity()));
    }

    @Override
    public Polynom<T> reciprocal() {
        throw new UnsupportedOperationException();
    }

    public T evaluate(T t) {
        T ans = zero.getZero();
        T power = zero.getIdentity();
        for (int i = 0; i < monoms.size(); i++) {
            ans = ans.add(power.multiply(monoms.get(i)));
            power = power.multiply(t);
        }
        return ans;
    }

    @NonNull
    public Matrix<T> evaluate(Matrix<T> matrix) {
        Matrix<T> ans = matrix.getZero(matrix.size());
        Matrix<T> power = matrix.getIdentity(matrix.size());
        for (int i = 0; i < monoms.size(); i++) {
            ans = ans.add(power.multiplyConstant(monoms.get(i)));
            double begin = System.currentTimeMillis();
            power = matrix.multiply(power);
            double end = System.currentTimeMillis();
            System.out.println("Time: " + (end - begin));
        }
        return ans;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < monoms.size(); i++) {
            sb.append(' ').append(monoms.get(i)).append("x^").append(i);
        }
        return sb.toString();
    }

    public ArrayList<T> monoms() {
        return monoms;
    }
}
