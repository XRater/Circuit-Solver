package ru.spbau.mit.circuit.logic.math.variables;


import ru.spbau.mit.circuit.logic.math.algebra.Numerical;
import ru.spbau.mit.circuit.logic.math.functions.Function;

/**
 * Function variable. May have any Numerical initial value.
 */
public class FunctionVariable extends Variable<Function> {

    private Numerical initialValue;

    public FunctionVariable() {
        super();
    }

    @SuppressWarnings("unused")
    public FunctionVariable(String name) {
        super(name);
    }

    @SuppressWarnings("unused")
    public Numerical initialValue() {
        return initialValue;
    }

    public void setInitialValue(Numerical initialValue) {
        this.initialValue = initialValue;
    }

    @Override
    public String toString() {
        if (name.equals("")) {
            return "f" + id;
        }
        return name;
    }
}
