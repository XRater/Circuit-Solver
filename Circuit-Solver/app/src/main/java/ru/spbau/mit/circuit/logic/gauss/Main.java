package ru.spbau.mit.circuit.logic.gauss;

import java.util.Arrays;
import java.util.List;

import ru.spbau.mit.circuit.logic.gauss.algebra.Numerical;
import ru.spbau.mit.circuit.logic.gauss.linear_containers.Vector;

public class Main {

    public static void main(String[] args) {

        LinearSystem<Numerical, Vector<Numerical, String>, Id> system = new LinearSystem<>(3);

        List<String> vars = Arrays.asList("a", "b", "c");
        Vector<Numerical, String> vector1 = new Vector<>(vars, Numerical.zero());
        Vector<Numerical, String> vector2 = new Vector<>(vars, Numerical.zero());
        Vector<Numerical, String> vector3 = new Vector<>(vars, Numerical.zero());

        vector1.setCoefficients(Numerical.number(2), Numerical.number(1), Numerical.number(-2));
        vector2.setCoefficients(Numerical.number(3), Numerical.number(-4), Numerical.number(2));
        vector3.setCoefficients(Numerical.number(-1), Numerical.number(1), Numerical.number(1));

        system.addEquation(new Equation<>(vector1, new Id()));
        system.addEquation(new Equation<>(vector2, new Id()));
        system.addEquation(new Equation<>(vector3, new Id()));

        system.solve();
        System.out.println(system);
    }

}
