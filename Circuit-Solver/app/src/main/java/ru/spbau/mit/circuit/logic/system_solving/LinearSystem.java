package ru.spbau.mit.circuit.logic.system_solving;


import java.util.ArrayList;

import ru.spbau.mit.circuit.logic.system_solving.exceptions.IllegalEquationSizeException;
import ru.spbau.mit.circuit.logic.system_solving.exceptions.ZeroDeterminantException;
import ru.spbau.mit.circuit.logic.system_solving.polynoms.Linear;
import ru.spbau.mit.circuit.logic.system_solving.polynoms.Vector;

public class LinearSystem<T extends Vector<T>, U extends Linear<U>> {

    private final ArrayList<Equation<T, U>> equations = new ArrayList<>();
    private final int eqSize;

    public LinearSystem(int eqSize) {
        this.eqSize = eqSize;
    }

    public void addEquation(Equation<T, U> eq) {
        if (eqSize != eq.size()) {
            throw new IllegalEquationSizeException();
        }
        equations.add(eq);
    }

    public Equation<T, U> get(int index) {
        return equations.get(index);
    }

    public double coefficient(int row, int col) {
        return equations.get(row).at(col);
    }

    public int size() {
        return equations.size();
    }

    public void swap(int i, int j) {
        Equation<T, U> tmp = get(i);
        equations.set(i, get(j));
        equations.set(j, tmp);
    }

    public void solve() {
        zeroBottomPart();
        normalize();
    }

    private void zeroBottomPart() {
        for (int i = 0; i < size() - 1; i++) {
            for (int j = i + 1; j < size(); j++) {
                if (coefficient(i, j) == 0) {
                    continue;
                }
                if (coefficient(i, i) == 0) {
                    swap(i, j);
                    continue;
                }
                double k = getMulCoefficient(coefficient(i, i), coefficient(j, i));
                get(i).mul(k);
                get(j).add(get(i));
            }
        }
    }

    private void normalize() {
        for (int i = size() - 1; i >= 0; i--) {
            if (coefficient(i, i) == 0) {
                throw new ZeroDeterminantException();
            }
            for (int j = i - 1; j >= 0; j--) {
                double k = getMulCoefficient(coefficient(i, i), coefficient(j, i));
                get(i).mul(k);
                get(j).add(get(i));
            }
            get(i).mul(1 / coefficient(i, i));
        }
    }

    /**
     * Returns coefficient k such that ka + b = 0.
     */
    private double getMulCoefficient(double a, double b) {
        return -b / a;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Equation<T, U> eq : equations) {
            sb.append("| ").append(eq.toString()).append('\n');
        }
        return sb.toString();
    }
}
