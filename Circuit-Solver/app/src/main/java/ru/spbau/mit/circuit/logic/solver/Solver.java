package ru.spbau.mit.circuit.logic.solver;


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
import ru.spbau.mit.circuit.logic.gauss.variables.NumberVariable;
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


        ArrayList<NumberVariable> variables = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            variables.add(new NumberVariable("c" + i));
        }
        findCoefficients(matrixExponent, constPart, variables);
        for (int i = 0; i < n; i++) {
            System.out.println(variables.get(i).value());
        }

        for (int i = 0; i < n; i++) {
            Function ansI = Functions.constant(0);

            for (int j = 0; j < n; j++) {
                ansI = ansI.add(matrixExponent.get(i, j).multiplyConstant(variables.get(i).value
                        ()));
            }

            ansI = ansI.add(constPart.get(i, 0));

            Derivative d = systemToSolve.get(i).coefficients().valueAt(i);
            d.parent().setValue(ansI);
            System.out.println(d.parent().value());
            d.setValue();
        }
    }

    private static void findCoefficients(
            Matrix<Function> matrixExponent, Matrix<Function> constPart,
            ArrayList<NumberVariable> variables) {

        LinearSystem<Numerical,
                Vector<Numerical, NumberVariable>,
                Numerical> system = new LinearSystem<>(n);

        for (int i = 0; i < n; i++) {
            Vector<Numerical, NumberVariable> vector = new Vector<>(variables, Numerical.zero());
            for (int j = 0; j < n; j++) {
                vector.add(variables.get(i), matrixExponent.get(i, j).apply(0));
            }

            Equation<Numerical, Vector<Numerical, NumberVariable>, Numerical> eq =
                    new Equation(vector, constPart.get(i, 0).apply(0));
            system.addEquation(eq);
        }

        system.solve();

        for (int i = 0; i < n; i++) {
            Numerical constant = system.get(i).constant();
            system.get(i).coefficients().valueAt(i).setValue(constant);
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
