package ru.spbau.mit.circuit.logic.math.linearSystems;

import android.support.annotation.NonNull;

import ru.spbau.mit.circuit.logic.math.algebra.Field;
import ru.spbau.mit.circuit.logic.math.algebra.Linear;

/**
 * Linear container which stores any container, that implements gauss interface in the left part
 * (represents variables with coefficients) and any linear container in the right part.
 *
 * @param <C> type of coefficients for both parts
 * @param <T> type of the left part
 * @param <U> type of the right part
 */
public class Equation<C extends Field<C>, T extends Gauss<C, T>, U extends Linear<C, U>>
        implements Gauss<C, Equation<C, T, U>> {

    private T coefficients;
    private U constant;

    public Equation(T coefficients, U constant) {
        this.coefficients = coefficients;
        this.constant = constant;
    }

    @NonNull
    @Override
    public Equation<C, T, U> add(@NonNull Equation<C, T, U> item) {
        coefficients = coefficients.add(item.coefficients);
        constant = constant.add(item.constant);
        return this;
    }

    @NonNull
    @Override
    public Equation<C, T, U> multiplyConstant(C c) {
        coefficients = coefficients.multiplyConstant(c);
        constant = constant.multiplyConstant(c);
        return this;
    }

    @Override
    public C coefficientAt(int index) {
        return coefficients.coefficientAt(index);
    }

    @Override
    public int size() {
        return coefficients.size();
    }

    public T coefficients() {
        return coefficients;
    }

    public U constant() {
        return constant;
    }

//    @Override
//    public String toString() {
//        return coefficients.toString() + " = " + constant.toString();
//    }


    @NonNull
    @Override
    public String toString() {
        return coefficients.toString() + " = " + constant.toString();
    }
}
