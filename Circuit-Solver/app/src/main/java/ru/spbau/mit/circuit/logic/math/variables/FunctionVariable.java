package ru.spbau.mit.circuit.logic.math.variables;


import ru.spbau.mit.circuit.logic.math.algebra.Numerical;
import ru.spbau.mit.circuit.logic.math.functions.Function;

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
