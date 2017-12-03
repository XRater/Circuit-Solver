package ru.spbau.mit.circuit.logic.system_solving.polynoms;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import ru.spbau.mit.circuit.logic.system_solving.exceptions.InvalidPolynomAdditionException;
import ru.spbau.mit.circuit.logic.system_solving.exceptions.InvalidPolynomException;

/**
 * Sorted container of Monoms<T>.
 * <p>
 * M1 + M2 + ... + Mk + constant
 *
 * @param <T> type of Monom
 */
public class Polynom<T extends Comparable<? super T>> implements Vector<Polynom<T>> {

    private final ArrayList<Monom<T>> monoms;
    private final int size;

    private double constant;

    public Polynom(Collection<Monom<T>> monoms) {
        this.monoms = new ArrayList<>(monoms);
        Collections.sort(this.monoms, this::compare);
        size = this.monoms.size();
        check();
    }

    public Polynom(Monom<T>[] monoms) {
        this.monoms = new ArrayList<>(Arrays.asList(monoms));
        Collections.sort(this.monoms, this::compare);
        size = this.monoms.size();
        check();
    }

    public Polynom(Collection<Monom<T>> monoms, int constant) {
        this(monoms);
        this.constant = constant;
    }

    public Polynom(Monom<T>[] monoms, int constant) {
        this(monoms);
        this.constant = constant;
    }

    private void check() {
        Iterator<Monom<T>> iterator = monoms.iterator();
        if (!iterator.hasNext()) {
            return;
        }
        Monom<T> last = iterator.next();
        while (iterator.hasNext()) {
            Monom<T> next = iterator.next();
            if (compare(last, next) >= 0) {
                throw new InvalidPolynomException();
            }
            last = next;
        }
    }

    public double constant() {
        return constant;
    }

    @Override
    public void add(Polynom<T> p) {
        Iterator<Monom<T>> our = monoms.iterator();
        Iterator<Monom<T>> their = p.monoms.iterator();
        while (our.hasNext() && their.hasNext()) {
            Monom<T> mo = our.next();
            Monom<T> mt = their.next();
            if (compare(mo, mt) != 0) {
                throw new InvalidPolynomAdditionException();
            }
            mo.add(mt);
        }
        if (our.hasNext() || their.hasNext()) {
            throw new InvalidPolynomAdditionException();
        }
        constant += p.constant;
    }

    public void addMonom(Monom<T> monom) {
        for (Monom<T> m : monoms) {
            if (compare(m, monom) == 0) {
                m.add(monom);
                return;
            }
        }
        throw new InvalidPolynomAdditionException();
    }

    public void addConst(double v) {
        constant += v;
    }

    @Override
    public void mul(double d) {
        for (Monom<T> m : monoms) {
            m.mul(d);
        }
        constant *= d;
    }

    @Override
    public double at(int index) {
        return monoms.get(index).coefficient();
    }

    @Override
    public int size() {
        return size;
    }

/*    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<Monom<T>> iterator = monoms.iterator();
        if (!iterator.hasNext()) {
            return "0";
        }
        sb.append(iterator.next());
        while (iterator.hasNext()) {
            Monom<T> next = iterator.next();
            if (next.coefficient() >= 0) {
                sb.append(" + ");
            } else {
                sb.append(" - ");
            }
            sb.append(next.getStringValue());
        }
        if (constant > 0) {
            sb.append(" + ").append(constant);
        } else if (constant < 0) {
            sb.append(" - ").append(-constant);
        }
        return sb.toString();
    }
*/

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Monom<T> m : monoms) {
            if (m.coefficient() > 0) {
                sb.append(" +").append(Math.round(Math.abs(m.coefficient())));
            }
            if (m.coefficient() == 0) {
                sb.append(" +").append(Math.round(Math.abs(m.coefficient())));
            }
            if (m.coefficient() < 0) {
                sb.append(" -").append(Math.round(Math.abs(m.coefficient())));
            }
        }
        return sb.toString();
    }

    public Monom<T> monomAt(int i) {
        return monoms.get(i);
    }

    private int compare(Monom<T> m1, Monom<T> m2) {
        return m1.value().compareTo(m2.value());
    }
}
