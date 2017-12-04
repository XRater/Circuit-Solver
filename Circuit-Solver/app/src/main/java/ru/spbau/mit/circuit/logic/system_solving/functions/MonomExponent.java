package ru.spbau.mit.circuit.logic.system_solving.functions;


import ru.spbau.mit.circuit.logic.system_solving.polynoms.Monom;

public class MonomExponent extends Monom<Exponent> implements FunctionExpression {

    public MonomExponent(double power, double coefficient) {
        super(new Exponent(power), coefficient);
    }
}
