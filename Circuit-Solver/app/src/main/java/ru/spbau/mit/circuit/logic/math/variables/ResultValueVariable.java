package ru.spbau.mit.circuit.logic.math.variables;


import ru.spbau.mit.circuit.logic.math.ResultValue;

/**
 * Function variable. May have any Numerical initial value.
 */
public class ResultValueVariable extends Variable<ResultValue> {

    private ResultValue initialValue;

    public ResultValueVariable() {
        super();
    }

    @SuppressWarnings("unused")
    public ResultValueVariable(String name) {
        super(name);
    }

    public ResultValue initialValue() {
        return initialValue;
    }

    public void setInitialValue(ResultValue initialValue) {
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
