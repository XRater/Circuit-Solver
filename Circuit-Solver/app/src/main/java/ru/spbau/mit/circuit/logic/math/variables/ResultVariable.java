package ru.spbau.mit.circuit.logic.math.variables;

import ru.spbau.mit.circuit.model.Result;

public class ResultVariable extends Variable<Result> {

    private Result initialValue;

    public ResultVariable() {
        super();
    }

    @SuppressWarnings("unused")
    public ResultVariable(String name) {
        super(name);
    }

    public Result initialValue() {
        return initialValue;
    }

    public void setInitialValue(Result initialValue) {
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
