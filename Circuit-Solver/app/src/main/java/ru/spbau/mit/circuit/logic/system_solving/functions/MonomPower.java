package ru.spbau.mit.circuit.logic.system_solving.functions;


import ru.spbau.mit.circuit.logic.system_solving.polynoms.Monom;

public class MonomPower extends Monom<Power> implements FunctionExpression {

    public MonomPower(int power, double coefficient) {
        super(new Power(power), coefficient);
    }

}
