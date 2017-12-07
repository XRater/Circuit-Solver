package ru.spbau.mit.circuit.logic.solver;

/*
public class Solver {

    private static int n;
    private static LinearSystem<Vector<Derivative>, Row<FunctionVariable, FunctionExpression>>
            initSystem;

    public static void solve(LinearSystem<Vector<Derivative>,
            Row<FunctionVariable, FunctionExpression>> systemToSolve) throws
            CircuitShortingException {
        initSystem = systemToSolve;
        n = initSystem.size();

        // Convert to Derivative = Sum of Functions
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

        // Init variables for c_i
        Map<FunctionVariable, PartialSolution> derToSol = new HashMap<>();
        ArrayList<Derivative> derivatives = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            FunctionVariable variable = new FunctionVariable("c" + i);
            derivatives.add(new Derivative(variable));
            derToSol.put(variable, solutions.get(i));
        }

        // Make new System for c_i
        LinearSystem<Vector<Derivative>, FunctionExpression> partialSystem =
                makePartialSystem(solutions, derivatives);

        System.out.println("New initSystem:");
        System.out.println(partialSystem);

        // Solve it
        partialSystem.solve();

        System.out.println("Solved:");
        System.out.println(partialSystem);

        for (int i = 0; i < n; i++) {
            Derivative derivative = partialSystem.get(i).coefficients().valueAt(i);
            derivative.parent().setValue(partialSystem.get(i).constant().integrate());
        }


        for (int i = 0; i < n; i++) {
            Equation<Vector<Derivative>,
                    Row<FunctionVariable, FunctionExpression>> eq = initSystem.get(i);
            Derivative derivative = eq.coefficients().valueAt(i);
            FunctionVariable function = derivative.parent();
            FunctionExpression result = FunctionExpression.empty();
            for (Map.Entry<FunctionVariable, PartialSolution> entry : derToSol.entrySet()) {
                System.out.println(entry.getKey().value());
                result.add(entry.getValue().function().mul(entry.getKey().value()), entry
                        .getValue().coefficientAt(i));
            }
            function.setValue(result);
            derivative.setValue();
        }


    }

    @NonNull
    private static LinearSystem<Vector<Derivative>, FunctionExpression> makePartialSystem
            (List<PartialSolution> solutions, List<Derivative> derivatives) {
        LinearSystem<Vector<Derivative>, FunctionExpression>
                partialSystem = new LinearSystem<>(n);
        for (int i = 0; i < n; i++) {
            Vector<Derivative> left = new Vector<>(derivatives);
            Iterator<Derivative> varsIter = derivatives.iterator();
            Iterator<PartialSolution> solIter = solutions.iterator();
            while (varsIter.hasNext()) {
                left.add(varsIter.next(), solIter.next().coefficientAt(i));
            }
            partialSystem.addEquation(new Equation<>(left, initSystem.get(i).constant().constant
                    ()));
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
                solutions.add(new PartialSolution(vector, FunctionExpression.exponent(root)));
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

    private static RealMatrix getMatrix(LinearSystem<Vector<Derivative>,
            Row<FunctionVariable, FunctionExpression>> system) {
        RealMatrix matrix = new Array2DRowRealMatrix(system.size(), system.size());
        for (int i = 0; i < n; i++) {
//            System.out.println(i + ":");
            Row<FunctionVariable, FunctionExpression> right = system.get(i).constant();
            for (int j = 0; j < n; j++) {
                Derivative derivative = system.get(i).coefficients().valueAt(j);
//                System.out.println(derivative);
                matrix.setEntry(i, j, right.get(derivative.parent()));
            }
        }
        return matrix;
    }
*/

    /*
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
*/
/*
    public static void main(String[] args) throws CircuitShortingException {
        List<Derivative> derivatives = new ArrayList<>();
        List<FunctionVariable> variables = new ArrayList<>();

        FunctionVariable a = new FunctionVariable("a");
        FunctionVariable b = new FunctionVariable("b");
        variables.add(a);
        variables.add(b);

        Derivative a1 = new Derivative(a);
        Derivative b1 = new Derivative(b);
        derivatives.add(a1);
        derivatives.add(b1);

        Vector<Derivative> row1 = new Vector<>(derivatives);
        row1.add(a1, 1);

        Vector<Derivative> row2 = new Vector<>(derivatives);
        row2.add(b1, 1);
//        row2.add(a1, -1);


        Row<FunctionVariable, FunctionExpression> vector1 = new Row<>(FunctionExpression
                .constant(0));
        vector1.add(a, 2);
//        vector1.add(a, 2);
        vector1.addConst(FunctionExpression.constant(1));

        Row<FunctionVariable, FunctionExpression> vector2 = new Row<>(FunctionExpression
                .constant(0));
        vector2.add(b, 3);
        vector2.addConst(FunctionExpression.constant(1));


        LinearSystem<Vector<Derivative>, Row<FunctionVariable, FunctionExpression>> s =
                new LinearSystem<>(2);
        s.addEquation(new Equation<>(row1, vector1));
        s.addEquation(new Equation<>(row2, vector2));
        System.out.println("Initial system:");
        System.out.println(s);
        solve(s);

        System.out.println("Functions:");
        for (FunctionVariable variable : variables) {
            System.out.println(variable.value());
        }
        System.out.println("Derivatives:");
        for (FunctionVariable variable : derivatives) {
            System.out.println(variable.value());
        }
    }

}
*/