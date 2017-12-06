package ru.spbau.mit.circuit.logic.system_solving.functions;


import ru.spbau.mit.circuit.logic.system_solving.functions.exceptions
        .IllegalFunctionCompareException;

class Identity implements PrimaryFunction {

    private static final int rank = 10;


    @Override
    public int rank() {
        return rank;
    }

    @Override
    public int compare(PrimaryFunction o) {
        if (o instanceof Identity) {
            return 0;
        }
        throw new IllegalFunctionCompareException();
    }

    @Override
    public String toString() {
        return "I";
    }
}
