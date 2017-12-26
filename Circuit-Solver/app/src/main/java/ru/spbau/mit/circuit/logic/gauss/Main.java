package ru.spbau.mit.circuit.logic.gauss;

/*
public class Main {

    public static void main(String[] args) {

        LinearSystem<BigReal, Vector<BigReal, String>, Id> system = new LinearSystem<>(3);

        List<String> vars = Arrays.asList("a", "b", "c");
        Vector<BigReal, String> vector1 = new Vector<>(vars, BigReal.zero());
        Vector<BigReal, String> vector2 = new Vector<>(vars, BigReal.zero());
        Vector<BigReal, String> vector3 = new Vector<>(vars, BigReal.zero());

        vector1.setCoefficients(BigReal.number(2), BigReal.number(1), BigReal.number(-2));
        vector2.setCoefficients(BigReal.number(3), BigReal.number(-4), BigReal.number(2));
        vector3.setCoefficients(BigReal.number(-1), BigReal.number(1), BigReal.number(1));

        system.addEquation(new Equation<>(vector1, new Id()));
        system.addEquation(new Equation<>(vector2, new Id()));
        system.addEquation(new Equation<>(vector3, new Id()));

        system.solve();
        System.out.println(system);
    }

}
*/