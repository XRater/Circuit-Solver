package ru.spbau.mit.circuit.logic.math.matrices;

import android.support.annotation.NonNull;

import java.lang.reflect.Array;

import ru.spbau.mit.circuit.logic.math.algebra.Field;
import ru.spbau.mit.circuit.logic.math.algebra.Linear;

/**
 * Base matrix class. May store any values, which implements field interface.
 *
 * @param <T> stored type.
 */
public class Matrix<T extends Field<T>> implements Field<Matrix<T>>, Linear<T, Matrix<T>> {

    @NonNull
    private final T[][] data;
    private final int n;
    private final int m;
    @NonNull
    private final T zero;

    @SuppressWarnings("WeakerAccess")
    public Matrix(int n, @NonNull T zero) {
        this(n, n, zero);
    }

    public Matrix(int n, int m, @NonNull T zero) {
        this.zero = zero;
        this.n = n;
        this.m = m;
        //noinspection unchecked
        data = (T[][]) (Array.newInstance(zero.getClass(), n, m));
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

    public void set(int i, int j, T t) {
        data[i][j] = t;
    }

    public T get(int i, int j) {
        return data[i][j];
    }

    @NonNull
    @Override
    public Matrix<T> getZero() {
        return new Matrix<>(n, m, zero);
    }

    @NonNull
    public Matrix<T> getZero(int sz) {
        return new Matrix<>(sz, zero);
    }

    @NonNull
    @Override
    public Matrix<T> getIdentity() {
        if (n != m) {
            throw new IllegalArgumentException();
        }
        return Matrices.identityMatix(n, zero);
    }

    @NonNull
    public Matrix<T> getIdentity(int sz) {
        return Matrices.identityMatix(sz, zero);
    }

    @NonNull
    @Override
    public Matrix<T> add(@NonNull Matrix<T> matrix) {
        if (n != matrix.n || m != matrix.m) {
            throw new IllegalArgumentException();
        }
        Matrix<T> ans = new Matrix<>(matrix.n, matrix.m, matrix.zero);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                ans.set(i, j, get(i, j).add(matrix.get(i, j)));
            }
        }
        return ans;
    }

    @NonNull
    @Override
    public Matrix<T> multiply(@NonNull Matrix<T> matrix) {
        if (m != matrix.n) {
            throw new IllegalArgumentException();
        }
        Matrix<T> ans = new Matrix<>(n, matrix.m, matrix.zero);
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

    @NonNull
    @Override
    public Matrix<T> negate() {
        return this.multiplyConstant(zero.getIdentity().negate());
    }

    @NonNull
    @Override
    public Matrix<T> reciprocal() {
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

    @NonNull
    @Override
    public Matrix<T> multiplyConstant(T t) {
        Matrix<T> ans = new Matrix<>(this.n, this.zero);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                ans.set(i, j, get(i, j).multiply(t));
            }
        }
        return ans;
    }

    @NonNull
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
