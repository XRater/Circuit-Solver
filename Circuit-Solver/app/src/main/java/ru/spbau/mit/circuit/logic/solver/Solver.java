package ru.spbau.mit.circuit.logic.solver;


import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.BigReal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import ru.spbau.mit.circuit.logic.CircuitShortingException;
import ru.spbau.mit.circuit.logic.gauss.LinearSystem;
import ru.spbau.mit.circuit.logic.gauss.exceptions.ZeroDeterminantException;
import ru.spbau.mit.circuit.logic.gauss.functions1.Function;
import ru.spbau.mit.circuit.logic.gauss.functions1.PolyFunction;
import ru.spbau.mit.circuit.logic.gauss.functions1.PolyFunctions;
import ru.spbau.mit.circuit.logic.gauss.linear_containers.Row;
import ru.spbau.mit.circuit.logic.gauss.linear_containers.Vector;
import ru.spbau.mit.circuit.logic.gauss.variables.Derivative;
import ru.spbau.mit.circuit.logic.gauss.variables.FunctionVariable;

public class Solver {

    private static int n;
    private static LinearSystem<
            BigReal,
            Vector<BigReal, Derivative>,
            Row<BigReal, FunctionVariable, PolyFunction>> initSystem;

    public static void solve(LinearSystem<
            BigReal,
            Vector<BigReal, Derivative>,
            Row<BigReal, FunctionVariable, PolyFunction>
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

        // Find partial solutions
        // TODO change to e^A
        ArrayList<PartialSolution> solutions = findGlobalSolution();
        System.out.println("Partial solutions:");
        for (PartialSolution solution : solutions) {
            System.out.println(solution.vector() + " " + solution.function().toString());
        }

        
    }

    private static RealVector getRightSideConstants(
            LinearSystem<
                    BigReal,
                    Vector<BigReal, Derivative>,
                    Row<BigReal, FunctionVariable, PolyFunction>> initSystem) {
        RealVector vector = new ArrayRealVector(n);
        for (int i = 0; i < n; i++) {
            vector.setEntry(i, initSystem.get(i).constant().constant().doubleValue());
        }
        return vector;
    }

    private static ArrayList<PartialSolution> findGlobalSolution() {
        ArrayList<PartialSolution> solutions = new ArrayList<>();
        RealMatrix matrix = getRightSideMatrix(initSystem);
        System.out.println("Matrix:");
        for (int i = 0; i < n; i++) {
            System.out.println(matrix.getRowVector(i));
        }
        Map<Double, List<RealVector>> vectors = getVectors(matrix);
        for (double root : vectors.keySet()) {
            for (RealVector vector : vectors.get(root)) {
                solutions.add(new PartialSolution(vector, new Function(PolyFunctions.exponent
                        (root))));
            }
        }
        return solutions;
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
            BigReal,
            Vector<BigReal, Derivative>,
            Row<BigReal, FunctionVariable, PolyFunction>> system) {
        RealMatrix matrix = new Array2DRowRealMatrix(system.size(), system.size());
        for (int i = 0; i < n; i++) {
            Row<BigReal, FunctionVariable, PolyFunction> right = system.get(i).constant();
            for (int j = 0; j < n; j++) {
                Derivative derivative = system.get(i).coefficients().valueAt(j);
                BigReal c = right.get(derivative.parent());
                matrix.setEntry(i, j, c == null ? 0 : c.doubleValue());
            }
        }
        return matrix;
    }


    public static void main(String[] args) throws CircuitShortingException {
    }

}
