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

        VarMonom ma1 = new VarMonom(a, 0);
        VarMonom mb1 = new VarMonom(b, 1);
        VarMonom mc1 = new VarMonom(c, 2);
        VarMonom ma2 = new VarMonom(a, 1);
        VarMonom mb2 = new VarMonom(b, 1);
        VarMonom mc2 = new VarMonom(c, -1);
        VarMonom ma3 = new VarMonom(a, 2);
        VarMonom mb3 = new VarMonom(b, 0);
        VarMonom mc3 = new VarMonom(c, -1);
        VarMonom mda1 = new VarMonom(da, 3);
        VarMonom mda2 = new VarMonom(da, 1);
        VarMonom mda3 = new VarMonom(da, -2);

        Polynom p1 = new Polynom(new VarMonom[]{ma1, mb1, mc1});
        Polynom p2 = new Polynom(new VarMonom[]{mb2, ma2, mc2});
        Polynom p3 = new Polynom(new VarMonom[]{mb3, ma3, mc3});
        Equation<Polynom, Polynom> e1 = new Equation<>(p1, new Polynom(new VarMonom[]{mda1}));
        Equation<Polynom, Polynom> e2 = new Equation<>(p2, new Polynom(new VarMonom[]{mda2}));
        Equation<Polynom, Polynom> e3 = new Equation<>(p3, new Polynom(new VarMonom[]{mda3}));
        LinearSystem<Polynom, Polynom> system = new LinearSystem<>(e1.size());
        system.addEquation(e1);
        system.addEquation(e2);
        system.addEquation(e3);

        system.solve();
*/
    }

}
