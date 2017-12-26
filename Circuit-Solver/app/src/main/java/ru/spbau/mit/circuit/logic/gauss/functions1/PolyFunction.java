package ru.spbau.mit.circuit.logic.gauss.functions1;


import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import ru.spbau.mit.circuit.logic.gauss.algebra.Linear;
import ru.spbau.mit.circuit.logic.gauss.algebra.Numerical;
import ru.spbau.mit.circuit.logic.gauss.functions1.exceptions.IllegalDoubleConvertionException;

public class PolyFunction implements Linear<Numerical, PolyFunction> {

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
        PolyFunction result = copy();
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
            PolyExponent exponent = data.get(function);
            System.out.println(exponent + " " + function);
            data.put(function, data.get(function).add(function));
            if (data.get(function).isZero()) {
                data.remove(function);
            }
        } else {
            data.put(function, function);
        }
    }

    @Override
    public PolyFunction mul(Numerical cf) {
        PolyFunction result = new PolyFunction();
        for (PolyExponent function : data.values()) {
            result.add(function.mul(cf));
        }
        return result;
    }

    public PolyFunction mul(PolyFunction other) {
        PolyFunction result = new PolyFunction();
        for (PolyExponent f : data.values()) {
            for (PolyExponent g : other.data.values()) {
                result.add(f.mul(g));
            }
        }
        return result;
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

    public PolyFunction copy() {
        return new PolyFunction(this);
    }

    public boolean isZero() {
        return data.size() == 0;
    }

    public boolean isIdentity() {
        if (data.size() != 1) {
            return false;
        }
        return data.values().iterator().next().isIdentity();
    }

    private PolyFunction div(PolyExponent gcd) {
        PolyFunction answer = new PolyFunction();
        for (PolyExponent function : data.values()) {
            answer.add(function.div(gcd));
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

    public static void main(String[] args) {
        PolyFunction function = PolyFunctions.polyExponent(1, 2, 2);
        System.out.println(function);
        System.out.println(function.differentiate());
    }

}
