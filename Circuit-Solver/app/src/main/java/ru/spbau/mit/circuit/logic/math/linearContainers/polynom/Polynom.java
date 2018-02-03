package ru.spbau.mit.circuit.logic.math.linearContainers.polynom;


import android.support.annotation.NonNull;

import ru.spbau.mit.circuit.logic.math.algebra.PolyElement;
import ru.spbau.mit.circuit.logic.math.algebra.interfaces.Field;
import ru.spbau.mit.circuit.logic.math.matrices.Matrix;

public class Polynom<C extends Field<C>> extends PolyElement<C, Monom, Polynom<C>> {

    private final C fieldZero;

    Polynom(C zero) {
        fieldZero = zero.getZero();
    }

    public C fieldZero() {
        return fieldZero.getZero();
    }

    public C fieldIdentity() {
        return fieldZero.getIdentity();
    }

    @Override
    protected Polynom<C> empty() {
        return new Polynom<>(fieldZero);
    }

    @Override
    protected Polynom<C> single() {
        Polynom<C> answer = new Polynom<>(fieldZero);
        answer.add(fieldZero.getIdentity(), Monom.identity());
        return answer;
    }

    public C evaluate(C c) {
        C ans = fieldZero.getZero();
        int pow = 0;
        C power = fieldZero.getIdentity();
        for (Pair<C, Monom> pair : data.values()) {
            ans.add(power.multiply(pair.first()));
            while (pow <= pair.second().power()) {
                power = power.multiply(c);
                pow++;
            }
        }
        return ans;
    }

    @NonNull
    public Matrix<C> evaluate(Matrix<C> matrix) {
        Matrix<C> ans = matrix.getZero(matrix.size());
        int pow = 0;
        Matrix<C> power = matrix.getIdentity(matrix.size());
        for (Pair<C, Monom> pair : data.values()) {
            ans = ans.add(power.multiplyConstant(pair.first()));
            while (pow <= pair.second().power()) {
                power = matrix.multiply(power);
                pow++;
            }
        }
        return ans;
    }

}
