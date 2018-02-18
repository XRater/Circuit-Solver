package ru.spbau.mit.circuit.logic.math.functions;

import ru.spbau.mit.circuit.logic.math.algebra.Numerical;
import ru.spbau.mit.circuit.logic.math.algebra.Pair;
import ru.spbau.mit.circuit.logic.math.algebra.PolyElement;
import ru.spbau.mit.circuit.logic.math.functions.exceptions.IllegalDoubleConvertionException;

import static ru.spbau.mit.circuit.logic.math.algebra.Pair.pair;

class PolyFunction extends PolyElement<Numerical, PolyExponent, PolyFunction> {

    PolyFunction() {
    }

    private PolyFunction(PolyExponent e) {
        data.put(e, pair(Numerical.identity(), e));
    }

    @Override
    protected PolyFunction empty() {
        return new PolyFunction();
    }

    @Override
    protected PolyFunction single() {
        return new PolyFunction(PolyExponent.identity());
    }

    @Override
    public PolyFunction getZero() {
        return PolyFunctions.zero();
    }

    @Override
    public PolyFunction getIdentity() {
        return PolyFunctions.identity();
    }

    PolyFunction differentiate() {
        PolyFunction answer = empty();

        for (Pair<Numerical, PolyExponent> pair : data.values()) {
            Numerical cf = pair.first();
            PolyExponent exponent = pair.second();
            answer.add(cf.multiply(Numerical.number(exponent.exponent())),
                    PolyExponent.exponent(exponent.power(), exponent.exponent()));
            answer.add(cf.multiply(Numerical.number(exponent.power())),
                    PolyExponent.exponent(exponent.power() - 1, exponent.exponent()));
        }

        return answer;
    }

    PolyFunction integrate() {
        PolyFunction answer = empty();

        for (Pair<Numerical, PolyExponent> pair : data.values()) {
            Numerical cf = pair.first();
            PolyExponent exponent = pair.second();
            if (exponent.isIdentity()) {
                answer.add(cf, PolyExponent.exponent(1, 0));
                continue;
            }
            if (exponent.isExponent()) {
                answer.add(cf.divide(Numerical.number(exponent.exponent())),
                        PolyExponent.exponent(exponent.power(), exponent.exponent()));
            } else {
                if (exponent.isPower()) {
                    if (exponent.power() == -1) {
                        throw new UnsupportedOperationException();
                    }
                    answer.add(cf.divide(Numerical.number(exponent.power() + 1)),
                            PolyExponent.exponent(exponent.power() + 1, exponent.exponent()));
                } else {
                    throw new UnsupportedOperationException();
                }
            }
        }

        return answer;
    }

    Numerical apply(double x) {
        Numerical answer = Numerical.zero();
        for (Pair<Numerical, PolyExponent> pair : data.values()) {
            answer = answer.add(pair.first().multiply(pair.second().apply(x)));
        }
        return answer;
    }

    double doubleValue() {
        if (data.size() == 0) {
            return 0;
        }
        if (data.size() != 1) {
            throw new IllegalDoubleConvertionException();
        }
        return data.values().iterator().next().first().value();
    }

    PolyExponent front() {
        return data.keySet().iterator().next();
    }
}
