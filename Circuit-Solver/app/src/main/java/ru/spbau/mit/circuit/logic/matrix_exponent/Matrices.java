package ru.spbau.mit.circuit.logic.matrix_exponent;


import org.apache.commons.math3.Field;
import org.apache.commons.math3.FieldElement;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.BigReal;

import ru.spbau.mit.circuit.logic.gauss.functions1.Function;
import ru.spbau.mit.circuit.logic.gauss.functions1.Functions;

class Matrices {

    private static final Field<BigReal> realField = new BigReal(0).getField();
    private static final Field<Function> functionField = Functions.constant(0).getField();

    public static <T extends FieldElement<T>> Matrix<T> identity(int size, Field<T> field) {
        Matrix<T> ans = new Matrix<T>(size, field);
        for (int i = 0; i < size; i++) {
            ans.set(i, i, field.getOne());
        }
        return ans;
    }

    public static Matrix<Function> getPolynomMatrix(RealMatrix matrix) {
        if (!matrix.isSquare()) {
            throw new IllegalArgumentException();
        }

        Matrix<Function> ans =
                new Matrix<>(matrix.getRowDimension(), functionField);

        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getRowDimension(); j++) {
                ans.set(i, j, Functions.power(matrix.getEntry(i, j), 1));
            }
        }

        return ans;
    }
}
