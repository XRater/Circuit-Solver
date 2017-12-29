package ru.spbau.mit.circuit.logic.math.linearContainers;


import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.spbau.mit.circuit.logic.math.algebra.Field;
import ru.spbau.mit.circuit.logic.math.algebra.Linear;
import ru.spbau.mit.circuit.logic.math.matrices.Matrix;

/**
 * Polynom class. May use any field class as coefficients.
 *
 * @param <C> type of polynom coefficient.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class Polynom<C extends Field<C>> implements Field<Polynom<C>>, Linear<C, Polynom<C>> {

    private final ArrayList<C> monoms = new ArrayList<>();
    private final C zero;

    public Polynom(C zero) {
        this.zero = zero;
    }

    public Polynom(C zero, List<C> cfs) {
        this.zero = zero;
        for (int i = 0; i < cfs.size(); i++) {
            addMonom(i, cfs.get(i));
        }
    }

    public Polynom(Polynom<C> p) {
        zero = p.zero;
        int index = 0;
        for (C c : p.monoms) {
            addMonom(index, c);
            index++;
        }
    }

    private void addMonom(int index, C value) {
        while (index >= monoms.size()) {
            monoms.add(zero.getZero());
        }
        monoms.set(index, monoms.get(index).add(value));
    }

    @Override
    public Polynom<C> add(Polynom<C> p) {
        Polynom<C> ans = new Polynom<>(this);
        int index = 0;
        for (C c : p.monoms) {
            ans.addMonom(index, c);
            index++;
        }
        return ans;
    }

    @Override
    public Polynom<C> subtract(Polynom<C> p) {
        Polynom<C> ans = new Polynom<>(this);
        int index = 0;
        for (C c : p.monoms) {
            ans.addMonom(index, c.negate());
            index++;
        }
        return ans;
    }

    @Override
    public Polynom<C> multiply(Polynom<C> p) {
        Polynom<C> ans = new Polynom<>(zero);
        for (int i = 0; i < monoms.size(); i++) {
            for (int j = 0; j < p.monoms.size(); j++) {
                ans.addMonom(i + j, monoms.get(i).multiply(p.monoms.get(j)));
            }
        }
        return ans;
    }

    @Override
    public Polynom<C> negate() {
        return this.multiplyConstant(zero.getIdentity().negate());
    }

    @Override
    public Polynom<C> multiplyConstant(C cf) {
        Polynom<C> ans = new Polynom<>(this);
        int index = 0;
        for (C c : monoms) {
            ans.addMonom(index, c.multiply(cf));
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
    public Polynom<C> getZero() {
        return new Polynom<>(zero.getZero());
    }

    @Override
    public Polynom<C> getIdentity() {
        return new Polynom<>(zero, Collections.singletonList(zero.getIdentity()));
    }

    @Override
    public Polynom<C> reciprocal() {
        throw new UnsupportedOperationException();
    }

    public C evaluate(C c) {
        C ans = zero.getZero();
        C power = zero.getIdentity();
        for (int i = 0; i < monoms.size(); i++) {
            ans = ans.add(power.multiply(monoms.get(i)));
            power = power.multiply(c);
        }
        return ans;
    }

    @NonNull
    public Matrix<C> evaluate(Matrix<C> matrix) {
        Matrix<C> ans = matrix.getZero(matrix.size());
        Matrix<C> power = matrix.getIdentity(matrix.size());
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

    public ArrayList<C> monoms() {
        return monoms;
    }
}
