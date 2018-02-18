package ru.spbau.mit.circuit.logic.math.linearContainers;


import android.support.annotation.NonNull;

import ru.spbau.mit.circuit.logic.math.algebra.interfaces.Abel;
import ru.spbau.mit.circuit.logic.math.algebra.interfaces.Proportional;

public class LPair<F,
        U extends Abel<U> & Proportional<F, U>,
        V extends Abel<V> & Proportional<F, V>>
        implements Abel<LPair<F, U, V>>, Proportional<F, LPair<F, U, V>> {

    private final U uZero;
    private final V vZero;
    private U first;
    private V second;

    private LPair(@NonNull U u, @NonNull V v) {
        first = u;
        second = v;
        uZero = u.getZero();
        vZero = v.getZero();
    }

    public static <F,
            U extends Abel<U> & Proportional<F, U>,
            V extends Abel<V> & Proportional<F, V>> LPair<F, U, V> lpair(@NonNull U u, @NonNull V v) {
        return new LPair<>(u, v);
    }

    @NonNull
    @Override
    public LPair<F, U, V> add(@NonNull LPair<F, U, V> item) {
        first = first.add(item.first);
        second = second.add(item.second);
        return this;
    }

    @NonNull
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

    @NonNull
    @Override
    public LPair<F, U, V> getZero() {
        return lpair(uZero, vZero);
    }

    @NonNull
    @Override
    public LPair<F, U, V> multiplyConstant(F f) {
        first = first.multiplyConstant(f);
        second = second.multiplyConstant(f);
        return this;
    }
}
