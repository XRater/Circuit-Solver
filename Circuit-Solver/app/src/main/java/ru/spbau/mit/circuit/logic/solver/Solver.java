package ru.spbau.mit.circuit.logic.solver;


import android.support.annotation.NonNull;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import ru.spbau.mit.circuit.logic.CircuitShortingException;
import ru.spbau.mit.circuit.logic.gauss.Equation;
import ru.spbau.mit.circuit.logic.gauss.LinearSystem;
import ru.spbau.mit.circuit.logic.gauss.algebra.Numerical;
import ru.spbau.mit.circuit.logic.gauss.exceptions.ZeroDeterminantException;
import ru.spbau.mit.circuit.logic.gauss.functions1.Function;
import ru.spbau.mit.circuit.logic.gauss.functions1.Functions;
import ru.spbau.mit.circuit.logic.gauss.functions1.PolyFunction;
import ru.spbau.mit.circuit.logic.gauss.functions1.PolyFunctions;
import ru.spbau.mit.circuit.logic.gauss.linear_containers.Row;
import ru.spbau.mit.circuit.logic.gauss.linear_containers.Vector;
import ru.spbau.mit.circuit.logic.gauss.variables.Derivative;
import ru.spbau.mit.circuit.logic.gauss.variables.FunctionVariable;
import ru.spbau.mit.circuit.logic.gauss.variables.NumberVariable;

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

//         Find partial solutions
        ArrayList<PartialSolution> solutions = findGlobalSolution();
        System.out.println("Partical solutions:");
        for (PartialSolution solution : solutions) {
            System.out.println(solution.vector() + " " + solution.function().toString());
        }

        Map<NumberVariable, PartialSolution> constantsForParts = new HashMap<>();
        ArrayList<NumberVariable> numberVariables = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            NumberVariable variable = new NumberVariable("c" + i);
            numberVariables.add(variable);
            constantsForParts.put(variable, solutions.get(i));
        }


        // Init variables for c_i
/*        Map<FunctionVariable, PartialSolution> derToSol = new HashMap<>();
        ArrayList<Derivative> derivatives = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            FunctionVariable variable = new FunctionVariable("c" + i);
            derivatives.add(new Derivative(variable));
            derToSol.put(variable, solutions.get(i));
        }
*/
        LinearSystem<
                Numerical,
                Vector<Numerical, NumberVariable>,
                Numerical> constantsSystem = makeConstantsSystem(solutions, numberVariables);


        // Make new System for c_i
 /*       LinearSystem<
                Function,
                Vector<Function, Derivative>,
                Function> partialSystem = makePartialSystem(solutions, derivatives);

        System.out.println("New initSystem:");
        System.out.println(partialSystem);
*/
        System.out.println(constantsSystem);

//         Solve it
//        partialSystem.solve();
        constantsSystem.solve();

        System.out.println("Solved:");
//        System.out.println(partialSystem);
        System.out.println(constantsSystem);

        for (int i = 0; i < n; i++) {
            NumberVariable variable = constantsSystem.get(i).coefficients().valueAt(i);
            variable.setValue(constantsSystem.get(i).constant());
        }

//        for (int i = 0; i < n; i++) {
//            Derivative derivative = partialSystem.get(i).coefficients().valueAt(i);
//            derivative.parent().setValue(partialSystem.get(i).constant().integrate());
//        }


        for (int i = 0; i < n; i++) {
            Equation<
                    Numerical,
                    Vector<Numerical, Derivative>,
                    Row<Numerical, FunctionVariable, PolyFunction>> eq = initSystem.get(i);
            Derivative derivative = eq.coefficients().valueAt(i);
            FunctionVariable function = derivative.parent();
            Function result = Functions.zero();
            for (Map.Entry<NumberVariable, PartialSolution> entry : constantsForParts.entrySet()) {
                Numerical cf = entry.getKey().value().mul(
                        Numerical.number(entry.getValue().coefficientAt(i)));
                result = result.add(entry.getValue().function().mul(cf.value()));
            }
            function.setValue(result);
            derivative.setValue();
        }
