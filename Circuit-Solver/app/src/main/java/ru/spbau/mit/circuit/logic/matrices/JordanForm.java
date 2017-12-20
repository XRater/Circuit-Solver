package ru.spbau.mit.circuit.logic.matrices;


import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularValueDecomposition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JordanForm {

    private final List<JordanCell> cells = new ArrayList<>();

    public static JordanForm getJordanForm(RealMatrix matrix) {
        if (!matrix.isSquare()) {
            throw new IllegalArgumentException();
        }
        int n = matrix.getRowDimension();

        JordanForm form = new JordanForm();
        Map<Complex, Integer> roots = getEigenValues(matrix);

        for (Map.Entry<Complex, Integer> entry : roots.entrySet()) {
            if (entry.getKey().getImaginary() != 0) {
                throw new NotYetException();
            }
            List<Integer> cellsSizes = getCellsSizes(matrix, entry.getKey(), entry.getValue());
            for (int size : cellsSizes) {
                form.addCell(entry.getKey(), size);
            }
        }

        return form;
    }

    private static List<Integer> getCellsSizes(RealMatrix matrix, Complex root, int multiplicity) {
        int n = matrix.getRowDimension();
        matrix = matrix.subtract(
                Matrices.identity(matrix.getRowDimension()).scalarMultiply(root.getReal()));

        RealMatrix powMatrix = matrix.copy();

        SingularValueDecomposition solver = new SingularValueDecomposition(powMatrix);

        List<Integer> rowsSizes = new ArrayList<>();
        rowsSizes.add(multiplicity);

        int rank = solver.getRank();
        while (rank != n - multiplicity) {
            rowsSizes.add(rank);
            powMatrix = powMatrix.multiply(matrix);
            solver = new SingularValueDecomposition(powMatrix);
            rank = solver.getRank();
        }

        rowsSizes.add(0);
        for (int i = 0; i < rowsSizes.size() - 1; i++) {
            rowsSizes.set(i, rowsSizes.get(i) - rowsSizes.get(i + 1));
        }

        List<Integer> ans = new ArrayList<>();
        for (int i = 0; i < rowsSizes.get(0); i++) {
            ans.add(0);
        }
        for (int i = 0; i < rowsSizes.size(); i++) {
            for (int j = 0; j < rowsSizes.get(i); j++) {
                ans.set(j, ans.get(j) + 1);
            }
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

    private void addCell(Complex value, int size) {
        cells.add(new JordanCell(value, size));
    }

    public static class JordanCell {

        private final Complex value;
        private final int size;

        private JordanCell(Complex value, int size) {
            this.value = value;
            this.size = size;
        }

        @Override
        public String toString() {
            return "Cell " + value.toString() + ":" + size;
        }
    }

    @Override
    public String toString() {
        return cells.toString();
    }

    public static void main(String[] args) {
        RealMatrix matrix = new Array2DRowRealMatrix(2, 2);

        matrix.setEntry(0, 0, 0);
        matrix.setEntry(1, 0, 0);
        matrix.setEntry(0, 1, 0);
        matrix.setEntry(1, 1, 0);

        System.out.println(getJordanForm(matrix));
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
}
