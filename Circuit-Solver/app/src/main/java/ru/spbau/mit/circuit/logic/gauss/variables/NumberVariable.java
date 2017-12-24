package ru.spbau.mit.circuit.logic.gauss.variables;


import org.apache.commons.math3.util.BigReal;

//import ru.spbau.mit.circuit.logic.gauss.algebra.Numerical;

public class NumberVariable extends Variable<BigReal> {

    public NumberVariable() {
        super();
    }

    public NumberVariable(String name) {
        super(name);
    }

    @Override
    public String toString() {
        if (name.equals("")) {
            return "x" + id;
        }
        return name;
    }
}
