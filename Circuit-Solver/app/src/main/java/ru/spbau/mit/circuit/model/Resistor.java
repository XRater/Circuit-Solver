package ru.spbau.mit.circuit.model;

public class Resistor extends Element {
    public float resistance;

    protected Resistor(Point from, Point to) {
        super(from, to);
    }

    //TODO
//    boolean isValid() {
//        return super.getCurrent() * resistance == super.getVoltage();
//    }
}
