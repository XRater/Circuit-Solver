package ru.spbau.mit.circuit.logic.math.linearContainers.polynom;

import ru.spbau.mit.circuit.logic.math.algebra.interfaces.Field;

/**
 * Fabricity class for polynom class.
 */
@SuppressWarnings("WeakerAccess")
public class Polynoms {

    public static <C extends Field<C>> Polynom<C> zero(C zero) {
        return new Polynom<>(zero);
    }

    public static <C extends Field<C>> Polynom<C> constant(C constant) {
        Polynom<C> zeroPolynom = zero(constant);
        return zeroPolynom.singleton(constant, Monom.monom(0));
    }

    public static <C extends Field<C>> Polynom<C> linear(C zero) {
        Polynom<C> zeroPolynom = zero(zero);
        return zeroPolynom.singleton(zeroPolynom.fieldIdentity(), Monom.monom(1));
    }

    public static <C extends Field<C>> Polynom<C> linearWithConstant(C constant) {
        Polynom<C> constantPolynom = constant(constant);
        return constantPolynom.add(linear(constant));
    }

}
