package ru.spbau.mit.circuit.logic.gauss.variables;


import ru.spbau.mit.circuit.logic.gauss.functions1.Function;

public class Derivative extends FunctionVariable {

    private final FunctionVariable parent;

    public Derivative(FunctionVariable parent) {
        this.parent = parent;
    }

    public FunctionVariable parent() {
        return parent;
    }

    public void setValue() {
//        value = parent.value;
        value = parent.value.differentiate();
//        throw new UnsupportedOperationException();
    }

    @Override
    public void setValue(Function f) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return parent.toString() + "'";
    }

    public static void main(String[] args) {
//        BigDecimal d = new BigDecimal(-21.227446049250313);
//        BigDecimal f = d.setScale(3, RoundingMode.HALF_EVEN);
//        f = f.multiply(f).setScale(3.);
//        System.out.println(f);
    }
}
