package ru.spbau.mit.circuit.logic.gauss.variables;


import ru.spbau.mit.circuit.logic.gauss.algebra.Numerical;

public class NumberVariable extends Variable<Numerical> {

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
