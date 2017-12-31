package ru.spbau.mit.circuit.model.circuitObjects.nodes;

import android.support.annotation.NonNull;

import java.io.Serializable;

import ru.spbau.mit.circuit.model.circuitObjects.exceptions.InvalidPointException;
import ru.spbau.mit.circuit.model.interfaces.Centered;
import ru.spbau.mit.circuit.model.interfaces.WireEnd;

public class Point implements Centered, WireEnd, Serializable {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Point getCenter(@NonNull Point a, @NonNull Point b) {
        if ((a.x + b.x) % 2 != 0 || (a.y + b.y) % 2 != 0) {
            throw new InvalidPointException("Float coordinate value");
        }
        return new Point((a.x + b.x) / 2, (a.y + b.y) / 2);
    }

    @Override
    public int x() {
        return x;
    }

    @Override
    public int y() {
        return y;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + this.x;
        hash = 71 * hash + this.y;
        return hash;
    }

    @NonNull
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Point) {
            Point p = (Point) obj;
            return p.x == x && p.y == y;
        }
        return false;
    }
}
