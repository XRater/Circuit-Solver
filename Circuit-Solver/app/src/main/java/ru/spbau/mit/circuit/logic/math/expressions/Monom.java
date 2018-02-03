package ru.spbau.mit.circuit.logic.math.expressions;

import android.support.annotation.NonNull;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import ru.spbau.mit.circuit.logic.math.algebra.interfaces.OrderedGroup;

class Monom implements OrderedGroup<Monom> {

    private static final Monom identity = identity();

    public static Monom identity() {
        return new Monom();
    }

    public static Monom monom(String name) {
        return new Monom(new Var(name));
    }

    private static double precision = 0.0000001;

    static Monom gcd(Monom a, Monom b) {
        Monom m = new Monom();
        for (Map.Entry<Var, Integer> var : a.data.entrySet()) {
            Var key = var.getKey();
            Integer value = var.getValue();
            if (b.data.containsKey(key)) {
                m.addVar(key, Math.min(value, b.data.get(key)));
            }
        }
        return m;
    }

    private Map<Var, Integer> data = new TreeMap<>();

    private Monom() {

    }

    private Monom(Var var) {
        data.put(var, 1);
    }

    private Monom(Map<Var, Integer> data) {
        this.data.putAll(data);
    }

    private void addVar(Var key, int p) {
        if (data.containsKey(key)) {
            throw new RuntimeException();
        }
        data.put(key, p);
    }

    @Override
    public int compareTo(@NonNull Monom o) {
        Iterator<Map.Entry<Var, Integer>> iter1 = data.entrySet().iterator();
        Iterator<Map.Entry<Var, Integer>> iter2 = o.data.entrySet().iterator();
        while (iter1.hasNext() && iter2.hasNext()) {
            Map.Entry<Var, Integer> entry1 = iter1.next();
            Map.Entry<Var, Integer> entry2 = iter2.next();
            int cmp = entry1.getKey().name().compareTo(entry2.getKey().name());
            if (cmp != 0) {
                return cmp;
            }
            cmp = entry1.getValue().compareTo(entry2.getValue());
            if (cmp != 0) {
                return -cmp;
            }
        }
        if (iter1.hasNext()) {
            return -1;
        }
        if (iter2.hasNext()) {
            return 1;
        }
        return 0;
    }

    @Override
    public Monom multiply(Monom m) {
        Map<Var, Integer> vars = new TreeMap<>();
        vars.putAll(data);
        for (Map.Entry<Var, Integer> var : m.data.entrySet()) {
            Var key = var.getKey();
            Integer pow = var.getValue();
            if (vars.containsKey(key)) {
                pow += data.get(key);
            }
            vars.put(key, pow);
            if (pow == 0) {
                vars.remove(key);
            }
        }
        return new Monom(vars);
    }

    @Override
    public Monom reciprocal() {
        Monom answer = new Monom();
        for (Map.Entry<Var, Integer> entry : data.entrySet()) {
            answer.addVar(entry.getKey(), -entry.getValue());
        }
        return answer;
    }

    @Override
    public boolean isIdentity() {
        return data.size() == 0;
    }

    @Override
    public Monom getIdentity() {
        return identity;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Var, Integer> var : data.entrySet()) {
            sb.append(var.getKey()).append("^").append(var.getValue());
        }
        return sb.toString();
    }
}
