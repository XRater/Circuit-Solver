package ru.spbau.mit.circuit.logic.gauss.functions1;

//Row<PrimaryFunction, Linear1Constant>
@Deprecated
public class FunctionExpression {
/*
    private static final Linear1Constant lc = new Linear1Constant();
    private static Zero zero = new Zero();

    private FunctionExpression() {
        super(lc);
    }

    private FunctionExpression(PrimaryFunction function, double cf) {
        super(lc);
        add(function, cf);
    }

    public static Zero zero() {
        return zero;
    }

    public static FunctionExpression empty() {
        return new FunctionExpression();
    }

    public static FunctionExpression constant(double c) {
        return new FunctionExpression(new Constant(), c);
    }

    public static FunctionExpression exponent(double pow) {
        if (pow == 0) {
            return constant(1);
        }
        return new FunctionExpression(new Exponent(pow), 1);
    }

    public void add(FunctionExpression expression, double d) {
        for (Map.Entry<PrimaryFunction, Double> entry : expression.data.entrySet()) {
            add(entry.getKey(), entry.getValue() * d);
        }
    }

    public FunctionExpression integrate() {
        FunctionExpression expression = new FunctionExpression();
        for (Map.Entry<PrimaryFunction, Double> entry : data.entrySet()) {
            Pair<PrimaryFunction, Double> integral = entry.getKey().integrate();
            expression.add(integral.first(), entry.getValue() * integral.second());
        }
        return expression;
    }

    public FunctionExpression differentiate() {
        FunctionExpression expression = new FunctionExpression();
        for (Map.Entry<PrimaryFunction, Double> entry : data.entrySet()) {
            Pair<PrimaryFunction, Double> differential = entry.getKey().differentiate();
            expression.add(differential.first(), entry.getValue() * differential.second());
        }
        return expression;
    }

    public FunctionExpression mul(FunctionExpression exp) {
        FunctionExpression expression = new FunctionExpression();
        for (Map.Entry<PrimaryFunction, Double> entry1 : data.entrySet()) {
            for (Map.Entry<PrimaryFunction, Double> entry2 : exp.data.entrySet()) {
                expression.add(entry1.getKey().mul(entry2.getKey()), entry1.getValue() *
                        entry2.getValue());
            }
        }
        return expression;
    }

    private static class Zero implements PrimaryFunction {

        @Override
        public int rank() {
            return 100;
        }

        @Override
        public int compare(PrimaryFunction o) {
            if (o instanceof Zero) {
                return 0;
            }
            throw new IllegalFunctionCompareException();
        }

        @Override
        public Pair<PrimaryFunction, Double> integrate() {
            return new Pair<>(FunctionExpression.zero(), 0.0);
        }

        @Override
        public Pair<PrimaryFunction, Double> differentiate() {
            return new Pair<>(FunctionExpression.zero(), 0.0);
        }

        @Override
        public PrimaryFunction mul(PrimaryFunction function) {
            return FunctionExpression.zero();
        }

        @Override
        public String toString() {
            return "";
        }
    }
*/
}
