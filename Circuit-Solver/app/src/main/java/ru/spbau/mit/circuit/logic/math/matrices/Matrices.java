package ru.spbau.mit.circuit.logic.math.matrices;


import android.support.annotation.NonNull;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import ru.spbau.mit.circuit.logic.math.algebra.Field;
import ru.spbau.mit.circuit.logic.math.functions.Function;
import ru.spbau.mit.circuit.logic.math.functions.Functions;
import ru.spbau.mit.circuit.logic.math.linearContainers.Polynom;

/**
 * Class to create different kinds of matrices.
 */
@SuppressWarnings("WeakerAccess")
public class Matrices {

    @NonNull
    private static Function functionZero = Functions.zero();

    /**
     * The method creates an identity matrix of the require size.
     *
     * @param size size of matrix
     * @param zero zero of the field
     * @param <T>  type of elements stored in matrix
     * @return new identityMatix matrix
     */
    @NonNull
    public static <T extends Field<T>> Matrix<T> identityMatix(int size, @NonNull T zero) {
        Matrix<T> ans = new Matrix<>(size, zero);
        for (int i = 0; i < size; i++) {
            ans.set(i, i, zero.getIdentity());
        }
        return ans;
    }

    /**
     * The method converts realMatrix to FunctionMatrix. All cells of result matrix will
     * contain function constants equals to the origin matrix values.
     *
     * @param matrix matrix to convert
     * @return function matrix representation of the given matrix
     */
    @NonNull
    public static Matrix<Function> getFunctionMatrix(@NonNull RealMatrix matrix) {
        Matrix<Function> ans =
                new Matrix<>(matrix.getRowDimension(), matrix.getColumnDimension(), functionZero);

        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                ans.set(i, j, Functions.constant(matrix.getEntry(i, j)));
            }
        }

        return ans;
    }

    /**
     * The method converts realVector to FunctionMatrix. All cells of result matrix will
     * contain function constants equals to the origin matrix values.
     * <p>
     * Similar to getFunctionMatrix(RealMatrix m) method
     *
     * @param vector matrix to convert
     * @return function matrix representation of the given matrix
     */
    @NonNull
    public static Matrix<Function> getFunctionMatrix(@NonNull RealVector vector) {
        Matrix<Function> answer = new Matrix<>(vector.getDimension(), 1, functionZero);
        for (int i = 0; i < vector.getDimension(); i++) {
            answer.set(i, 0, Functions.constant(vector.getEntry(i)));
        }
        return answer;
    }


    /**
     * The method takes Function matrix and integrates every cell of it.
     *
     * @param matrix matrix to integrate.
     */
    @NonNull
    public static Matrix<Function> integrate(@NonNull Matrix<Function> matrix) {
        Matrix<Function> answer = new Matrix<>(matrix.n(), matrix.m(), functionZero);
        for (int i = 0; i < matrix.n(); i++) {
            for (int j = 0; j < matrix.m(); j++) {
                answer.set(i, j, matrix.get(i, j).integrate());
            }
        }
        return answer;
    }

    /**
     * This method was created to speed up apply method of polynom, but it did not work well yet.
     */
    @NonNull
    @Deprecated
    public static Matrix<Function> applyInPolynom(@NonNull RealMatrix matrix, @NonNull Polynom<Function> polynom) {
        int n = matrix.getRowDimension();
        RealMatrix power = new Array2DRowRealMatrix(matrix.getRowDimension(), matrix
                .getColumnDimension());
        for (int i = 0; i < n; i++) {
            power.setEntry(i, i, 1);
        }
        Matrix<Function> ans = Matrices.identityMatix(n, functionZero);
        for (int i = 0; i < polynom.monoms().size(); i++) {
            ans = ans.add(Matrices.getFunctionMatrix(power).multiplyConstant(polynom.monoms().get
                    (i)));
            power = matrix.multiply(power);
        }
        return ans;
    }
}
