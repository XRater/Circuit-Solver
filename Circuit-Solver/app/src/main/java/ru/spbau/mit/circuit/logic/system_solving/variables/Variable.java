package ru.spbau.mit.circuit.logic.system_solving.variables;


import android.support.annotation.NonNull;

import ru.spbau.mit.circuit.logic.system_solving.FunctionExpression;

public interface Variable extends Comparable<Variable> {

    FunctionExpression function();

    int id();

    @Override
    default int compareTo(@NonNull Variable o) {
        return id() - o.id();
    }

    void setFunction(FunctionExpression f);
}
