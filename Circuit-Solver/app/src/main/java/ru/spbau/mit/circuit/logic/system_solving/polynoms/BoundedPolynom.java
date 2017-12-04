package ru.spbau.mit.circuit.logic.system_solving.polynoms;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import ru.spbau.mit.circuit.logic.system_solving.exceptions.InvalidPolynomAdditionException;

/**
 * Sorted container of Monoms<T>.
 * <p>
 * M1 + M2 + ... + Mk + constant
 *
 * @param <T> type of Monom
 */
public class BoundedPolynom<T extends Comparable<? super T>> implements Gauss<BoundedPolynom<T>> {

    private final ArrayList<Monom<T>> monoms;
    private final int size;

    public BoundedPolynom(Polynom<T> polynom) {
        this.monoms = new ArrayList<>();
        for (Monom<T> monom : polynom.monoms()) {
            monoms.add(new Monom<T>(monom.value(), monom.coefficient()));
        }
        size = monoms.size();
    }

    public BoundedPolynom(Collection<Monom<T>> monoms) {
        this(new Polynom<>(monoms));
    }

    public BoundedPolynom(Monom<T>[] monoms) {
        this(new Polynom<>(monoms));
    }

    @Override
    public void add(BoundedPolynom<T> p) {
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

    @Override
    public void mul(double d) {
        for (Monom<T> m : monoms) {
            m.mul(d);
        }
    }

    @Override
    public double coefficientAt(int index) {
        return monoms.get(index).coefficient();
    }

    @Override
    public int size() {
        return size;
    }

    public Monom<T> at(int i) {
        return monoms.get(i);
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

    private int compare(Monom<T> m1, Monom<T> m2) {
        return m1.value().compareTo(m2.value());
    }
}
