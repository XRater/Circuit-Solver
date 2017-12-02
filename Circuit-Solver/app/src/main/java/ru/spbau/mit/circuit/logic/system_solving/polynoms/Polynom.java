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

    private double constant;

    public Polynom(Collection<Monom> monoms) {
        this.monoms = new ArrayList<>(monoms);
        Collections.sort(this.monoms);
        size = this.monoms.size();
        check();
    }

    public Polynom(Monom[] monoms) {
        this.monoms = new ArrayList<>(Arrays.asList(monoms));
        Collections.sort(this.monoms);
        size = this.monoms.size();
        check();
    }

    public Polynom(Collection<Monom> monoms, int constant) {
        this(monoms);
        this.constant = constant;
    }

    public Polynom(Monom[] monoms, int constant) {
        this(monoms);
        this.constant = constant;
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

    public double constant() {
        return constant;
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
        constant += p.constant;
    }

    public void addMonom(Monom monom) {
        for (Monom m : monoms) {
            if (m.compareTo(monom) == 0) {
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
        for (Monom m : monoms) {
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
        for (Monom m : monoms) {
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

    public Monom monomAt(int i) {
        return monoms.get(i);
    }
}
