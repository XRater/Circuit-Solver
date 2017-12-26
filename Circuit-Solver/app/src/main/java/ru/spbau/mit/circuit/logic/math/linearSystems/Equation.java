package ru.spbau.mit.circuit.logic.math.linearSystems;

import ru.spbau.mit.circuit.logic.math.algebra.Field;
import ru.spbau.mit.circuit.logic.math.algebra.Linear;

public class Equation<C extends Field<C>, T extends Gauss<C, T>, U extends Linear<C, U>>
        implements Gauss<C, Equation<C, T, U>> {

    private T coefficients;
    private U constant;

    public Equation(T coefficients, U constant) {
        this.coefficients = coefficients;
        this.constant = constant;
    }

    @Override
    public Equation<C, T, U> add(Equation<C, T, U> item) {
        coefficients = coefficients.add(item.coefficients);
        constant = constant.add(item.constant);
        return this;
    }

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


    @Override
    public String toString() {
        return coefficients.toString() + " = " + constant.toString();
    }
}
