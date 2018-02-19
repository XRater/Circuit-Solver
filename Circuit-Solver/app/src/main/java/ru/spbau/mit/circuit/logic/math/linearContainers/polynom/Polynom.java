package ru.spbau.mit.circuit.logic.math.linearContainers.polynom;


import android.support.annotation.NonNull;

import ru.spbau.mit.circuit.logic.math.algebra.Pair;
import ru.spbau.mit.circuit.logic.math.algebra.PolyElement;
import ru.spbau.mit.circuit.logic.math.algebra.interfaces.Field;
import ru.spbau.mit.circuit.logic.math.matrices.Matrix;

/**
 * Base polynom above any field.
 *
 * @param <F> field class.
 */
public class Polynom<F extends Field<F>> extends PolyElement<F, Monom, Polynom<F>> {

    private final F fieldZero;

    Polynom(F zero) {
        fieldZero = zero.getZero();
    }

    @SuppressWarnings("unused")
    public F fieldZero() {
        return fieldZero.getZero();
    }

    @SuppressWarnings("WeakerAccess")
    public F fieldIdentity() {
        return fieldZero.getIdentity();
    }

    @Override
    protected Polynom<F> empty() {
        return new Polynom<>(fieldZero);
    }

    @Override
    protected Polynom<F> single() {
        Polynom<F> answer = new Polynom<>(fieldZero);
        answer.add(fieldZero.getIdentity(), Monom.identity());
        return answer;
    }

    /**
     * Evaluates polynom value in the given field point.
     */
    @SuppressWarnings("unused")
    public F evaluate(F f) {
        F ans = fieldZero.getZero();
        int pow = 0;
        F power = fieldZero.getIdentity();
        for (Pair<F, Monom> pair : data.values()) {
            ans.add(power.multiply(pair.first()));
            while (pow <= pair.second().power()) {
                power = power.multiply(f);
                pow++;
            }
        }
        return ans;
    }

    /**
     * The method evaluates polynom value from the given matrix of field elements.
     */
    @NonNull
    public Matrix<F> evaluate(Matrix<F> matrix) {
        Matrix<F> ans = matrix.getZero(matrix.size());
        int pow = 0;
        Matrix<F> power = matrix.getIdentity(matrix.size());
        for (Pair<F, Monom> pair : data.values()) {
            ans = ans.add(power.multiplyConstant(pair.first()));
            while (pow <= pair.second().power()) {
                power = matrix.multiply(power);
                pow++;
            }
        }
        return ans;
    }

}
