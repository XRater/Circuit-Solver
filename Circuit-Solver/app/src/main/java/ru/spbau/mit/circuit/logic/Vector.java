package ru.spbau.mit.circuit.logic;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Deprecated
public class Vector {
    private final ArrayList<Fractional> coordinates = new ArrayList<>();

    public Vector(Fractional[] coordinates) {
        Collections.addAll(this.coordinates, coordinates);
    }

    public Vector(Collection<Fractional> coordinates) {
        this.coordinates.addAll(coordinates);
    }

    public Fractional getCoord(int i) {
        return coordinates.get(i);
    }

    public void mul(Fractional t) {
        for (int i = 0; i < coordinates.size(); i++) {
            coordinates.set(i, coordinates.get(i).mul(t));
        }
    }

    public void add(Vector v) {
        for (int i = 0; i < coordinates.size(); i++) {
            coordinates.set(i, coordinates.get(i).add(v.getCoord(i)));
        }
    }

    public void normalize() {
        int i = 0;
        while (i < coordinates.size() && coordinates.get(i).isZero()) {
            i++;
        }
        if (i == coordinates.size()) {
            return;
        }
        mul(coordinates.get(i).inverse());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Fractional f : coordinates) {
            sb.append(f).append(' ');
        }
        return sb.toString();
    }
}
