package ru.spbau.mit.circuit.logic.solver;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import ru.spbau.mit.circuit.logic.CircuitShortingException;
import ru.spbau.mit.circuit.logic.system_solving.Equation;
import ru.spbau.mit.circuit.logic.system_solving.LinearSystem;
import ru.spbau.mit.circuit.logic.system_solving.exceptions.ZeroDeterminantException;
import ru.spbau.mit.circuit.logic.system_solving.functions.MonomExponent;
import ru.spbau.mit.circuit.logic.system_solving.functions.MonomPolyExponent;
import ru.spbau.mit.circuit.logic.system_solving.functions.PolyExponent;
import ru.spbau.mit.circuit.logic.system_solving.polynoms.BoundedPolynom;
import ru.spbau.mit.circuit.logic.system_solving.polynoms.Polynom;
import ru.spbau.mit.circuit.logic.system_solving.variables.Derivative;
import ru.spbau.mit.circuit.logic.system_solving.variables.FunctionVariable;

public class Solver {

    private static int n;
    private static LinearSystem<BoundedPolynom<Derivative>, Polynom<FunctionVariable>>
            system;

    public static void solve(LinearSystem<BoundedPolynom<Derivative>,
            Polynom<FunctionVariable>> systemToSolve) throws CircuitShortingException {
        system = systemToSolve;
        n = system.size();

        System.out.println(system);
        System.out.println();
        // Convert to Derivative = Sum of Functions
        try {
            system.solve();
        } catch (ZeroDeterminantException e) {
            throw new CircuitShortingException();
        }
        System.out.println(system);

        // Find partial solutions
//        List<PartialSolution> solutions = findGlobalSolution();
//        for (PartialSolution solution : solutions) {
//            System.out.println(solution);
//        }

        // Make new system
        LinearSystem<BoundedPolynom<PolyExponent>, BoundedPolynom<PolyExponent>> partialSystem = new
                LinearSystem<>(n);
//        partialSystem.addEquation(new Equation<>(null, system.get(0).constant().constant()));

        // Solve it


        for (int i = 0; i < system.size(); i++) {
            Equation<BoundedPolynom<Derivative>,
                    Polynom<FunctionVariable>> eq = system.get(i);
            FunctionVariable v = eq.coefficients().at(i).value();
            v.setValue(new MonomExponent(0, 0));
        }
    }

    private static List<PartialSolution> findGlobalSolution() {
        List<PartialSolution> solution = new ArrayList<>();
        RealMatrix matrix = getMatrix(system);
        Map<Double, List<RealVector>> vectors = getVectors(matrix);
        for (double root : vectors.keySet()) {
            for (RealVector vector : vectors.get(root)) {
                solution.add(new PartialSolution(vector, new MonomPolyExponent(0, root, 1)));
            }
        }
        return solution;
    }

    private static Map<Double, List<RealVector>> getVectors(RealMatrix matrix) {
        EigenDecomposition eg = new EigenDecomposition(matrix);
        Map<Double, List<RealVector>> map = new HashMap<>();
        RealMatrix roots = eg.getV();
        System.out.println(roots);
        //TODO complex roots
        for (int i = 0; i < n; i++) {
            double root = getRoot(matrix, roots.getColumnMatrix(i));
            if (!map.containsKey(root)) {
                map.put(root, new ArrayList<>());
            }
            map.get(root).add(roots.getColumnVector(i));
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

    private static RealMatrix getMatrix(LinearSystem<BoundedPolynom<Derivative>,
            Polynom<FunctionVariable>> system) {
        RealMatrix matrix = new Array2DRowRealMatrix(system.size(), system.size());
        for (int i = 0; i < n; i++) {
            Derivative derivative = system.get(i).coefficients().at(i).value();
            Polynom<FunctionVariable> polynom = system.get(i).constant();
//            Iterator<Monom<FunctionVariable>> iterator = polynom.monoms().iterator();
            for (int j = 0; j < n; j++) {
                matrix.setEntry(i, j, polynom.getCoefficient(derivative.parent()));
            }
        }
        return matrix;
    }

    private static Map<Double, Integer> getRealRoots(int n, EigenDecomposition eg) {
        Map<Double, Integer> realRoots = new HashMap<>();
        for (int i = 0; i < n; i++) {
            if (eg.getImagEigenvalue(i) == 0) {
                Integer cnt = 0;
                Double root = eg.getImagEigenvalue(i);
                if (realRoots.containsKey(root)) {
                    cnt = realRoots.get(root);
                }
                realRoots.remove(root);
                realRoots.put(eg.getRealEigenvalue(i), cnt + 1);
            }
        }
        return realRoots;
    }
}
