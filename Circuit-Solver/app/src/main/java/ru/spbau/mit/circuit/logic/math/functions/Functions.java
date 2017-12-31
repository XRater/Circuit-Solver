package ru.spbau.mit.circuit.logic.math.functions;

@SuppressWarnings({"unused", "WeakerAccess"})
public class Functions {

    private static Function zero = Functions.constant(0);
    private static Function identity = Functions.constant(1);

    private Functions() {
    }

    public static Function zero() {
        return zero;
    }

    public static Function identity() {
        return identity;
    }

    public static Function constant(double value) {
        return new Function(PolyFunctions.constant(value));
    }

    @SuppressWarnings("SameParameterValue")
    public static Function power(int mPow) {
        return new Function(PolyFunctions.power(mPow));
    }

    public static Function power(double cf, int mPow) {
        return new Function(PolyFunctions.power(cf, mPow));
    }

    public static Function exponent(double ePow) {
        return new Function(PolyFunctions.exponent(ePow));
    }

    public static Function exponent(double cf, double ePow) {
        return new Function(PolyFunctions.exponent(cf, ePow));
    }

    public static Function polyExponent(int mPow, double ePow) {
        return new Function(PolyFunctions.polyExponent(mPow, ePow));
    }

    public static Function polyExponent(double cf, int mPow, double ePow) {
        return new Function(PolyFunctions.polyExponent(cf, mPow, ePow));
    }

}