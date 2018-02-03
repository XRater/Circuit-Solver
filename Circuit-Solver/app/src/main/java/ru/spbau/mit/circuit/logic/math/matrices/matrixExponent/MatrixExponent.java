package ru.spbau.mit.circuit.logic.math.matrices.matrixExponent;


import android.support.annotation.NonNull;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.spbau.mit.circuit.logic.math.functions.Function;
import ru.spbau.mit.circuit.logic.math.functions.Functions;
import ru.spbau.mit.circuit.logic.math.linearContainers.polynom.Polynom;
import ru.spbau.mit.circuit.logic.math.linearContainers.polynom.Polynoms;
import ru.spbau.mit.circuit.logic.math.matrices.Matrices;
import ru.spbau.mit.circuit.logic.math.matrices.Matrix;

/**
 * Class to evaluate matrix Exponent.
 */
public class MatrixExponent {

    private static final Function functionZero = Functions.zero();

    public static Matrix<Function> matrixExponent(RealMatrix matrix) {
        Map<Complex, Integer> roots = getEigenValues(matrix);
        Polynom<Function> polynom = buildVariablePolynom(roots);
        return polynom.evaluate(Matrices.getFunctionMatrix(matrix));
    }

    @NonNull
    private static Polynom<Function> buildVariablePolynom(Map<Complex, Integer> roots) {
        Polynom<Function> ans = Polynoms.zero(functionZero);

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

        // initializes multiply coefficient
        Polynom<Function> coefficient = Polynoms.constant(Functions.constant(1));
//        new Polynom<>(functionZero,
//                Collections.singletonList(Functions.constant(1)));

        // initializes subtractColumn
        SubtractColumn subtractColumn = new SubtractColumn(rootList);

        // updates answer polynom
        for (int i = 0; i < rootList.size(); i++) {
            ans = ans.add(coefficient.multiply(
                    Polynoms.constant(subtractColumn.first())));
//                    new Polynom<>(functionZero,
//                            Collections.singletonList(subtractColumn.first()))));
            subtractColumn.next();
            coefficient = coefficient.multiply(
                    Polynoms.linearWithConstant(Functions.constant(-rootList.get(i).getReal())));
//                    new Polynom<>(functionZero,
//                    Arrays.asList(Functions.constant(
//                            -rootList.get(i).getReal()), Functions.constant(1))));
        }

        return ans;
    }

    /**
     * The method valuates matrix eigen values.
     *
     * @param matrix matrix to find eigen values
     * @return map with all complex eigen values with their multiplicity
     */
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

    public static void main(String[] args) {
        RealMatrix matrix = new Array2DRowRealMatrix(2, 2);
        matrix.setEntry(0, 0, 0);
        matrix.setEntry(0, 1, 0);
        matrix.setEntry(1, 0, 0);
        matrix.setEntry(1, 1, 0);
        System.out.println(matrixExponent(matrix));
    }

}
