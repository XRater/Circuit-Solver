package ru.spbau.mit.circuit.logic.solver;


import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import ru.spbau.mit.circuit.logic.CircuitShortingException;
import ru.spbau.mit.circuit.logic.gauss.LinearSystem;
import ru.spbau.mit.circuit.logic.gauss.algebra.Numerical;
import ru.spbau.mit.circuit.logic.gauss.exceptions.ZeroDeterminantException;
import ru.spbau.mit.circuit.logic.gauss.functions1.Function;
import ru.spbau.mit.circuit.logic.gauss.functions1.Functions;
import ru.spbau.mit.circuit.logic.gauss.functions1.PolyFunction;
import ru.spbau.mit.circuit.logic.gauss.linear_containers.Row;
import ru.spbau.mit.circuit.logic.gauss.linear_containers.Vector;
import ru.spbau.mit.circuit.logic.gauss.variables.Derivative;
import ru.spbau.mit.circuit.logic.gauss.variables.FunctionVariable;
import ru.spbau.mit.circuit.logic.matrix_exponent.Matrices;
import ru.spbau.mit.circuit.logic.matrix_exponent.Matrix;
import ru.spbau.mit.circuit.logic.matrix_exponent.MatrixExponent;

public class Solver {

    private static int n;
    private static LinearSystem<
            Numerical,
            Vector<Numerical, Derivative>,
            Row<Numerical, FunctionVariable, PolyFunction>> initSystem;

    public static void solve(LinearSystem<
            Numerical,
            Vector<Numerical, Derivative>,
            Row<Numerical, FunctionVariable, PolyFunction>
            > systemToSolve) throws CircuitShortingException {
        initSystem = systemToSolve;
        n = initSystem.size();

        // Convert to Derivative = Sum of PolyFunctions
        try {
            initSystem.solve();
        } catch (ZeroDeterminantException e) {
            throw new CircuitShortingException();
        }
        System.out.println("Diagonal:");
        System.out.println(initSystem);

        RealMatrix A = getRightSideMatrix(initSystem);
        RealVector constants = getRightSideConstants(initSystem);

        if (isZeroMatrix(A)) {
            for (int i = 0; i < n; i++) {
                Derivative d = systemToSolve.get(i).coefficients().valueAt(i);
                d.parent().setValue(Functions.constant(constants.getEntry(i)).integrate());
                d.setValue();
            }
            return;
        }

        Matrix<Function> matrixExponent = MatrixExponent.matrixExponent(A);
        Matrix<Function> Ab = Matrices.multiply(
                MatrixExponent.matrixExponent(A.scalarMultiply(-1)), constants);
        Matrix<Function> constPart = matrixExponent.multiply(Matrices.integrate(Ab));

        for (int i = 0; i < n; i++) {
            Function ansI = Functions.constant(0);

            for (int j = 0; j < n; j++) {
                ansI = ansI.add(matrixExponent.get(i, j));
            }

            ansI = ansI.add(constPart.get(i, 0));

            Derivative d = systemToSolve.get(i).coefficients().valueAt(i);
            d.parent().setValue(ansI);
            d.setValue();
        }
    }

    private static boolean isZeroMatrix(RealMatrix a) {
        for (int i = 0; i < a.getRowDimension(); i++) {
            for (int j = 0; j < a.getRowDimension(); j++) {
                if (a.getEntry(i, j) != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private static RealVector getRightSideConstants(
            LinearSystem<
                    Numerical,
                    Vector<Numerical, Derivative>,
                    Row<Numerical, FunctionVariable, PolyFunction>> initSystem) {
        RealVector vector = new ArrayRealVector(n);
        for (int i = 0; i < n; i++) {
            vector.setEntry(i, initSystem.get(i).constant().constant().doubleValue());
        }
        return vector;
    }

    private static Map<Double, List<RealVector>> getVectors(RealMatrix matrix) {
        EigenDecomposition eg = new EigenDecomposition(matrix);
        Map<Double, List<RealVector>> map = new HashMap<>();
        RealMatrix vectors = eg.getV();
        //TODO complex roots
        for (int i = 0; i < n; i++) {
            double root = getRoot(matrix, vectors.getColumnMatrix(i));
            if (!map.containsKey(root)) {
                map.put(root, new ArrayList<>());
            }
            map.get(root).add(vectors.getColumnVector(i));
        }
        return map;
    }

    private static double getRoot(RealMatrix matrix, RealMatrix columnVector) {
        RealMatrix prod = matrix.multiply(columnVector);
        for (int i = 0; i < n; i++) {
            if (zeroAndMinusZero(columnVector.getEntry(i, 0)) != 0) {
                return prod.getEntry(i, 0) / columnVector.getEntry(i, 0);
            }
        }
        throw new NoSuchElementException();
    }

    private static double zeroAndMinusZero(double root) {
        return root == -0.0 ? 0.0 : root;
    }

    private static RealMatrix getRightSideMatrix(LinearSystem<
            Numerical,
            Vector<Numerical, Derivative>,
            Row<Numerical, FunctionVariable, PolyFunction>> system) {
        RealMatrix matrix = new Array2DRowRealMatrix(system.size(), system.size());
        for (int i = 0; i < n; i++) {
            Row<Numerical, FunctionVariable, PolyFunction> right = system.get(i).constant();
            for (int j = 0; j < n; j++) {
                Derivative derivative = system.get(i).coefficients().valueAt(j);
                Numerical c = right.get(derivative.parent());
                matrix.setEntry(i, j, c == null ? 0 : c.value());
            }
        }
        return matrix;
    }


    public static void main(String[] args) throws CircuitShortingException {
        FunctionVariable a = new FunctionVariable("a");
        FunctionVariable b = new FunctionVariable("b");
        Derivative da = new Derivative(a);
        Derivative db = new Derivative(b);
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
