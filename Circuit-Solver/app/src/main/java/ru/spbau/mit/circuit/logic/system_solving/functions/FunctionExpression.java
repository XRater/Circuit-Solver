package ru.spbau.mit.circuit.logic.system_solving.functions;

import ru.spbau.mit.circuit.logic.system_solving.polynoms.Linear;

public interface FunctionExpression extends Linear<FunctionExpression> {

    @Override
    default void add(FunctionExpression item) {

    }

    @Override
    default void mul(double d) {

    }

}
