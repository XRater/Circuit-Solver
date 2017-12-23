package ru.spbau.mit.circuit.logic.gauss.functions1;


public class PolyFunctions {

    public static PolyFunction zero() {
        return new PolyFunction(new PolyExponent(0, 0, 0));
    }

    public static PolyFunction identity() {
        return new PolyFunction(new PolyExponent(1, 0, 0));
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
