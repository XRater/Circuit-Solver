package ru.spbau.mit.circuit.logic.system_solving.functions;


import ru.spbau.mit.circuit.logic.system_solving.polynoms.Monom;

public class MonomPolyExponent extends Monom<PolyExponent> implements FunctionExpression {

    public MonomPolyExponent(int deg, double power, double coefficient) {
        super(new PolyExponent(new Power(deg), new Exponent(power)), coefficient);
    }
}
