package ru.spbau.mit.circuit.logic.matrix_exponent;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.FieldElement;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.BigReal;

import java.lang.reflect.Array;

public class Matrix<T extends FieldElement<T>> {

    private final T[][] data;
    private final int size;
    private final Field<T> field;

    public Matrix(int size, Field<T> field) {
        data = (T[][]) (Array.newInstance(field.getZero().getClass(), size, size));
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                data[i][j] = field.getZero();
            }
        }
        this.field = field;
        this.size = size;
    }

    // TODO
    public Matrix(RealMatrix matrix) {
        data = null;
        field = null;
        size = 0;
    }

    public Matrix(Matrix<T> matrix) {
        data = (T[][]) new Object[matrix.size][matrix.size];
        this.size = matrix.size;
        this.field = matrix.field;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
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

    public Matrix<T> getZero() {
        return new Matrix<T>(data.length, field);
    }

    public Matrix<T> getOne() {
        return Matrices.identity(data.length, field);
    }

    public Matrix<T> add(Matrix<T> matrix) {
        Matrix<T> ans = new Matrix<>(matrix.size, matrix.field);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                ans.set(i, j, get(i, j).add(matrix.get(i, j)));
            }
        }
        return ans;
    }

    public Matrix<T> multiply(T t) {
        Matrix<T> ans = new Matrix<>(this.size, this.field);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                ans.set(i, j, get(i, j).multiply(t));
            }
        }
        return ans;
    }

    public Matrix<T> multiply(Matrix<T> matrix) {
        Matrix<T> ans = new Matrix<>(matrix.size, matrix.field);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    ans.set(i, j, ans.get(i, j)
                            .add(data[i][k].multiply(matrix.data[k][j])));
                }
            }
        }
        return ans;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
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
        Matrix<BigReal> A = new Matrix<BigReal>(2, BigReal.ZERO.getField());
        A.set(0, 0, new BigReal(1));
        A.set(0, 1, new BigReal(1));
        A.set(1, 0, new BigReal(1));
        A.set(1, 1, new BigReal(1));
        System.out.println(A.multiply(A));
        //        Matrix<BigReal> B = new Matrix<BigReal>(2, BigReal.ZERO.getField());
    }
}
