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
import ru.spbau.mit.circuit.logic.math.functions.PolyFunction;
import ru.spbau.mit.circuit.logic.math.linearContainers.Vector;
import ru.spbau.mit.circuit.logic.math.linearSystems.Equation;
import ru.spbau.mit.circuit.logic.math.linearSystems.LinearSystem;
import ru.spbau.mit.circuit.logic.math.linearSystems.Row;
import ru.spbau.mit.circuit.logic.math.linearSystems.exceptions.ZeroDeterminantException;
import ru.spbau.mit.circuit.logic.math.matrices.Matrices;
import ru.spbau.mit.circuit.logic.math.matrices.Matrix;
import ru.spbau.mit.circuit.logic.math.matrices.matrixExponent.MatrixExponent;
import ru.spbau.mit.circuit.logic.math.variables.Derivative;
import ru.spbau.mit.circuit.logic.math.variables.FunctionVariable;
import ru.spbau.mit.circuit.logic.math.variables.NumericalVariable;


/**
 * Class to solve system of differential equations.
 */
public class Solver {

    private static int n;
    private static LinearSystem<
            Numerical,
            Vector<Numerical, Derivative>,
            Row<Numerical, FunctionVariable, PolyFunction>> initSystem;

    /**
     * The method sets values of function variables and derivatives to their exact values.
     *
     * @param systemToSolve system to solve
     * @throws CircuitShortingException if the system expected to has more then one solution
     */
    public static void solve(LinearSystem<
            Numerical,
            Vector<Numerical, Derivative>,
            Row<Numerical, FunctionVariable, PolyFunction>
            > systemToSolve) throws CircuitShortingException {
        initSystem = systemToSolve;
        n = initSystem.size();

        // Solve initial system
        try {
            initSystem.solve();
        } catch (ZeroDeterminantException e) {
            throw new CircuitShortingException();
        }
        System.out.println("Diagonal:");
        System.out.println(initSystem);

        RealMatrix A = getRightSideMatrix(initSystem);
        RealVector constants = getRightSideConstants(initSystem);

        // Set answer if A is zero
        if (isZeroMatrix(A)) {
            for (int i = 0; i < n; i++) {
                Derivative d = systemToSolve.get(i).coefficients().valueAt(i);
                d.parent().setValue(Functions.constant(constants.getEntry(i)).integrate());
                d.setValue();
            }
            return;
        }

        // Evaluate general solution
        Matrix<Function> matrixExponent = MatrixExponent.matrixExponent(A);

        // Evaluate particle solution
        Matrix<Function> underIntegralMatrix = MatrixExponent.matrixExponent(A.scalarMultiply(-1))
                .multiply(Matrices.getFunctionMatrix(constants));

        Matrix<Function> constPart = matrixExponent.multiply(Matrices.integrate
                (underIntegralMatrix));

        // Find constants for initial values
        ArrayList<NumericalVariable> variables = getCoefficients(matrixExponent, constPart);


        // Set answer
        for (int i = 0; i < n; i++) {
            Function ansI = Functions.constant(0);

            for (int j = 0; j < n; j++) {
                ansI = ansI.add(matrixExponent.get(i, j).multiplyConstant(variables.get(j).value
                        ()));
            }

            ansI = ansI.add(constPart.get(i, 0));

            Derivative d = systemToSolve.get(i).coefficients().valueAt(i);
            d.parent().setValue(ansI);
            d.setValue();
        }
    }

    /**
     * The method finds coefficients for global solution to match initial values.
     *
     * @param matrixExponent global solution of homogeneous system
     * @param constPart      particle solution of system
     * @return ordered array list of numerical variables storing coefficients
     */
    @NonNull
    private static ArrayList<NumericalVariable> getCoefficients(Matrix<Function> matrixExponent,
                                                                Matrix<Function> constPart) {
        ArrayList<NumericalVariable> variables = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            variables.add(new NumericalVariable("c" + i));
        }
        LinearSystem<Numerical,
                Vector<Numerical, NumericalVariable>,
                Numerical> constantsSystem = new LinearSystem<>(n);

        for (int i = 0; i < n; i++) {
            Vector<Numerical, NumericalVariable> vector = new Vector<>(variables, Numerical.zero());
            for (int j = 0; j < n; j++) {
                vector.add(variables.get(i), matrixExponent.get(i, j).apply(0));
            }

            @SuppressWarnings("unchecked") Equation<Numerical, Vector<Numerical,
                    NumericalVariable>, Numerical> eq =
                    new Equation(vector,
                            constPart.get(i, 0).apply(0).negate()
                                    .add(initSystem.get(i).coefficients().valueAt(i).parent()
                                            .initialValue()));
            constantsSystem.addEquation(eq);
        }

        System.out.println("SOLVED");
        constantsSystem.solve();
        for (int i = 0; i < n; i++) {
            Numerical constant = constantsSystem.get(i).constant();
            constantsSystem.get(i).coefficients().valueAt(i).setValue(constant);
        }
        return variables;
    }

    /**
     * Checks if matrix is zero matrix
     *
     * @param a matrix to check
     * @return true if matrix is zero matrix and false otherwise
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

    /**
     * The method finds right side constants of linear system.
     *
     * @param initSystem system to get constants from
     * @return realVector storing right side system constants
     */
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

    /**
     * The method finds matrix of right side coefficients of the given linear system
     *
     * @param system system to get right side coefficients from
     * @return realMatrix storing coefficients of the right side of the system
     */
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

}
