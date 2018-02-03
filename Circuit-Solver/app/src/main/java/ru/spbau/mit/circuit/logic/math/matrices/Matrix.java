package ru.spbau.mit.circuit.logic.math.matrices;

import java.lang.reflect.Array;

import ru.spbau.mit.circuit.logic.math.algebra.interfaces.Algebra;
import ru.spbau.mit.circuit.logic.math.algebra.interfaces.Field;

/**
 * Base matrix class. May store any values, which implements field interface.
 *
 * @param <F> stored type.
 */
public class Matrix<F extends Field<F>> implements Algebra<F, Matrix<F>> {

    private final F[][] data;
    private final int n;
    private final int m;
    private final F zero;

    @SuppressWarnings("WeakerAccess")
    public Matrix(int n, F zero) {
        this(n, n, zero);
    }

    public Matrix(int n, int m, F zero) {
        this.zero = zero;
        this.n = n;
        this.m = m;
        //noinspection unchecked
        data = (F[][]) (Array.newInstance(zero.getClass(), n, m));
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                data[i][j] = zero.getZero();
            }
        }
    }

    @SuppressWarnings("WeakerAccess")
    public int n() {
        return n;
    }

    @SuppressWarnings("WeakerAccess")
    public int m() {
        return m;
    }

    public int size() {
        if (n != m) {
            throw new IllegalArgumentException();
        }
        return n;
    }

    public void set(int i, int j, F f) {
        data[i][j] = f;
    }

    public F get(int i, int j) {
        return data[i][j];
    }

    @Override
    public Matrix<F> getZero() {
        return new Matrix<>(n, m, zero);
    }

    public Matrix<F> getZero(int sz) {
        return new Matrix<>(sz, zero);
    }

    @Override
    public Matrix<F> getIdentity() {
        if (n != m) {
            throw new IllegalArgumentException();
        }
        return Matrices.identityMatix(n, zero);
    }

    public Matrix<F> getIdentity(int sz) {
        return Matrices.identityMatix(sz, zero);
    }

    @Override
    public Matrix<F> add(Matrix<F> matrix) {
        if (n != matrix.n || m != matrix.m) {
            throw new IllegalArgumentException();
        }
        Matrix<F> ans = new Matrix<>(matrix.n, matrix.m, matrix.zero);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                ans.set(i, j, get(i, j).add(matrix.get(i, j)));
            }
        }
        return ans;
    }

    @Override
    public Matrix<F> multiply(Matrix<F> matrix) {
        if (m != matrix.n) {
            throw new IllegalArgumentException();
        }
        Matrix<F> ans = new Matrix<>(n, matrix.m, matrix.zero);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < matrix.m; j++) {
                for (int k = 0; k < m; k++) {
                    ans.set(i, j, ans.get(i, j)
                            .add(data[i][k].multiply(matrix.data[k][j])));
                }
            }
        }
        return ans;
    }

    @Override
    public Matrix<F> negate() {
        return this.multiplyConstant(zero.getIdentity().negate());
    }

    @Override
    public Matrix<F> reciprocal() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isZero() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (!data[i][j].isZero()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean isIdentity() {
        if (n != m) {
            return false;
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (i != j) {
                    if (!data[i][j].isZero()) {
                        return false;
                    }
                } else {
                    if (!data[i][j].isIdentity()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public Matrix<F> multiplyConstant(F f) {
        Matrix<F> ans = new Matrix<>(this.n, this.zero);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                ans.set(i, j, get(i, j).multiply(f));
            }
        }
        return ans;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (j != 0) {
                    sb.append(' ');
                }
                sb.append(data[i][j].toString());
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
