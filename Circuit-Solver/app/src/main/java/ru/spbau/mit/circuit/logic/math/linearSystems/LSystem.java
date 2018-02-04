package ru.spbau.mit.circuit.logic.math.linearSystems;


import android.support.annotation.NonNull;

import java.util.ArrayList;

import ru.spbau.mit.circuit.logic.math.algebra.interfaces.Abel;
import ru.spbau.mit.circuit.logic.math.algebra.interfaces.Field;
import ru.spbau.mit.circuit.logic.math.algebra.interfaces.Proportional;
import ru.spbau.mit.circuit.logic.math.linearContainers.FArray;
import ru.spbau.mit.circuit.logic.math.linearSystems.exceptions.ZeroDeterminantException;

public class LSystem<F extends Field<F>, U extends Abel<U> & Proportional<F, U>> {

    private final F fZero;
    private final U uZero;

    private final ArrayList<FArray<F>> data = new ArrayList<>();
    private final ArrayList<U> constants = new ArrayList<>();
//    private final ArrayList<? extends Variable<U>> variables;

    private final ArrayList<Integer> columns = new ArrayList<>();

    private final int variablesNumber;
    private int equationsNumber;

    public LSystem(int n, @NonNull F f, @NonNull U u) {
        variablesNumber = n;
        equationsNumber = 0;
        fZero = f.getZero();
        uZero = u.getZero();
    }

    public int variablesNumber() {
        return variablesNumber;
    }

    public void addEquation(@NonNull FArray<F> coefficients, @NonNull U constant) throws
            ZeroDeterminantException {
        if (coefficients.size() != variablesNumber) {
            throw new IllegalArgumentException();
        }
        equationsNumber++;
        data.add(coefficients);
        constants.add(constant);

        for (int i = 0; i < columns.size(); i++) {
            reduce(equationsNumber - 1, i, columns.get(i));
        }

        for (int i = 0; i < variablesNumber; i++) {
            if (!get(equationsNumber - 1, i).isZero()) {
                columns.add(i);
                return;
            }
        }

        if (!constants.get(equationsNumber - 1).isZero()) {
            data.remove(equationsNumber - 1);
            constants.remove(equationsNumber - 1);
            throw new ZeroDeterminantException();
        }
        data.remove(equationsNumber - 1);
        constants.remove(equationsNumber - 1);
        equationsNumber--;
        // already in the system
    }

    @NonNull
    public ArrayList<U> getSolution() {
        for (int i = 0; i < variablesNumber; i++) {
            if (!columns.contains(i)) {
                FArray<F> baseVector = FArray.array(variablesNumber, fZero);
                for (int j = 0; j < variablesNumber; j++) {
                    if (i != j) {
                        baseVector.set(j, fZero);
                    } else {
                        baseVector.set(j, fZero.getIdentity());
                    }
                }
                columns.add(i);
                data.add(baseVector);
                constants.add(uZero);
            }
        }
        if (data.size() != variablesNumber || columns.size() != variablesNumber) {
            throw new RuntimeException();
        }

        ArrayList<U> solution = new ArrayList<>();
        for (int i = 0; i < variablesNumber(); i++) {
            solution.add(null);
        }
        for (int i = 0; i < variablesNumber; i++) {
            for (int j = 0; j < variablesNumber; j++) {
                if (i == j) {
                    continue;
                }
                reduce(i, j, columns.get(j));
            }
            int col = columns.get(i);
            F k = get(i, col).reciprocal();
            solution.set(col, get(i).multiplyConstant(k));
        }
        return solution;
    }

    @SuppressWarnings("unused")
    private void set(int i, int j, F f) {
        data.get(i).set(j, f);
    }

    private F get(int i, int j) {
        return data.get(i).get(j);
    }

    private void set(int i, U u) {
        constants.set(i, u);
    }

    private U get(int i) {
        return constants.get(i);
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private void reduce(int x, int y, int column) {
        if (get(x, column).isZero()) {
            return;
        }
        F k = get(x, column).divide(get(y, column)).negate();
        mul(y, k);
        add(x, y);
        mul(y, k.reciprocal());
    }

    private void add(int x, int y) {
        data.get(x).add(data.get(y));
        set(x, constants.get(x).add(constants.get(y)));
    }

    private void mul(int x, F f) {
        data.get(x).multiplyConstant(f);
        set(x, constants.get(x).multiplyConstant(f));
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < equationsNumber; i++) {
            for (int j = 0; j < variablesNumber; j++) {
                sb.append(get(i, j).toString()).append(" | ");
            }
            sb.append("=").append(constants.get(i).toString());
            sb.append('\n');
        }
        return sb.toString();
    }
}
