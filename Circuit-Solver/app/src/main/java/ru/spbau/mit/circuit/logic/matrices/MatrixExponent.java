package ru.spbau.mit.circuit.logic.matrices;


import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.BigReal;

import java.util.HashMap;
import java.util.Map;

import ru.spbau.mit.circuit.logic.gauss.functions1.Function;
import ru.spbau.mit.circuit.logic.gauss.functions1.PolyFunctions;
import ru.spbau.mit.circuit.logic.gauss.functions1.Polynom;

public class MatrixExponent {

    private static Matrix<Function> matrixExponent(JordanForm j) {
        FunctionMatrix matrix = new FunctionMatrix(j.size());

        int row = 0;

        for (JordanForm.JordanCell cell : j.getCells()) {
            double cf = 1;
            for (int diag = 0; diag < cell.size(); diag++) {
                cf /= (diag + 1);
                for (int index = 0; index < cell.size() - diag; index++) {
                    matrix.set(row + index, row + diag + index,
                            PolyFunctions.polyExponent(cf, diag, cell.lambda()));
                }
            }
            row += cell.size();
        }

        return matrix;
    }

    private static Matrix<Function> matrixExponent(RealMatrix matrix) {
        Map<Complex, Integer> roots = getEigenValues(matrix);
        Polynom<Polynom<BigReal>> polynom = buildVariablePolynom(roots);
        return polynom.evaluate(new Matrix(matrix));
    }

    private static Polynom<Polynom<BigReal>> buildVariablePolynom(Map<Complex, Integer> roots) {
        return null;
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

    }

}
