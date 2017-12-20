package ru.spbau.mit.circuit.logic.gauss;


import java.util.ArrayList;

import ru.spbau.mit.circuit.logic.gauss.algebra.Field;
import ru.spbau.mit.circuit.logic.gauss.algebra.Linear;
import ru.spbau.mit.circuit.logic.gauss.exceptions.IllegalEquationSizeException;
import ru.spbau.mit.circuit.logic.gauss.exceptions.ZeroDeterminantException;

public class LinearSystem<
        C extends Field<C>,
        T extends Gauss<C, T>,
        U extends Linear<C, U>
        > {

    private final ArrayList<Equation<C, T, U>> equations = new ArrayList<>();
    private final int eqSize;

    public LinearSystem(int eqSize) {
        this.eqSize = eqSize;
    }

    public int size() {
        return equations.size();
    }

    public void addEquation(Equation<C, T, U> eq) {
        if (eqSize != eq.size()) {
            throw new IllegalEquationSizeException();
        }
        equations.add(eq);
    }

    public Equation<C, T, U> get(int index) {
        return equations.get(index);
    }

    public C coefficient(int row, int col) {
        return equations.get(row).coefficientAt(col);
    }

    public void swap(int i, int j) {
        Equation<C, T, U> tmp = get(i);
        equations.set(i, get(j));
        equations.set(j, tmp);
    }

    public void solve() {
        zeroBottomPart();
        makeDiagonal();
    }

    private void zeroBottomPart() {
        for (int i = 0; i < size() - 1; i++) {
            for (int j = i + 1; j < size(); j++) {
                if (coefficient(j, i).isZero()) {
                    continue;
                }
                if (coefficient(i, i).isZero()) {
                    swap(i, j);
                    continue;
                }
                C k = getMulCoefficient(coefficient(i, i), coefficient(j, i));
                get(i).mul(k);
                get(j).add(get(i));
            }
        }
    }

    private void makeDiagonal() {
        for (int i = size() - 1; i >= 0; i--) {
            if (coefficient(i, i).isZero()) {
                throw new ZeroDeterminantException();
            }
            for (int j = i - 1; j >= 0; j--) {
                if (coefficient(j, i).isZero()) {
                    continue;
                }
                C k = getMulCoefficient(coefficient(i, i), coefficient(j, i));
                get(i).mul(k);
                get(j).add(get(i));
            }
            get(i).mul(coefficient(i, i).inverse());
        }
    }

    private C getMulCoefficient(C a, C b) {
        return b.div(a).negate();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Equation<C, T, U> eq : equations) {
            sb.append("| ").append(eq.toString()).append('\n');
        }
        return sb.toString();
    }
}
