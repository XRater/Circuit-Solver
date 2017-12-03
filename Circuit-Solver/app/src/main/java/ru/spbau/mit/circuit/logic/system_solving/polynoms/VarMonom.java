package ru.spbau.mit.circuit.logic.system_solving.polynoms;


import ru.spbau.mit.circuit.logic.system_solving.exceptions.InvalidMonomAdditionException;
import ru.spbau.mit.circuit.logic.system_solving.variables.FunctionVariable;

public class VarMonom implements Monom<FunctionVariable> {

    private double cf;

    private final FunctionVariable var;

    public VarMonom(FunctionVariable variable) {
        var = variable;
    }

    public VarMonom(FunctionVariable variable, double coefficient) {
        this.var = variable;
        cf = coefficient;
    }

    @Override
    public FunctionVariable value() {
        return var;
    }

    @Override
    public double coefficient() {
        return cf;
    }

    @Override
    public void add(final Monom<FunctionVariable> m) {
        if (!m.value().equals(var)) {
            System.err.println(m);
            System.err.println(this);
            throw new InvalidMonomAdditionException();
        }
        cf += m.coefficient();
    }

    @Override
    public void mul(double d) {
        cf *= d;
    }

//    @Override
//    public int compareTo(@NonNull Monom<Variable> o) {
//        return var.compareTo(o.value());
//    }

    @Override
    public String toString() {
        return cf + var.toString();
    }

    public String getStringValue() {
        return Math.abs(cf) + var.toString();
    }
}
