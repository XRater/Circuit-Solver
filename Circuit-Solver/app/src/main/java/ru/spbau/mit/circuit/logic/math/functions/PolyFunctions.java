package ru.spbau.mit.circuit.logic.math.functions;


import ru.spbau.mit.circuit.logic.math.algebra.Numerical;

@SuppressWarnings("WeakerAccess")
public class PolyFunctions {

    private static final PolyFunction zero = new PolyFunction();
    private static final PolyFunction identity = PolyFunctions.constant(1);

    private PolyFunctions() {
    }

    public static PolyFunction zero() {
        return zero;
    }

    public static PolyFunction identity() {
        return identity;
    }

    public static PolyFunction constant(double value) {
        return zero().singleton(Numerical.number(value), PolyExponent.identity());
    }

    public static PolyFunction power(int mPow) {
        return zero().singleton(Numerical.identity(), PolyExponent.exponent(mPow, 0));
    }

    public static PolyFunction power(double cf, int mPow) {
        return zero().singleton(Numerical.number(cf), PolyExponent.exponent(mPow, 0));
    }

    public static PolyFunction exponent(double ePow) {
        return zero().singleton(Numerical.identity(), PolyExponent.exponent(0, ePow));
    }

    public static PolyFunction exponent(double cf, double ePow) {
        return zero().singleton(Numerical.number(cf), PolyExponent.exponent(0, ePow));
    }

    public static PolyFunction polyExponent(int mPow, double ePow) {
        return zero().singleton(Numerical.identity(), PolyExponent.exponent(mPow, ePow));
    }

    public static PolyFunction polyExponent(double cf, int mPow, double ePow) {
        return zero().singleton(Numerical.number(cf), PolyExponent.exponent(mPow, ePow));
    }
}
