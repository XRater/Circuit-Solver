package ru.spbau.mit.circuit.logic.system_solving.polynoms;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import ru.spbau.mit.circuit.logic.system_solving.exceptions.InvalidPolynomAdditionException;
import ru.spbau.mit.circuit.logic.system_solving.exceptions.InvalidPolynomException;

public class Polynom implements Vector<Polynom> {

    private final ArrayList<Monom> monoms;
    private final int size;

    public Polynom(Collection<Monom> monoms) {
        this.monoms = new ArrayList<>(monoms);
        Collections.sort(this.monoms);
        size = monoms.size();
        check();
    }

    public Polynom(Monom[] monoms) {
        this.monoms = new ArrayList<>(Arrays.asList(monoms));
        Collections.sort(this.monoms);
        size = this.monoms.size();
        check();
    }

    private void check() {
        Iterator<Monom> iterator = monoms.iterator();
        if (!iterator.hasNext()) {
            return;
        }
        Monom last = iterator.next();
        while (iterator.hasNext()) {
            Monom next = iterator.next();
            if (last.compareTo(next) >= 0) {
                throw new InvalidPolynomException();
            }
            last = next;
        }
    }

    @Override
    public void add(Polynom p) {
        Iterator<Monom> our = monoms.iterator();
        Iterator<Monom> their = p.monoms.iterator();
        while (our.hasNext() && their.hasNext()) {
            Monom mo = our.next();
            Monom mt = their.next();
            if (mo.compareTo(mt) != 0) {
                throw new InvalidPolynomAdditionException();
            }
            mo.add(mt);
        }
        if (our.hasNext() || their.hasNext()) {
            throw new InvalidPolynomAdditionException();
        }
    }

    @Override
    public void mul(double d) {
        for (Monom m : monoms) {
            m.mul(d);
        }
    }

    @Override
    public double at(int index) {
        return monoms.get(index).coefficient();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<Monom> iterator = monoms.iterator();
        if (!iterator.hasNext()) {
            return "0";
        }
        sb.append(iterator.next());
        while (iterator.hasNext()) {
            Monom next = iterator.next();
            if (next.coefficient() >= 0) {
                sb.append(" + ");
            } else {
                sb.append(" - ");
            }
            sb.append(next.getStringValue());
        }
        return sb.toString();
    }
}
