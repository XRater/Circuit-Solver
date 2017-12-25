package ru.spbau.mit.circuit.logic.matrix_exponent;


import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import ru.spbau.mit.circuit.logic.gauss.algebra.Field;
import ru.spbau.mit.circuit.logic.gauss.algebra.Numerical;
import ru.spbau.mit.circuit.logic.gauss.functions1.Function;
import ru.spbau.mit.circuit.logic.gauss.functions1.Functions;

public class Matrices {

    private static Numerical numericalZero = Numerical.zero();
    private static Function functionZero = Functions.zero();

    public static <T extends Field<T>> Matrix<T> identity(int size, T zero) {
        Matrix<T> ans = new Matrix<T>(size, zero);
        for (int i = 0; i < size; i++) {
            ans.set(i, i, zero.getIdentity());
        }
        return ans;
    }

    public static Matrix<Function> getPolynomMatrix(RealMatrix matrix) {
        if (!matrix.isSquare()) {
            throw new IllegalArgumentException();
        }

        Matrix<Function> ans =
                new Matrix<>(matrix.getRowDimension(), functionZero);

        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getRowDimension(); j++) {
                ans.set(i, j, Functions.power(matrix.getEntry(i, j), 1));
            }
        }

        return ans;
    }

    public static Matrix<Function> getFunctionMatrix(RealMatrix matrix) {
        if (!matrix.isSquare()) {
            throw new IllegalArgumentException();
        }

        Matrix<Function> ans =
                new Matrix<>(matrix.getRowDimension(), functionZero);

        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getRowDimension(); j++) {
                ans.set(i, j, Functions.constant(matrix.getEntry(i, j)));
            }
        }

        return ans;
    }

    private static Matrix<Function> getFunctionMatrix(RealVector vector) {
        Matrix<Function> answer = new Matrix<>(vector.getDimension(), 1, functionZero);
        for (int i = 0; i < vector.getDimension(); i++) {
            answer.set(i, 0, Functions.constant(vector.getEntry(i)));
        }
        return answer;
    }

    public static Matrix<Function> multiply(Matrix<Function> matrix, RealVector vector) {
        return matrix.multiply(getFunctionMatrix(vector));
    }

    public static Matrix<Function> integrate(Matrix<Function> matrix) {
        Matrix<Function> answer = new Matrix<>(matrix.n(), matrix.m(), functionZero);
        for (int i = 0; i < matrix.n(); i++) {
            for (int j = 0; j < matrix.m(); j++) {
                answer.set(i, j, matrix.get(i, j).integrate());
            }
        }
        return answer;
    }
}
