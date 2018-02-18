package ru.spbau.mit.circuit.logic.solver;


import android.support.annotation.NonNull;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.ArrayList;

import ru.spbau.mit.circuit.logic.CircuitShortingException;
import ru.spbau.mit.circuit.logic.math.algebra.Numerical;
import ru.spbau.mit.circuit.logic.math.expressions.Expression;
import ru.spbau.mit.circuit.logic.math.functions.Function;
import ru.spbau.mit.circuit.logic.math.functions.Functions;
import ru.spbau.mit.circuit.logic.math.linearContainers.FArray;
import ru.spbau.mit.circuit.logic.math.linearSystems.LSystem;
import ru.spbau.mit.circuit.logic.math.linearSystems.exceptions.InconsistentSystemException;
import ru.spbau.mit.circuit.logic.math.matrices.Matrices;
import ru.spbau.mit.circuit.logic.math.matrices.Matrix;
import ru.spbau.mit.circuit.logic.math.matrices.matrixExponent.MatrixExponent;
import ru.spbau.mit.circuit.logic.math.variables.NumericalVariable;
import ru.spbau.mit.circuit.model.Result;


/**
 * Class to solve system of differential equations.
 */
public class Solver {

    private static int n;
    private static LSystem<Expression, FArray<Expression>> initSystem;

    /**
     * The method sets values of function variables and derivatives to their exact values.
     *
     * @param systemToSolve system to solve
     * @throws CircuitShortingException if the system expected to has more then one solution
     */
    public static ArrayList<? extends Result> solve(LSystem<Expression, FArray<Expression>>
                                                            systemToSolve)
            throws CircuitShortingException {

        initSystem = systemToSolve;
        n = initSystem.variablesNumber();
        System.out.println(initSystem);

        // Solve initial system
        ArrayList<FArray<Expression>> solution = initSystem.getSolution();

        for (int i = 0; i < solution.size(); i++) {
            System.out.println(solution.get(i));
        }

        // Set answer if A is getZero
        if (noCapacitors(solution)) {
            ArrayList<Expression> answer = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                answer.add(solution.get(i).back());
            }
            for (int i = 0; i < answer.size(); i++) {
                System.out.println(answer.get(i));
            }
            return answer;
        }

        RealMatrix A = getRightSideMatrix(solution);
        RealVector constants = getRightSideConstants(solution);


        // Evaluate general solution
        Matrix<Function> matrixExponent = MatrixExponent.matrixExponent(A);

        System.out.println(matrixExponent);

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

            answerAddition = answerAddition.add(constPart.get(i, 0).differentiate());

            answer.add(answerAddition);
        }

        return answer;
    }

    private static boolean noCapacitors(ArrayList<FArray<Expression>> solution) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (!solution.get(i).get(j).isZero()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * The method finds coefficients for global solution to match initial values.
     *
     * @param matrixExponent global solution of homogeneous system
     * @param constPart      particle solution of system
     * @return ordered array list of numerical variables storing coefficients
     */

    @NonNull
    private static ArrayList<Numerical> getCoefficients(Matrix<Function> matrixExponent,
                                                        Matrix<Function> constPart)
            throws InconsistentSystemException {
        ArrayList<NumericalVariable> variables = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            variables.add(new NumericalVariable("c" + i));
        }
        LSystem<Numerical, Numerical> constantsSystem =
                new LSystem<>(n, Numerical.zero(), Numerical.zero());

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
//
//            @SuppressWarnings("unchecked") Equation<Numerical, LArray<Numerical,
//                    NumericalVariable>, Numerical> eq =
//                    new Equation(LArray,
//                            constPart.get(i, 0).apply(0).negate()
//                                    .add(initSystem.get(i).coefficients().valueAt(i).parent()
//                                            .initialValue()));
//            constantsSystem.addEquation(eq);
    }

    /**
     * Checks if matrix is getZero matrix
     *
     * @param a matrix to check
     * @return true if matrix is getZero matrix and false otherwise
     */
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

    private static RealVector getRightSideConstants(ArrayList<FArray<Expression>> solution) {
        int size = solution.get(0).size();
        RealVector vector = new ArrayRealVector(n);
        for (int i = 0; i < n; i++) {
            vector.setEntry(i, solution.get(i).get(size - 1).doubleValue());
        }
        return vector;
    }

    private static RealMatrix getRightSideMatrix(ArrayList<FArray<Expression>> solution) {
        RealMatrix matrix = new Array2DRowRealMatrix(solution.size(), solution.size());
        for (int i = 0; i < n; i++) {
            FArray<Expression> right = solution.get(i);
            for (int j = 0; j < n; j++) {
                matrix.setEntry(i, j, right.get(j).doubleValue());
                //                Derivative derivative = system.get(i).coefficients().valueAt(j);
//                Numerical c = right.get(derivative.parent());
//                matrix.setEntry(i, j, c == null ? 0 : c.value());
            }
        }
        return matrix;
    }

}
