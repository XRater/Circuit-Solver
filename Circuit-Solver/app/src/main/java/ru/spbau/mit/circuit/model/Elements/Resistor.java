package ru.spbau.mit.circuit.model.Elements;

import ru.spbau.mit.circuit.model.Point;

public class Resistor extends Element {
    private double resistance = 1;

    public Resistor(Point from, Point to) {
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
