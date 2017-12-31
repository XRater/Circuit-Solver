package ru.spbau.mit.circuit.logic.math.functions;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import ru.spbau.mit.circuit.logic.math.algebra.Field;
import ru.spbau.mit.circuit.logic.math.algebra.Linear;
import ru.spbau.mit.circuit.logic.math.algebra.Numerical;
import ru.spbau.mit.circuit.logic.math.functions.exceptions.IllegalDoubleConvertionException;
import ru.spbau.mit.circuit.logic.math.functions.exceptions.IllegalFunctionTransformationException;

public class PolyExponent implements Comparable<PolyExponent>, Field<PolyExponent>,
        Linear<Numerical, PolyExponent> {

    private static final int scale = 2;
    private static final double precision = 0.0000001;

    private final double cf;
    private final int mPow;
    private final double ePow;

    private int hashCode;

    PolyExponent(double cf, int mPow, double ePow) {
        this.cf = cf;
        this.mPow = mPow;
        this.ePow = ePow;
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

    @Override
    public int compareTo(@NonNull PolyExponent o) {
        if (!Objects.equals(ePow, o.ePow)) {
            return Double.compare(ePow, o.ePow);
        }
        return mPow - o.mPow;
    }

    @NonNull
    @Override
    public PolyExponent add(@Nullable PolyExponent f) {
        if (f != null) {
            if (f.mPow != mPow || !isEquals(ePow, f.ePow)) {
                throw new IllegalFunctionTransformationException();
            }
            return new PolyExponent(cf + f.cf, mPow, ePow);
        }
        throw new IllegalFunctionTransformationException();
    }

    @NonNull
    @Override
    public PolyExponent multiply(@Nullable PolyExponent f) {
        if (f != null) {
            return new PolyExponent(cf * f.cf, mPow + f.mPow, ePow + f.ePow);
        }
        throw new IllegalFunctionTransformationException();
    }

    @NonNull
    @Override
    public PolyExponent reciprocal() {
        return new PolyExponent(1 / cf, -mPow, -ePow);
    }

    @NonNull
    @Override
    public PolyExponent negate() {
        return new PolyExponent(-cf, mPow, ePow);
    }

    @NonNull
    @Override
    public PolyExponent multiplyConstant(@NonNull Numerical num) {
        return new PolyExponent(cf * num.value(), mPow, ePow);
    }

    @Override
    public boolean isZero() {
        return isEquals(cf, 0);
    }

    @Override
    public boolean isIdentity() {
        return isEquals(cf, 1) && mPow == 0 && isEquals(ePow, 0);
    }

    @NonNull
    @Override
    public PolyExponent getZero() {
        return new PolyExponent(0, 0, 0);
    }

    @NonNull
    @Override
    public PolyExponent getIdentity() {
        return new PolyExponent(1, 0, 0);
    }

    @NonNull
    PolyFunction integrate() {
        if (isEquals(ePow, 0)) {
            if (mPow != -1) {
                return PolyFunctions.power(cf / (mPow + 1), mPow + 1);
            } else {
                throw new UnsupportedOperationException();
            }
        } else {
            if (mPow == 0) {
                return PolyFunctions.exponent(cf / ePow, ePow);
            } else {
                throw new UnsupportedOperationException();
            }
        }
    }

    double doubleValue() {
        if (!isEquals(ePow, 0) || mPow != 0) {
            throw new IllegalDoubleConvertionException();
        }
        return cf;
    }

    @NonNull
    PolyFunction differentiate() {
        PolyFunction ans = PolyFunctions.zero();
        ans = ans.add(PolyFunctions.polyExponent(cf * mPow, mPow - 1, ePow));
        ans = ans.add(PolyFunctions.polyExponent(cf * ePow, mPow, ePow));
        return ans;
    }

    @NonNull
    Numerical apply(double x) {
        if (isEquals(x, 0)) {
            if (mPow == 0) {
                return Numerical.number(cf);
            }
            if (mPow > 0) {
                return Numerical.zero();
            }
            throw new RuntimeException();
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        String res = "";
        res += writeNumber(cf);
        if (isEquals(ePow, 0) && mPow == 0 && (writeNumber(cf).equals(""))) {
            res += Math.round(cf);
        }
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
            return exponent.mPow == mPow &&
                    isEquals(ePow, exponent.ePow) && isEquals(cf, exponent.cf);
        }
        return false;
    }

    private int getHashCode() {
        // Speedable
        return mPow +
                new BigDecimal(ePow).setScale(10, BigDecimal.ROUND_HALF_DOWN).hashCode() +
                new BigDecimal(cf).setScale(10, BigDecimal.ROUND_HALF_DOWN).hashCode();
    }
}
