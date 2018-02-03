package ru.spbau.mit.circuit.logic.math.variables;


import ru.spbau.mit.circuit.logic.math.expressions.Expression;

public class ExpressionVariable extends Variable<Expression> {

    @SuppressWarnings("unused")
    public ExpressionVariable() {
        super();
    }

    public ExpressionVariable(String name) {
        super(name);
    }
}