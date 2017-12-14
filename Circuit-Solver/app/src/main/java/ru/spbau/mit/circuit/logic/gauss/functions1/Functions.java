package ru.spbau.mit.circuit.logic.gauss.functions1;


public class Functions {

    public static Function zero() {
        return new Function(PolyFunctions.zero());
    }

    public static Function identity() {
        return new Function(PolyFunctions.identity());
    }

    public static Function constant(double value) {
        return new Function(PolyFunctions.constant(value));
    }

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
