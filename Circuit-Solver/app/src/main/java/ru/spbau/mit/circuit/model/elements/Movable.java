package ru.spbau.mit.circuit.model.elements;


import ru.spbau.mit.circuit.model.node.Point;

public interface Movable extends Centered {
    void move(int dx, int dy);

    default void replace(Point p) {
        move(p.x() - x(), p.y() - y());
    }
}
