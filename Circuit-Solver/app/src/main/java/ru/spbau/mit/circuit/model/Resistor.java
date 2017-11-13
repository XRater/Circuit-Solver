package ru.spbau.mit.circuit.model;

public class Resistor extends Element {
    private double resistance;

    protected Resistor(Point from, Point to) {
        super(from, to);
    }

    public double getResistance() {
        return resistance;
    }

    public void setResistance(double resistance) {
        this.resistance = resistance;
    }

    //TODO
//    boolean isValid() {
//        return super.getCurrent() * resistance == super.getVoltage();
//    }
}
