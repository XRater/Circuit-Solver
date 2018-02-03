package ru.spbau.mit.circuit.logic.math.algebra.interfaces;


public interface Proportional<F, P extends Proportional<F, P>> {

    P multiplyConstant(F f);

}
