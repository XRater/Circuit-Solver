package ru.spbau.mit.circuit.logic.matrices;


import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

class Matrices {

    public static RealMatrix identity(int size) {
        RealMatrix ans = new Array2DRowRealMatrix(size, size);
        for (int i = 0; i < size; i++) {
            ans.setEntry(i, i, 1);
        }
        return ans;
    }

}
