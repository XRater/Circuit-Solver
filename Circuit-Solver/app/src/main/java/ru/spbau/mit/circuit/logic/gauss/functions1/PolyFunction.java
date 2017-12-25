package ru.spbau.mit.circuit.logic.gauss.functions1;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import ru.spbau.mit.circuit.logic.gauss.algebra.Field;
import ru.spbau.mit.circuit.logic.gauss.algebra.Linear;
import ru.spbau.mit.circuit.logic.gauss.algebra.Numerical;
import ru.spbau.mit.circuit.logic.gauss.functions1.exceptions.IllegalDoubleConvertionException;

public class PolyFunction implements Field<PolyFunction>, Linear<Numerical, PolyFunction> {

    private final Map<PolyExponent, PolyExponent> data = new TreeMap<>();

    private PolyFunction() {
    }

    PolyFunction(PolyExponent f) {
        add(f);
    }

    public PolyFunction(PolyFunction function) {
        for (PolyExponent f : function.data.values()) {
            add(f);
        }
    }

    @Override
    public PolyFunction add(PolyFunction item) {
        PolyFunction result = new PolyFunction(this);
        for (PolyExponent function : item.data.values()) {
            result.add(function);
        }
        return result;
    }

    private void add(PolyExponent function) {
        if (function.isZero()) {
            return;
        }
        if (data.containsKey(function)) {
            data.put(function, data.get(function).add(function));
            if (data.get(function).isZero()) {
                data.remove(function);
            }
        } else {
            data.put(function, function);
        }
    }

    @Override
    public PolyFunction multiply(PolyFunction other) {
        PolyFunction result = new PolyFunction();
        for (PolyExponent f : data.values()) {
            for (PolyExponent g : other.data.values()) {
                result.add(f.multiply(g));
            }
        }
        return result;
    }

    @Override
    public PolyFunction negate() {
        return this.multiplyConstant(Numerical.number(-1));
    }

    @Override
    public PolyFunction reciprocal() {
        throw new UnsupportedOperationException();
    }

    @Override
    public PolyFunction multiplyConstant(Numerical cf) {
        PolyFunction result = new PolyFunction();
        for (PolyExponent function : data.values()) {
            result.add(function.multiplyConstant(cf));
        }
        return result;
    }

    @Override
    public PolyFunction getZero() {
        return PolyFunctions.constant(0);
    }

    @Override
    public PolyFunction getIdentity() {
        return PolyFunctions.identity();
    }

    @Override
    public boolean isZero() {
        return data.size() == 0;
    }

    @Override
    public boolean isIdentity() {
        if (data.size() != 1) {
            return false;
        }
        return data.values().iterator().next().isIdentity();
    }

    private PolyFunction div(PolyExponent gcd) {
        PolyFunction answer = new PolyFunction();
        for (PolyExponent function : data.values()) {
            answer.add(function.divide(gcd));
        }
        return answer;
    }

    PolyFunction div(PolyFunction function) {
        if (function.data.size() == 1) {
            return div(function.data.values().iterator().next());
        }
        return this;
    }

    public PolyFunction differentiate() {
        PolyFunction ans = new PolyFunction();
        for (PolyExponent exponent : data.values()) {
            ans.add(new PolyExponent(
                    exponent.cf() * exponent.mPow(), exponent.mPow() - 1, exponent.ePow()));
            ans.add(new PolyExponent(
                    exponent.cf() * exponent.ePow().doubleValue(), exponent.mPow(), exponent.ePow
                    ()));
        }
        return ans;
    }

    public double doubleValue() {
        if (data.size() == 0) {
            return 0;
        }
        if (data.size() != 1) {
            throw new IllegalDoubleConvertionException();
        }
        for (PolyExponent exponent : data.values()) {
            return exponent.doubleValue();
        }
        throw new RuntimeException();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (data.values().isEmpty()) {
            return "0";
        }
        Iterator<PolyExponent> iter = data.values().iterator();
        sb.append(iter.next().toString());
        while (iter.hasNext()) {
            sb.append(" ").append(iter.next().toString());
        }
        return sb.toString();
    }

    public PolyFunction integrate() {
        PolyFunction answer = new PolyFunction();

        for (PolyExponent exponent : data.values()) {
            answer = answer.add(exponent.integrate());
        }

        return answer;
    }
}
