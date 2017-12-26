package ru.spbau.mit.circuit.logic.solver;


import android.support.annotation.NonNull;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.ArrayList;

import ru.spbau.mit.circuit.logic.CircuitShortingException;
import ru.spbau.mit.circuit.logic.gauss.Equation;
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
import ru.spbau.mit.circuit.logic.gauss.variables.NumericalVariable;
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
        Matrix<Function> Ab = Matrices.multiply(
                MatrixExponent.matrixExponent(A.scalarMultiply(-1)), constants);
        Matrix<Function> constPart = matrixExponent.multiply(Matrices.integrate(Ab));

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

            Equation<Numerical, Vector<Numerical, NumericalVariable>, Numerical> eq =
                    new Equation(vector,
                            constPart.get(i, 0).apply(0).negate()
                                    .add(initSystem.get(i).coefficients().valueAt(i).parent()
                                            .initialValue()));
            constantsSystem.addEquation(eq);
        }

        constantsSystem.solve();
        for (int i = 0; i < n; i++) {
            Numerical constant = constantsSystem.get(i).constant();
            constantsSystem.get(i).coefficients().valueAt(i).setValue(constant);
        }
        return variables;
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
