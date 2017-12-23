package ru.spbau.mit.circuit.logic.matrices;


import org.apache.commons.math3.linear.RealMatrix;

import ru.spbau.mit.circuit.logic.gauss.functions1.PolyFunctions;

public class MatrixExponent {

    private static FunctionMatrix matrixExponent(JordanForm j) {
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
