package ru.spbau.mit.circuit.logic.math.algebra.interfaces;


public interface Group<G extends Group<G>> extends Proportional<G, G> {

    G multiply(G g);

    G reciprocal();

    G getIdentity();

    default G divide(G f) {
        return multiply(f.reciprocal());
    }

    @Override
    default G multiplyConstant(G g) {
        return multiply(g);
    }

    default boolean isIdentity() {
        return this.equals(getIdentity());
    }
}