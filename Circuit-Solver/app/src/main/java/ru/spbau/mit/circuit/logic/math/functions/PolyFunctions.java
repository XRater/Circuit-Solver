package ru.spbau.mit.circuit.logic.math.functions;


public class PolyFunctions {

    private static final PolyFunction zero = PolyFunctions.constant(0);
    private static final PolyFunction identity = PolyFunctions.constant(1);

    public static PolyFunction zero() {
        return zero;
    }

    public static PolyFunction identity() {
        return identity;
    }

    public static PolyFunction constant(double value) {
        return new PolyFunction(new PolyExponent(value, 0, 0));
    }

    public static PolyFunction power(int mPow) {
        return new PolyFunction(new PolyExponent(1, mPow, 0));
    }

    public static PolyFunction power(double cf, int mPow) {
        return new PolyFunction(new PolyExponent(cf, mPow, 0));
    }

    public static PolyFunction exponent(double ePow) {
        return new PolyFunction(new PolyExponent(1, 0, ePow));
    }

    public static PolyFunction exponent(double cf, double ePow) {
        return new PolyFunction(new PolyExponent(cf, 0, ePow));
    }

    public static PolyFunction polyExponent(int mPow, double ePow) {
        return new PolyFunction(new PolyExponent(1, mPow, ePow));
    }

    public static PolyFunction polyExponent(double cf, int mPow, double ePow) {
        return new PolyFunction(new PolyExponent(cf, mPow, ePow));
    }
}
