package ru.spbau.mit.circuit.logic.solver;


import android.support.annotation.NonNull;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.ArrayList;

import ru.spbau.mit.circuit.logic.CircuitShortingException;
import ru.spbau.mit.circuit.logic.math.algebra.Numerical;
import ru.spbau.mit.circuit.logic.math.functions.Function;
import ru.spbau.mit.circuit.logic.math.functions.Functions;
import ru.spbau.mit.circuit.logic.math.linearContainers.FArray;
import ru.spbau.mit.circuit.logic.math.linearSystems.LinearSystem;
import ru.spbau.mit.circuit.logic.math.linearSystems.exceptions.InconsistentSystemException;
import ru.spbau.mit.circuit.logic.math.matrices.Matrices;
import ru.spbau.mit.circuit.logic.math.matrices.Matrix;
import ru.spbau.mit.circuit.logic.math.matrices.matrixExponent.MatrixExponent;


/**
 * Class to solve system of differential equations.
 */
public class Solver {

    private static int n;

    /**
     * The method sets values of function variables and derivatives to their exact values.
     *
     * @param systemToSolve system to solve
     * @throws CircuitShortingException if the system expected to has more then one solution
     */
    @NonNull
    public static ArrayList<Function> solve(
            @NonNull LinearSystem<Numerical, FArray<Numerical>> systemToSolve)
            throws CircuitShortingException {

        n = systemToSolve.variablesNumber();

        // Solve initial system
        ArrayList<FArray<Numerical>> solution = systemToSolve.getSolution();

        RealMatrix A = getRightSideMatrix(solution);
        RealVector constants = getRightSideConstants(solution);

        // Set answer if A is getZero
        if (isZeroMatrix(A)) {
            ArrayList<Function> answer = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                answer.add(Functions.constant(constants.getEntry(i)).integrate());
            }
            return answer;
        }

        // Evaluate general solution
        Matrix<Function> matrixExponent = MatrixExponent.matrixExponent(A);

        // Evaluate particle solution
        Matrix<Function> underIntegralMatrix = MatrixExponent.matrixExponent(A.scalarMultiply(-1))
                .multiply(Matrices.getFunctionMatrix(constants));

        Matrix<Function> constPart =
                matrixExponent.multiply(Matrices.integrate(underIntegralMatrix));

        // Find constants for initial values
        ArrayList<Numerical> coefficients;
        try {
            coefficients = getCoefficients(matrixExponent, constPart);
        } catch (InconsistentSystemException e) {
            throw new RuntimeException(); // Should never happen
        }


        // Set answer
        ArrayList<Function> answer = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Function answerAddition = Functions.zero();

            for (int j = 0; j < n; j++) {
                answerAddition = answerAddition.add(matrixExponent.get(i, j)
                        .multiplyConstant(coefficients.get(j)));
            }

            answerAddition = answerAddition.add(constPart.get(i, 0));

            answer.add(answerAddition);
        }

        return answer;
    }

    /**
     * The method finds coefficients for global solution to match initial values.
     *
     * @param matrixExponent global solution of homogeneous system
     * @param constPart      particle solution of system
     * @return ordered array list of numerical variables storing coefficients
     */

    @NonNull
    private static ArrayList<Numerical> getCoefficients(@NonNull Matrix<Function> matrixExponent,
                                                        @NonNull Matrix<Function> constPart)
            throws InconsistentSystemException {
        LinearSystem<Numerical, Numerical> constantsSystem =
                new LinearSystem<>(n, Numerical.zero(), Numerical.zero());

        for (int i = 0; i < n; i++) {
            FArray<Numerical> array = FArray.array(n, Numerical.zero());
            for (int j = 0; j < n; j++) {
                array.set(j, matrixExponent.get(i, j).apply(0));
            }

            //TODO
            constantsSystem.addEquation(
                    array, constPart.get(i, 0).apply(0).negate().add(Numerical.zero()));
        }

        return constantsSystem.getSolution();
    }

    /**
     * Checks if matrix is getZero matrix
     *
     * @param a matrix to check
     * @return true if matrix is getZero matrix and false otherwise
     */
    private static boolean isZeroMatrix(@NonNull RealMatrix a) {
        for (int i = 0; i < a.getRowDimension(); i++) {
            for (int j = 0; j < a.getRowDimension(); j++) {
                if (a.getEntry(i, j) != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    @NonNull
    private static RealVector getRightSideConstants(
            @NonNull ArrayList<FArray<Numerical>> solution) {
        int size = solution.get(0).size();
        RealVector vector = new ArrayRealVector(n);
        for (int i = 0; i < n; i++) {
            vector.setEntry(i, solution.get(i).get(size - 1).value());
        }
        return vector;
    }

    @NonNull
    private static RealMatrix getRightSideMatrix(@NonNull ArrayList<FArray<Numerical>> solution) {
        RealMatrix matrix = new Array2DRowRealMatrix(solution.size(), solution.size());
        for (int i = 0; i < n; i++) {
            FArray<Numerical> right = solution.get(i);
            for (int j = 0; j < n; j++) {
                matrix.setEntry(i, j, right.get(j).value());
            }
        }
        return matrix;
    }

}
