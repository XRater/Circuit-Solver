package ru.spbau.mit.circuit.logic.matrix_exponent;


import android.support.annotation.NonNull;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.BigReal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.spbau.mit.circuit.logic.gauss.functions1.Function;
import ru.spbau.mit.circuit.logic.gauss.functions1.Functions;

public class MatrixExponent {

    private static final Field<BigReal> realField = new BigReal(0).getField();
    private static final Field<Function> functionField = Functions.constant(0).getField();

//    private static Matrix<Function> matrixExponent(JordanForm j) {
//        FunctionMatrix matrix = new FunctionMatrix(j.size());
//
//        int row = 0;
//
//        for (JordanForm.JordanCell cell : j.getCells()) {
//            double cf = 1;
//            for (int diag = 0; diag < cell.size(); diag++) {
//                cf /= (diag + 1);
//                for (int index = 0; index < cell.size() - diag; index++) {
//                    matrix.set(row + index, row + diag + index,
//                            PolyFunctions.polyExponent(cf, diag, cell.lambda()));
//                }
//            }
//            row += cell.size();
//        }
//
//        return matrix;
//    }

    private static Matrix<Function> matrixExponent(RealMatrix matrix) {
        Map<Complex, Integer> roots = getEigenValues(matrix);
        Polynom<Function> polynom = buildVariablePolynom(roots);
        return polynom.evaluate(Matrices.getPolynomMatrix(matrix));
    }

    @NonNull
    private static Polynom<Function> buildVariablePolynom(Map<Complex, Integer> roots) {
        Polynom<Function> ans = new Polynom<>(functionField);

        BlockArray blockArray = new BlockArray(roots);

        // Make list of roots with duplicates
        List<Double> rootList = new ArrayList<>();
        for (Map.Entry<Complex, Integer> entry : roots.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                if (entry.getKey().getImaginary() != 0) {
                    throw new NotYetException();
                }
                rootList.add(entry.getKey().getReal());
            }
        }

        Polynom<Function> coefficient = new Polynom<>(functionField);

        for (int i = 0; i < rootList.size(); i++) {
            coefficient = coefficient.multiply(new Polynom<>(functionField,
                    Arrays.asList(Functions.constant(rootList.get(i)), Functions.constant(1))));
            ans = ans.add(coefficient.multiply(blockArray.first()));
            blockArray.next();
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

    static void print(RealMatrix m, int sz) {
        for (int i = 0; i < sz; i++) {
            for (int j = 0; j < sz; j++) {
                System.out.print(m.getEntry(i, j) + " ");
            }
            System.out.print("\n");
        }
        System.out.print("\n");
    }

    public static void main(String[] args) {

        Map<Complex, Integer> map = new HashMap<>();
        map.put(new Complex(1, 0), 1);
//        map.put(new Complex(2, 0), 2);

        System.out.println(buildVariablePolynom(map));
    }

}
