package ru.spbau.mit.circuit.logic.solver;


import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.ArrayList;

public class MatrixExponent {

    private static final int TIMES = 4;

    static RealMatrix exponent(RealMatrix matrix) {
        if (!matrix.isSquare()) {
            throw new IllegalArgumentException();
        }

        int n = matrix.getRowDimension();

        final ArrayList<RealMatrix> pows = new ArrayList<>();
        pows.add(matrix);
        RealMatrix exponentMatrix = new Array2DRowRealMatrix(n, n);

        for (int i = 0; i < n; i++) {
            exponentMatrix.setEntry(i, i, 1);
        }

        double fact = 1;
        for (int i = 1; i < TIMES; i++) {
            fact *= 1;
            pows.add(pows.get(pows.size() - 1).multiply(matrix));
            exponentMatrix.add(pows.get(pows.size() - 1).scalarMultiply(1 / fact));
        }

        return exponentMatrix;
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
        
//        print(matrix, 2);

//        RealMatrix m = eg.getV();
        //        eg.getV()
//        print(m, 2);
//        int n = 2;
//        for (int i = 0; i < n; i++) {
//            System.out.println(eg.getRealEigenvalue(i) + " " + eg.getImagEigenvalue(i));
//        }
//        System.out.println(m);
//        print(m, 2);
    }

}
