package ru.spbau.mit.circuit.logic.math.variables;


import ru.spbau.mit.circuit.logic.math.algebra.Numerical;

/**
 * Class wrapper for numerical variables. For more convenience work with them.
 */
public class NumericalVariable extends Variable<Numerical> {

    @SuppressWarnings("unused")
    public NumericalVariable() {
        super();
    }

    public NumericalVariable(String name) {
        super(name);
    }
}
