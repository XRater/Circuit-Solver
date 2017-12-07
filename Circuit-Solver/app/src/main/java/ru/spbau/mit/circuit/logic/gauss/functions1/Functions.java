package ru.spbau.mit.circuit.logic.gauss.functions1;


public class Functions {

    public static PolyExponent zero() {
        return new PolyExponent(0, 0, 0);
    }

    public static PolyExponent identity() {
        return new PolyExponent(1, 0, 0);
    }

    public static PolyExponent constant(double value) {
        return new PolyExponent(value, 0, 0);
    }

    public static PolyExponent power(int mPow) {
        return new PolyExponent(1, mPow, 0);
    }

    public static PolyExponent power(double cf, int mPow) {
        return new PolyExponent(cf, mPow, 0);
    }

    public static PolyExponent exponent(double ePow) {
        return new PolyExponent(1, 0, ePow);
    }

    public static PolyExponent exponent(double cf, double ePow) {
        return new PolyExponent(cf, 0, ePow);
    }

    public static PolyExponent polyExponent(int mPow, double ePow) {
        return new PolyExponent(1, mPow, ePow);
    }

    public static PolyExponent polyExponent(double cf, int mPow, double ePow) {
        return new PolyExponent(cf, mPow, ePow);
    }

}
