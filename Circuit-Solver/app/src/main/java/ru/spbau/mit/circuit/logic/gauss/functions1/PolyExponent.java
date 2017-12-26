package ru.spbau.mit.circuit.logic.gauss.functions1;


import android.support.annotation.NonNull;

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
    private final int scale = 10;
    //    private BigDecimal ePow = new BigDecimal(0);
    private double ePow;
    private int precision = 2;

    double cf() {
        return cf;
    }

    int mPow() {
        return mPow;
    }

    double ePow() {
        return ePow;
    }

    PolyExponent(double cf, int mPow, double ePow) {
        this.cf = cf;
        this.mPow = mPow;
//        this.ePow = new BigDecimal(ePow).setScale(scale, RoundingMode.HALF_EVEN);
        this.ePow = ePow;
    }

//    PolyExponent(double cf, int mPow, double ePow) {
//        this.cf = cf;
//        this.mPow = mPow;
//        this.ePow = ePow.setScale(scale, RoundingMode.HALF_EVEN);
//        this.ePow = ePow;
//    }

    private PolyExponent(PolyExponent f) {
        cf = f.cf;
        ePow = f.ePow;
        mPow = f.mPow;
    }

    @Override
    public int compareTo(@NonNull PolyExponent o) {
        if (!Objects.equals(ePow, o.ePow)) {
            return Double.compare(ePow, o.ePow);
        }
        return mPow - o.mPow;
    }

    @Override
    public PolyExponent add(PolyExponent f) {
        if (f != null) {
            if (f.mPow != mPow || !isEquals(ePow, f.ePow)) {
                throw new IllegalFunctionTransformationException();
            }
            return new PolyExponent(cf + f.cf, mPow, ePow);
        }
        throw new IllegalFunctionTransformationException();
    }

    @Override
    public PolyExponent multiply(PolyExponent f) {
        if (f != null) {
            return new PolyExponent(cf * f.cf, mPow + f.mPow, ePow + f.ePow);
        }
        throw new IllegalFunctionTransformationException();
    }

    @Override
    public PolyExponent reciprocal() {
        return new PolyExponent(1 / cf, -mPow, -ePow);
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
        return isEquals(cf, 1) && mPow == 0 && isEquals(ePow, 0);
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

    private String writeNumber(double d) {
        if (isEquals(d, Math.round(d))) {
            if (Math.round(d) == 0 || Math.round(d) == 1) {
                return "";
            }
            return String.valueOf(Math.round(d));
        }
        String res = Double.toString(d);
        return res;
        //        return res.substring(0, res.indexOf('.') + precision);
    }

    private boolean isEquals(double x, double y) {
        return Math.abs(x - y) < 0.0001;
    }

    public double doubleValue() {
        if (!isEquals(ePow, 0) || mPow != 0) {
            throw new IllegalDoubleConvertionException();
        }
        return cf;
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
        res += "e^" + writeNumber(ePow) + "t";
        System.out.println(res);
        return res;
    }

    public PolyFunction differentiate() {
        PolyFunction ans = PolyFunctions.zero();
        ans = ans.add(PolyFunctions.polyExponent(cf() * mPow(), mPow() - 1, ePow()));
        ans = ans.add(PolyFunctions.polyExponent(cf() * ePow(), mPow(), ePow()));
        return ans;
    }

    public Numerical apply(double x) {
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
}
