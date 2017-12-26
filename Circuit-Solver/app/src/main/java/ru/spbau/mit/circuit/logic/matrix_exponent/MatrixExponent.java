package ru.spbau.mit.circuit.logic.matrix_exponent;


import android.support.annotation.NonNull;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.spbau.mit.circuit.logic.gauss.algebra.Numerical;
import ru.spbau.mit.circuit.logic.gauss.functions1.Function;
import ru.spbau.mit.circuit.logic.gauss.functions1.Functions;

public class MatrixExponent {

    private static final Numerical numericalZero = Numerical.zero();
    private static final Function functionZero = Functions.zero();

    public static Matrix<Function> matrixExponent(RealMatrix matrix) {
        Map<Complex, Integer> roots = getEigenValues(matrix);
        Polynom<Function> polynom = buildVariablePolynom(roots);
        return polynom.evaluate(Matrices.getFunctionMatrix(matrix));
    }

    @NonNull
    private static Polynom<Function> buildVariablePolynom(Map<Complex, Integer> roots) {
        Polynom<Function> ans = new Polynom<>(functionZero);

        // Make list of roots with duplicates
        List<Complex> rootList = new ArrayList<>();
        for (Map.Entry<Complex, Integer> entry : roots.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                // TODO
                if (entry.getKey().getImaginary() != 0) {
                    throw new NotYetException();
                }
                rootList.add(entry.getKey());
            }
        }

        Polynom<Function> coefficient = new Polynom<>(functionZero,
                Collections.singletonList(Functions.constant(1)));

        SubtractColumn subtractColumn = new SubtractColumn(rootList);

        for (int i = 0; i < rootList.size(); i++) {
            ans = ans.add(coefficient.multiply(
                    new Polynom<>(functionZero,
                            Collections.singletonList(subtractColumn.first()))));
            subtractColumn.next();
            coefficient = coefficient.multiply(new Polynom<>(functionZero,
                    Arrays.asList(Functions.constant(
                            -rootList.get(i).getReal()), Functions.constant(1))));
        }

        return ans;
    }

    private static Map<Complex, Integer> getEigenValues(RealMatrix matrix) {
        Map<Complex, Integer> ans = new HashMap<>();
        EigenDecomposition eg = new EigenDecomposition(matrix);
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            Complex value = new Complex(eg.getRealEigenvalue(i), eg.getImagEigenvalue(i));
            if (ans.containsKey(value)) {
                ans.put(value, ans.get(value) + 1);
            } else {
                ans.put(value, 1);
            }
        }
        return ans;
    }

    static void print(RealMatrix m) {
        int sz = m.getColumnDimension();
        for (int i = 0; i < sz; i++) {
            for (int j = 0; j < sz; j++) {
                System.out.print(m.getEntry(i, j) + " ");
            }
            System.out.print("\n");
        }
        System.out.print("\n");
    }

    public static void main(String[] args) {

        RealMatrix matrix = new Array2DRowRealMatrix(1, 1);
        matrix.setEntry(0, 0, 0);

        print(matrix);
        System.out.println(matrixExponent(matrix));
    }

}
