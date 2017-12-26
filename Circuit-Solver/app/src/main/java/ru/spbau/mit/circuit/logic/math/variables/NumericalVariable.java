package ru.spbau.mit.circuit.logic.math.variables;


import ru.spbau.mit.circuit.logic.math.algebra.Numerical;

//import ru.spbau.mit.circuit.logic.math.algebra.Numerical;

public class NumericalVariable extends Variable<Numerical> {

    public NumericalVariable() {
        super();
    }

    public NumericalVariable(String name) {
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
