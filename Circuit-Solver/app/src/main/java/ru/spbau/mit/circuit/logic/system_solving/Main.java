package ru.spbau.mit.circuit.logic.system_solving;


import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        RealMatrix rm = new Array2DRowRealMatrix(new double[][]{
                {0, 1},
                {-1, 0}
        });
        EigenDecomposition eg = new EigenDecomposition(rm);

        double[] realRoots = eg.getRealEigenvalues();
        double[] imagRoots = eg.getImagEigenvalues();

        System.out.println(Arrays.toString(realRoots));
        System.out.println(Arrays.toString(imagRoots));
        /*
        Variable a = new Function("a");
        Variable b = new Function("b");
        Variable c = new Function("c");
        Variable da = new Derivative(a);

        Monom ma1 = new Monom(a, 0);
        Monom mb1 = new Monom(b, 1);
        Monom mc1 = new Monom(c, 2);
        Monom ma2 = new Monom(a, 1);
        Monom mb2 = new Monom(b, 1);
        Monom mc2 = new Monom(c, -1);
        Monom ma3 = new Monom(a, 2);
        Monom mb3 = new Monom(b, 0);
        Monom mc3 = new Monom(c, -1);
        Monom mda1 = new Monom(da, 3);
        Monom mda2 = new Monom(da, 1);
        Monom mda3 = new Monom(da, -2);

        Polynom p1 = new Polynom(new Monom[]{ma1, mb1, mc1});
        Polynom p2 = new Polynom(new Monom[]{mb2, ma2, mc2});
        Polynom p3 = new Polynom(new Monom[]{mb3, ma3, mc3});
        Equation<Polynom, Polynom> e1 = new Equation<>(p1, new Polynom(new Monom[]{mda1}));
        Equation<Polynom, Polynom> e2 = new Equation<>(p2, new Polynom(new Monom[]{mda2}));
        Equation<Polynom, Polynom> e3 = new Equation<>(p3, new Polynom(new Monom[]{mda3}));
        LinearSystem<Polynom, Polynom> system = new LinearSystem<>(e1.size());
        system.addEquation(e1);
        system.addEquation(e2);
        system.addEquation(e3);

        system.solve();
*/
    }

}
