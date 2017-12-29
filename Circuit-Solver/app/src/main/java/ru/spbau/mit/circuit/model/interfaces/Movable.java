package ru.spbau.mit.circuit.model.interfaces;


import ru.spbau.mit.circuit.model.circuitObjects.nodes.Point;

/**
 * This interface allows to move object without creating a new one.
 */
public interface Movable extends Centered {

    void move(int dx, int dy);

    /**
     * The method replaces object center to the new position.
     *
     * @param p new center position
     */
    default void replace(Point p) {
        move(p.x() - x(), p.y() - y());
    }
}