/*
        for (int i = 0; i < n; i++) {
            Equation<
                    Numerical,
                    Vector<Numerical, Derivative>,
                    Row<Numerical, FunctionVariable, PolyFunction>> eq = initSystem.get(i);
            Derivative derivative = eq.coefficients().valueAt(i);
            FunctionVariable function = derivative.parent();
            Function result = Functions.zero();
            for (Map.Entry<FunctionVariable, PartialSolution> entry : derToSol.entrySet()) {
                Function toAdd = entry.getKey().value().mul(entry.getValue().functionAt(i));
                result = result.add(toAdd);
            }
            function.setValue(result);
            derivative.setValue();
        }
*/
    }

    private static LinearSystem<
            Numerical,
            Vector<Numerical, NumberVariable>,
            Numerical> makeConstantsSystem(
            List<PartialSolution> solutions, List<NumberVariable> numberVariables) {

        LinearSystem<
                Numerical,
                Vector<Numerical, NumberVariable>,
                Numerical> constantsSystem = new LinearSystem<>(n);

        for (int i = 0; i < n; i++) {
            Vector<Numerical, NumberVariable> left = new Vector<>(numberVariables, Numerical.zero
                    ());
            Iterator<NumberVariable> varsIter = numberVariables.iterator();
            Iterator<PartialSolution> solIter = solutions.iterator();
            while (varsIter.hasNext()) {
                left.add(varsIter.next(), Numerical.number(solIter.next().coefficientAt(i)));
            }

            constantsSystem.addEquation(
                    new Equation<>(left, Numerical.number(
                            -initSystem.get(i).constant().constant().doubleValue())));
        }
        return constantsSystem;
    }

    @NonNull
    private static LinearSystem<
            Function,
            Vector<Function, Derivative>,
            Function> makePartialSystem(List<PartialSolution> solutions, List<Derivative>
            derivatives) {

        LinearSystem<
                Function,
                Vector<Function, Derivative>,
                Function>
                partialSystem = new LinearSystem<>(n);

        for (int i = 0; i < n; i++) {
            Vector<Function, Derivative> left = new Vector<>(derivatives, Functions.zero());
            Iterator<Derivative> varsIter = derivatives.iterator();
            Iterator<PartialSolution> solIter = solutions.iterator();
            while (varsIter.hasNext()) {
                left.add(varsIter.next(), solIter.next().functionAt(i));
            }

            partialSystem.addEquation(
                    new Equation<>(left, new Function(initSystem.get(i).constant().constant())));
        }
        return partialSystem;
    }

    private static ArrayList<PartialSolution> findGlobalSolution() {
        ArrayList<PartialSolution> solutions = new ArrayList<>();
        RealMatrix matrix = getMatrix(initSystem);
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
//        System.out.println("Normalized");
//        for (int i = 0; i < n; i++) {
//            System.out.println(vectors.getColumnVector(i));
//        }
        //        System.out.println(vectors);
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

    private static RealMatrix getMatrix(LinearSystem<
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
        List<Derivative> derivatives = new ArrayList<>();
        List<FunctionVariable> variables = new ArrayList<>();

        FunctionVariable a = new FunctionVariable("a");
        FunctionVariable b = new FunctionVariable("b");
        FunctionVariable c = new FunctionVariable("c");
        variables.add(a);
        variables.add(b);
        variables.add(c);

        Derivative a1 = new Derivative(a);
        Derivative b1 = new Derivative(b);
        Derivative c1 = new Derivative(c);
        derivatives.add(a1);
        derivatives.add(b1);
        derivatives.add(c1);

        Vector<Numerical, Derivative> vector1 = new Vector<>(derivatives, Numerical.zero());
        vector1.add(a1, Numerical.number(1));
        vector1.add(b1, Numerical.number(-1));
        vector1.add(c1, Numerical.number(-1));

        Vector<Numerical, Derivative> vector2 = new Vector<>(derivatives, Numerical.zero());
        vector2.add(b1, Numerical.number(1));

        Vector<Numerical, Derivative> vector3 = new Vector<>(derivatives, Numerical.zero());
        vector3.add(b1, Numerical.number(1));
        vector3.add(a1, Numerical.number(1));

        Row<Numerical, FunctionVariable, PolyFunction> row1 =
                new Row<>(PolyFunctions.constant(0));

        Row<Numerical, FunctionVariable, PolyFunction> row2 =
                new Row<>(PolyFunctions.constant(0));
        row2.add(c, Numerical.number(1));

        Row<Numerical, FunctionVariable, PolyFunction> row3 =
                new Row<>(PolyFunctions.constant(0));
        row3.add(a, Numerical.number(1));
        row3.addConst(PolyFunctions.constant(10));

        LinearSystem<
                Numerical,
                Vector<Numerical, Derivative>,
                Row<Numerical, FunctionVariable, PolyFunction>> s =
                new LinearSystem<>(3);

        s.addEquation(new Equation<>(vector1, row1));
        s.addEquation(new Equation<>(vector2, row2));
        s.addEquation(new Equation<>(vector3, row3));
        System.out.println("Initial system:");
        System.out.println(s);
        solve(s);

        System.out.println("PolyFunctions:");
        for (FunctionVariable variable : variables) {
            System.out.println(variable.value());
        }
        System.out.println("Derivatives:");
        for (FunctionVariable variable : derivatives) {
            System.out.println(variable.value());
        }
    }

}
