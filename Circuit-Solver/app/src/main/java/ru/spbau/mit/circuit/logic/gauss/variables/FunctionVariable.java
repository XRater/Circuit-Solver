package ru.spbau.mit.circuit.logic.gauss.variables;


import ru.spbau.mit.circuit.logic.gauss.algebra.Numerical;
import ru.spbau.mit.circuit.logic.gauss.functions1.Function;

public class FunctionVariable extends Variable<Function> {

    private Numerical initialValue;

    public FunctionVariable() {
        super();
        this.initialValue = initialValue;
    }

    public FunctionVariable(String name) {
        super(name);
        this.initialValue = initialValue;
    }

    public Numerical initialValue() {
        return initialValue;
    }

    public void setInitialValue(Numerical initialValue) {
        this.initialValue = initialValue;
    }

    @Override
    public String toString() {
        if (name.equals("")) {
            return "x" + id;
        }
        return name;
    }
}
