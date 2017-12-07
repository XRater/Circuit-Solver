package ru.spbau.mit.circuit.logic.gauss.functions1;


import ru.spbau.mit.circuit.logic.gauss.algebra.Numerical;
import ru.spbau.mit.circuit.logic.gauss.functions1.exceptions.IllegalFunctionCompareException;
import ru.spbau.mit.circuit.logic.gauss.functions1.exceptions
        .IllegalFunctionTransformationException;

public class PolyExponent implements PrimaryFunction {

    private final double cf;
    private final int mPow;
    private final double ePow;

    PolyExponent(double cf, int mPow, double ePow) {
        this.cf = cf;
        this.mPow = mPow;
        this.ePow = ePow;
    }

    private PolyExponent(PolyExponent f) {
        cf = f.cf;
        ePow = f.ePow;
        mPow = f.mPow;
    }

    @Override
    public int rank() {
        return 1;
    }

    @Override
    public int compare(PrimaryFunction o) {
        if (o instanceof PolyExponent) {
            if (ePow != ((PolyExponent) o).ePow) {
                return (int) (ePow - ((PolyExponent) o).ePow);
            }
            return mPow - ((PolyExponent) o).mPow;
        }
        throw new IllegalFunctionCompareException();
    }

    @Override
    public PrimaryFunction copy() {
        return new PolyExponent(this);
    }

    @Override
    public PrimaryFunction add(PrimaryFunction f) {
        if (f instanceof PolyExponent) {
            if (((PolyExponent) f).mPow != mPow || Math.abs(((PolyExponent) f).ePow - ePow) >
                    0.0001) {
                throw new IllegalFunctionTransformationException();
            }
            return new PolyExponent(cf + ((PolyExponent) f).cf, mPow, ePow);
        }
        throw new IllegalFunctionTransformationException();
    }

    @Override
    public PrimaryFunction mul(PrimaryFunction f) {
        if (f instanceof PolyExponent) {
            PolyExponent exponent = (PolyExponent) f;
            return new PolyExponent(cf * exponent.cf, mPow + exponent.mPow, ePow + exponent.ePow);
        }
        throw new IllegalFunctionTransformationException();
    }

    @Override
    public PrimaryFunction inverse() {
        return new PolyExponent(1 / cf, -mPow, -ePow);
    }

    @Override
    public PrimaryFunction negate() {
        return new PolyExponent(-cf, mPow, ePow);
    }

    @Override
    public boolean isZero() {
        return cf == 0;
    }

    @Override
    public PrimaryFunction mul(Numerical num) {
        return new PolyExponent(cf + num.value(), mPow, ePow);
    }

    @Override
    public String toString() {
        String res = "";
        res += writeNumber(cf);
        if (ePow == 0 && mPow == 0 && (writeNumber(cf).equals(""))) {
            res += Math.round(cf);
        }
        if (mPow != 0) {
            res += "t";
            if (mPow != 1) {
                res += "^" + mPow;
            }
        }
        if (Math.abs(ePow) < 0.0001) {
            return res;
        }
        res += "e^" + writeNumber(ePow) + "t";
        return res;
    }

    private String writeNumber(double d) {
        if (Math.abs(d - Math.round(d)) < 0.0001) {
            if (Math.round(d) == 0 || Math.round(d) == 1) {
                return "";
            }
            return String.valueOf(Math.round(d));
        }
        return Double.toString(d);
    }
}
