package ru.spbau.mit.circuit.logic.system_solving.functions;

import ru.spbau.mit.circuit.logic.system_solving.polynoms.Vector;

public class FunctionExpression extends Vector<PrimaryFunction, LinearConstant> {

    private static final LinearConstant lc = new LinearConstant();

    private FunctionExpression() {
        super(lc);
    }

    private FunctionExpression(PrimaryFunction function, double cf) {
        super(lc);
        add(function, cf);
    }

    public static FunctionExpression empty() {
        return new FunctionExpression();
    }

    public static FunctionExpression constant(double c) {
        return new FunctionExpression(new Identity(), c);
    }
}
