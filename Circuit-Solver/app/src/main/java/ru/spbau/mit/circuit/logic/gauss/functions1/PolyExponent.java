package ru.spbau.mit.circuit.logic.gauss.functions1;


import android.support.annotation.NonNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import ru.spbau.mit.circuit.logic.gauss.algebra.Field;
import ru.spbau.mit.circuit.logic.gauss.algebra.Linear;
import ru.spbau.mit.circuit.logic.gauss.algebra.Numerical;
import ru.spbau.mit.circuit.logic.gauss.functions1.exceptions.IllegalDoubleConvertionException;
import ru.spbau.mit.circuit.logic.gauss.functions1.exceptions
        .IllegalFunctionTransformationException;

public class PolyExponent implements Comparable<PolyExponent>, Field<PolyExponent>,
        Linear<Numerical, PolyExponent> {

    private final double cf;
    private final int mPow;
    private final int scale = 20;
    private BigDecimal ePow = new BigDecimal(0);
    private int precision = 2;

    double cf() {
        return cf;
    }

    int mPow() {
        return mPow;
    }

    BigDecimal ePow() {
        return ePow;
    }

    PolyExponent(double cf, int mPow, double ePow) {
        this.cf = cf;
        this.mPow = mPow;
        this.ePow = new BigDecimal(ePow).setScale(scale, RoundingMode.HALF_EVEN);
    }

    PolyExponent(double cf, int mPow, BigDecimal ePow) {
        this.cf = cf;
        this.mPow = mPow;
        this.ePow = ePow.setScale(scale, RoundingMode.HALF_EVEN);
    }

    private PolyExponent(PolyExponent f) {
        cf = f.cf;
        ePow = f.ePow;
        mPow = f.mPow;
    }

    @Override
    public int compareTo(@NonNull PolyExponent o) {
        if (!Objects.equals(ePow, o.ePow)) {
            return ePow.compareTo(o.ePow);
        }
        return mPow - o.mPow;
    }

    @Override
    public PolyExponent add(PolyExponent f) {
        if (f != null) {
            if (f.mPow != mPow || !ePow.equals(f.ePow)) {
                throw new IllegalFunctionTransformationException();
            }
            return new PolyExponent(cf + f.cf, mPow, ePow);
        }
        throw new IllegalFunctionTransformationException();
    }

    @Override
    public PolyExponent multiply(PolyExponent f) {
        if (f != null) {
            return new PolyExponent(cf * f.cf, mPow + f.mPow, ePow.add(f
                    .ePow));
        }
        throw new IllegalFunctionTransformationException();
    }

    @Override
    public PolyExponent reciprocal() {
        return new PolyExponent(1 / cf, -mPow, ePow.negate());
    }

    @Override
    public PolyExponent negate() {
        return new PolyExponent(-cf, mPow, ePow);
    }

    @Override
    public PolyExponent multiplyConstant(Numerical num) {
        return new PolyExponent(cf * num.value(), mPow, ePow);
    }

    @Override
    public boolean isZero() {
        return isEquals(cf, 0);
    }

    @Override
    public boolean isIdentity() {
        return isEquals(cf, 1) && mPow == 0 && ePow.signum() == 0;
    }

    @Override
    public PolyExponent getZero() {
        return new PolyExponent(0, 0, 0);
    }

    @Override
    public PolyExponent getIdentity() {
        return new PolyExponent(1, 0, 0);
    }

    public PolyFunction integrate() {
        if (isEquals(ePow.doubleValue(), 0)) {
            if (mPow != -1) {
                return PolyFunctions.power(cf / (mPow + 1), mPow + 1);
            } else {
                throw new UnsupportedOperationException();
            }
        } else {
            if (mPow == 0) {
                return PolyFunctions.exponent(cf / ePow.doubleValue(), ePow.doubleValue());
            } else {
                throw new UnsupportedOperationException();
            }
        }
    }

    private String writeNumber(double d) {
        if (isEquals(d, Math.round(d))) {
            if (Math.round(d) == 0 || Math.round(d) == 1) {
                return "";
            }
            return String.valueOf(Math.round(d));
        }
        String res = Double.toString(d);
        return res.substring(0, res.indexOf('.') + precision);
    }

    private boolean isEquals(double x, double y) {
        return Math.abs(x - y) < 0.0001;
    }

    public double doubleValue() {
        if (ePow.signum() != 0 || mPow != 0) {
            throw new IllegalDoubleConvertionException();
        }
        return cf;
    }

    @Override
    public String toString() {
        String res = "";
        res += writeNumber(cf);
        if (ePow.signum() == 0 && mPow == 0 && (writeNumber(cf).equals(""))) {
            res += Math.round(cf);
        }
        if (mPow != 0) {
            res += "t";
            if (mPow != 1) {
                res += "^" + mPow;
            }
        }
        if (ePow.signum() == 0) {
            return res;
        }
        res += "e^" + writeNumber(ePow.doubleValue()) + "t";
        return res;
    }
}
