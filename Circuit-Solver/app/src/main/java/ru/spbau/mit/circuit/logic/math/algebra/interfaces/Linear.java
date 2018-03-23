package ru.spbau.mit.circuit.logic.math.algebra.interfaces;

public interface Linear<F extends Field<F>, L extends Linear<F, L>> extends Abel<L> {

    L multiplyConstant(F f);

}
