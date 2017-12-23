package ru.spbau.mit.circuit.logic.gauss;

import org.apache.commons.math3.FieldElement;

import java.util.ArrayList;

import ru.spbau.mit.circuit.logic.gauss.algebra.Linear;
import ru.spbau.mit.circuit.logic.gauss.exceptions.IllegalEquationSizeException;
import ru.spbau.mit.circuit.logic.gauss.exceptions.ZeroDeterminantException;


/**
 * @param <C> type of coefficients
 * @param <T> type of right side
 * @param <U> type of left side
 */
public class LinearSystem<
        C extends FieldElement<C>,
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
        FieldElement<C> zero = coefficient(0, 0).getField().getZero();
        for (int i = 0; i < size() - 1; i++) {
            for (int j = i + 1; j < size(); j++) {
                if (coefficient(j, i).equals(zero)) {
                    continue;
                }
                if (coefficient(i, i).equals(zero)) {
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
        FieldElement<C> zero = coefficient(0, 0).getField().getZero();
        for (int i = size() - 1; i >= 0; i--) {
            if (coefficient(i, i).equals(zero)) {
                throw new ZeroDeterminantException();
            }
            for (int j = i - 1; j >= 0; j--) {
                if (coefficient(j, i).equals(zero)) {
                    continue;
                }
                C k = getMulCoefficient(coefficient(i, i), coefficient(j, i));
                get(i).mul(k);
                get(j).add(get(i));
            }
            get(i).mul(coefficient(i, i).reciprocal());
        }
    }

    private C getMulCoefficient(C a, C b) {
        return b.divide(a).negate();
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
