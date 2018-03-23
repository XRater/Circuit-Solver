package ru.spbau.mit.circuit.logic.math.functions;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import ru.spbau.mit.circuit.logic.math.algebra.Numerical;
import ru.spbau.mit.circuit.logic.math.algebra.interfaces.OrderedGroup;
import ru.spbau.mit.circuit.logic.math.functions.exceptions.IllegalFunctionTransformationException;

public class PolyExponent implements OrderedGroup<PolyExponent> {

    private static final int scale = 2;
    private static final double precision = 0.0000001;
    private final int mPow;
    private final double ePow;
    private int hashCode;

    private PolyExponent(int mPow, double ePow) {
        this.mPow = mPow;
        this.ePow = ePow;
    }

    static PolyExponent identity() {
        return new PolyExponent(0, 0);
    }

    static PolyExponent exponent(int mPow, double ePow) {
        return new PolyExponent(mPow, ePow);
    }

    boolean isExponent() {
        return mPow == 0;
    }

    boolean isPower() {
        return isEquals(ePow, 0);
    }

    double exponent() {
        return ePow;
    }

    int power() {
        return mPow;
    }

    @Override
    public int compareTo(@NonNull PolyExponent o) {
        if (!Objects.equals(ePow, o.ePow)) {
            return Double.compare(ePow, o.ePow);
        }
        return mPow - o.mPow;
    }

    @NonNull
    @Override
    public PolyExponent multiply(@Nullable PolyExponent f) {
        if (f != null) {
            return new PolyExponent(mPow + f.mPow, ePow + f.ePow);
        }
        throw new IllegalFunctionTransformationException();
    }

    @NonNull
    @Override
    public PolyExponent reciprocal() {
        return PolyExponent.exponent(-mPow, -ePow);
    }

    @Override
    public boolean isIdentity() {
        return mPow == 0 && isEquals(ePow, 0);
    }

    @NonNull
    @Override
    public PolyExponent getIdentity() {
        return new PolyExponent(0, 0);
    }

    @Override
    public int hashCode() {
        if (hashCode == -1) {
            hashCode = getHashCode();
        }
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PolyExponent) {
            PolyExponent exponent = (PolyExponent) obj;
            return exponent.mPow == mPow && isEquals(ePow, exponent.ePow);
        }
        return false;
    }

    @NonNull
    @Override
    public String toString() {
        if (isIdentity()) {
            return "1";
        }
        String res = "";
        if (mPow != 0) {
            res += "t";
            if (mPow != 1) {
                res += "^" + mPow;
            }
        }
        if (isEquals(ePow, 0)) {
            return res;
        }
        res += "exp(" + writeNumber(ePow) + "t)";
        return res;
    }

    @NonNull
    Numerical apply(double x) {
        if (isEquals(x, 0)) {
            if (mPow == 0) {
                return Numerical.identity();
            }
            if (mPow > 0) {
                return Numerical.zero();
            }
            throw new RuntimeException();
        }
        throw new UnsupportedOperationException();
    }

    private int getHashCode() {
        // Speedable
        return mPow + new BigDecimal(ePow).setScale(10, BigDecimal.ROUND_HALF_DOWN).hashCode();
    }

    private static String writeNumber(double d) {
        if (isEquals(d, Math.round(d))) {
            if (Math.round(d) == 0 || Math.round(d) == 1) {
                return "";
            }
            if (Math.round(d) == -1) {
                return "-";
            }
            return String.valueOf(Math.round(d));
        }
        BigDecimal decimal = new BigDecimal(d);
        decimal = decimal.setScale(scale, RoundingMode.HALF_EVEN);
        return decimal.toString();
    }

    private static boolean isEquals(double x, double y) {
        return Math.abs(x - y) < precision;
    }

}
