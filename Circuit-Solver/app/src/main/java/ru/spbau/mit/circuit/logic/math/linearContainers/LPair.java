package ru.spbau.mit.circuit.logic.math.linearContainers;


import ru.spbau.mit.circuit.logic.math.algebra.interfaces.Abel;
import ru.spbau.mit.circuit.logic.math.algebra.interfaces.Proportional;

public class LPair<F,
        U extends Abel<U> & Proportional<F, U>,
        V extends Abel<V> & Proportional<F, V>>
        implements Abel<LPair<F, U, V>>, Proportional<F, LPair<F, U, V>> {

    public static <F,
            U extends Abel<U> & Proportional<F, U>,
            V extends Abel<V> & Proportional<F, V>> LPair<F, U, V> lpair(U u, V v) {
        return new LPair<>(u, v);
    }

    private final U uZero;
    private final V vZero;

    private U first;
    private V second;

    private LPair(U u, V v) {
        first = u;
        second = v;
        uZero = u.getZero();
        vZero = v.getZero();
    }

    @Override
    public LPair<F, U, V> add(LPair<F, U, V> item) {
        first = first.add(item.first);
        second = second.add(item.second);
        return this;
    }

    @Override
    public LPair<F, U, V> negate() {
        first = first.negate();
        second = second.negate();
        return this;
    }

    @Override
    public boolean isZero() {
        return first.isZero() && second.isZero();
    }

    @Override
    public LPair<F, U, V> getZero() {
        return lpair(uZero, vZero);
    }

    @Override
    public LPair<F, U, V> multiplyConstant(F f) {
        first = first.multiplyConstant(f);
        second = second.multiplyConstant(f);
        return this;
    }
}
