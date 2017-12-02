package ru.spbau.mit.circuit.logic.system_solving;


import ru.spbau.mit.circuit.logic.system_solving.polynoms.Linear;
import ru.spbau.mit.circuit.logic.system_solving.polynoms.Polynom;
import ru.spbau.mit.circuit.logic.system_solving.polynoms.Vector;

public class Equation<T extends Vector<T>, U extends Linear<U>> implements Vector<Equation<T, U>> {

    private final T coefficients;
    private final U constant;

    public Equation(T coefficients, U constant) {
        this.coefficients = coefficients;
        this.constant = constant;
    }

    @Override
    public void add(Equation<T, U> item) {
        coefficients.add(item.coefficients);
        constant.add(item.constant);
    }

    @Override
    public void mul(double d) {
        coefficients.mul(d);
        constant.mul(d);
    }

    @Override
    public double at(int index) {
        return coefficients.at(index);
    }

    @Override
    public int size() {
        return coefficients.size();
    }

    public T coefficients() {
        return coefficients;
    }

    public U constant() {
        return constant;
    }

//    @Override
//    public String toString() {
//        return coefficients.toString() + " = " + constant.toString();
//    }


    @Override
    public String toString() {
        return coefficients.toString() + " = " + ((Polynom) constant).constant();
    }
}
