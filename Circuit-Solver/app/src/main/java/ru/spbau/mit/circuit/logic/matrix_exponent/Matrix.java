package ru.spbau.mit.circuit.logic.matrix_exponent;

import java.lang.reflect.Array;

import ru.spbau.mit.circuit.logic.gauss.algebra.Field;
import ru.spbau.mit.circuit.logic.gauss.algebra.Linear;
import ru.spbau.mit.circuit.logic.gauss.algebra.Numerical;

public class Matrix<T extends Field<T>> implements Field<Matrix<T>>, Linear<T, Matrix<T>> {

    private final T[][] data;
    private final int n;
    private final int m;
    private final T zero;

    public Matrix(int n, T zero) {
        this(n, n, zero);
    }

    public Matrix(int n, int m, T zero) {
        this.zero = zero;
        this.n = n;
        this.m = m;
        data = (T[][]) (Array.newInstance(zero.getClass(), n, m));
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                data[i][j] = zero.getZero();
            }
        }
    }

    public Matrix(Matrix<T> matrix) {
        this.zero = matrix.zero;
        this.n = matrix.n;
        this.m = matrix.m;
        data = (T[][]) (Array.newInstance(zero.getClass(), n, m));
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                data[i][j] = matrix.data[i][j];
            }
        }
    }

    public void set(int i, int j, T t) {
        data[i][j] = t;
    }

    public T get(int i, int j) {
        return data[i][j];
    }

    @Override
    public Matrix<T> getZero() {
        return new Matrix<>(n, m, zero);
    }

    public Matrix<T> getZero(int sz) {
        return new Matrix<>(sz, zero);
    }

    @Override
    public Matrix<T> getIdentity() {
        if (n != m) {
            throw new MatrixException();
        }
        return Matrices.identity(n, zero);
    }

    public Matrix<T> getIdentity(int sz) {
        return Matrices.identity(sz, zero);
    }

    @Override
    public Matrix<T> add(Matrix<T> matrix) {
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

    @Override
    public Matrix<T> multiply(Matrix<T> matrix) {
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

    @Override
    public Matrix<T> negate() {
        return this.multiplyConstant(zero.getIdentity().negate());
    }

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

    public static void main(String[] args) {
        Matrix<Numerical> A = new Matrix<>(2, Numerical.zero());
        A.set(0, 0, Numerical.number(1));
        A.set(0, 1, Numerical.number(1));
        A.set(1, 0, Numerical.number(1));
        A.set(1, 1, Numerical.number(1));
        System.out.println(A.multiply(A));
        //        Matrix<BigReal> B = new Matrix<BigReal>(2, BigReal.ZERO.getField());
    }

    public int size() {
        if (n != m) {
            throw new IllegalArgumentException();
        }
        return n;
    }

    public int n() {
        return n;
    }

    public int m() {
        return m;
    }
}
