package ru.spbau.mit.circuit.logic.gauss.functions1;


import java.util.Map;
import java.util.TreeMap;

import ru.spbau.mit.circuit.logic.gauss.algebra.Linear;
import ru.spbau.mit.circuit.logic.gauss.algebra.Numerical;

class PolyFunction implements Linear<Numerical, PolyFunction> {

    private Map<PrimaryFunction, PrimaryFunction> data = new TreeMap<>();

    @Override
    public PolyFunction add(PolyFunction item) {
        for (PrimaryFunction function : item.data.values()) {
            add(function);
        }
        return this;
    }

    private void add(PrimaryFunction function) {
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
    public PolyFunction mul(Numerical cf) {
        for (PrimaryFunction function : data.values()) {
            function.mul(cf);
        }
        return this;
    }

    public PolyFunction mul(PolyFunction other) {
        Map<PrimaryFunction, PrimaryFunction> old1 = new TreeMap<>();
        Map<PrimaryFunction, PrimaryFunction> old2 = new TreeMap<>();
        old1.putAll(data);
        old2.putAll(other.data);
        data.clear();
        for (PrimaryFunction f : old1.values()) {
            for (PrimaryFunction g : old2.values()) {
                PrimaryFunction fg = f.copy().mul(g);
                add(fg);
            }
        }
        return this;
    }

    public static void main(String[] args) {
        PolyFunction pf = new PolyFunction();
        pf.add(Functions.constant(1));
        pf.add(Functions.exponent(2));
        pf.add(Functions.power(2));
        pf.add(Functions.power(1));

        PolyFunction pf1 = new PolyFunction();
//        pf1.add(Functions.power(1));
//        pf1.add(Functions.power(2));
//        pf1.add(Functions.exponent(1));
        pf1.add(Functions.constant(1));

        System.out.println(pf);
        System.out.println(pf1);
        System.out.println("Result:");
        pf.mul(pf1);
        System.out.println(pf);
        System.out.println(pf1);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (PrimaryFunction f : data.values()) {
            sb.append(f).append(" ");
        }
        return sb.toString();
    }

    public PolyFunction copy() {
        PolyFunction ans = new PolyFunction();
        for (PrimaryFunction function : data.values()) {
            ans.add(function.copy());
        }
        return ans;
    }

    public boolean isZero() {
        return data.size() == 0;
    }
}
