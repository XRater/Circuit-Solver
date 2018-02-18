package ru.spbau.mit.circuit.logic.math.linearSystems;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import ru.spbau.mit.circuit.logic.math.algebra.Field;
import ru.spbau.mit.circuit.logic.math.algebra.Linear;
import ru.spbau.mit.circuit.logic.math.linearSystems.exceptions.IllegalEquationSizeException;
import ru.spbau.mit.circuit.logic.math.linearSystems.exceptions.ZeroDeterminantException;


/**
 * Linear system. Has one public method, which represents Gauss algorithm.
 *
 * @param <C> type of coefficients
 * @param <T> type of the left side of equation
 * @param <U> type of the right side of the equation
 */
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

    public void addEquation(@NonNull Equation<C, T, U> eq) {
        if (eqSize != eq.size()) {
            throw new IllegalEquationSizeException();
        }
        equations.add(eq);
    }

    public Equation<C, T, U> get(int index) {
        return equations.get(index);
    }

    @SuppressWarnings("WeakerAccess")
    public C coefficient(int row, int col) {
        return equations.get(row).coefficientAt(col);
    }

    /**
     * This method decomposes left side matrix to the diagonal view.
     * <p>
     * This method may change initial system (works in place).
     *
     * @throws ZeroDeterminantException if matrix was singular.
     */
    public void solve() throws ZeroDeterminantException {
        zeroBottomPart();
        makeDiagonal();
    }

    private void swap(int i, int j) {
        Equation<C, T, U> tmp = get(i);
        equations.set(i, get(j));
        equations.set(j, tmp);
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
                get(i).multiplyConstant(k);
                get(j).add(get(i));
            }
        }
    }

    private void makeDiagonal() throws ZeroDeterminantException {
        for (int i = size() - 1; i >= 0; i--) {
            if (coefficient(i, i).isZero()) {
                throw new ZeroDeterminantException();
            }
            for (int j = i - 1; j >= 0; j--) {
                if (coefficient(j, i).isZero()) {
                    continue;
                }
                C k = getMulCoefficient(coefficient(i, i), coefficient(j, i));
                get(i).multiplyConstant(k);
                get(j).add(get(i));
            }
            get(i).multiplyConstant(coefficient(i, i).reciprocal());
        }
    }

    private C getMulCoefficient(@NonNull C a, @NonNull C b) {
        return b.divide(a).negate();
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Equation<C, T, U> eq : equations) {
            sb.append("| ").append(eq.toString()).append('\n');
        }
        return sb.toString();
    }
}
