package ru.spbau.mit.circuit.logic.math.matrices.matrixExponent;


import android.support.annotation.NonNull;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.spbau.mit.circuit.logic.math.functions.Function;
import ru.spbau.mit.circuit.logic.math.functions.Functions;
import ru.spbau.mit.circuit.logic.math.linearContainers.Polynom;
import ru.spbau.mit.circuit.logic.math.matrices.Matrices;
import ru.spbau.mit.circuit.logic.math.matrices.Matrix;

/**
 * Class to evaluate matrix Exponent.
 */
public class MatrixExponent {

    private static final Function functionZero = Functions.zero();

    public static Matrix<Function> matrixExponent(@NonNull RealMatrix matrix) {

        Map<Complex, Integer> roots = getEigenValues(matrix);
        Polynom<Function> polynom = buildVariablePolynom(roots);
        return polynom.evaluate(Matrices.getFunctionMatrix(matrix));
    }

    @NonNull
    private static Polynom<Function> buildVariablePolynom(@NonNull Map<Complex, Integer> roots) {
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

        // initializes multiply coefficient
        Polynom<Function> coefficient = new Polynom<>(functionZero,
                Collections.singletonList(Functions.constant(1)));

        // initializes subtractColumn
        SubtractColumn subtractColumn = new SubtractColumn(rootList);

        // updates answer polynom
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

    /**
     * The method valuates matrix eigen values.
     *
     * @param matrix matrix to find eigen values
     * @return map with all complex eigen values with their multiplicity
     */
    @NonNull
    private static Map<Complex, Integer> getEigenValues(@NonNull RealMatrix matrix) {
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

}
