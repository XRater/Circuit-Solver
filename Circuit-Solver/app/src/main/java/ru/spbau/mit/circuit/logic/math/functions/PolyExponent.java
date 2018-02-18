package ru.spbau.mit.circuit.logic.math.functions;


import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.NonNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import ru.spbau.mit.circuit.logic.math.algebra.Numerical;
import ru.spbau.mit.circuit.logic.math.algebra.interfaces.OrderedGroup;
import ru.spbau.mit.circuit.logic.math.functions.exceptions.IllegalFunctionTransformationException;

import static ru.spbau.mit.circuit.ui.Drawer.ELEMENTS_PAINT;

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

    public static PolyExponent identity() {
        return new PolyExponent(0, 0);
    }

    public static PolyExponent exponent(int mPow, double ePow) {
        return new PolyExponent(mPow, ePow);
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

    public boolean isExponent() {
        return mPow == 0;
    }

    public boolean isPower() {
        return isEquals(ePow, 0);
    }

    public double exponent() {
        return ePow;
    }

    public int power() {
        return mPow;
    }

    @Override
    public int compareTo(@NonNull PolyExponent o) {
        if (!Objects.equals(ePow, o.ePow)) {
            return Double.compare(ePow, o.ePow);
        }
        return mPow - o.mPow;
    }

    @Override
    public PolyExponent multiply(PolyExponent f) {
        if (f != null) {
            return new PolyExponent(mPow + f.mPow, ePow + f.ePow);
        }
        throw new IllegalFunctionTransformationException();
    }

    @Override
    public PolyExponent reciprocal() {
        return PolyExponent.exponent(-mPow, -ePow);
    }

    @Override
    public boolean isIdentity() {
        return mPow == 0 && isEquals(ePow, 0);
    }

    @Override
    public PolyExponent getIdentity() {
        return new PolyExponent(0, 0);
    }

    //    PolyFunction integrate() {
//        if (isEquals(ePow, 0)) {
//            if (mPow != -1) {
//                return PolyFunctions.power(cf / (mPow + 1), mPow + 1);
//            } else {
//                throw new UnsupportedOperationException();
//            }
//        } else {
//            if (mPow == 0) {
//                return PolyFunctions.exponent(cf / ePow, ePow);
//            } else {
//                throw new UnsupportedOperationException();
//            }
//        }
//    PolyFunction differentiate() {
//        PolyFunction ans = PolyFunctions.identity();
//        ans = ans.add(PolyFunctions.polyExponent(cf * mPow, mPow - 1, ePow));
//        ans = ans.add(PolyFunctions.polyExponent(cf * ePow, mPow, ePow));
//        return ans;
//    }

    double doubleValue() {
        return 1;
    }

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

    public int print(Canvas canvas, int x, int y) {
        Rect textSize = new Rect();
        if (isIdentity()) {
            ELEMENTS_PAINT.getTextBounds("1", 0, "1".length(), textSize);
            canvas.drawText("1", x, y, ELEMENTS_PAINT);
            return x + textSize.width();
        }

        if (mPow != 0) {
            ELEMENTS_PAINT.getTextBounds("t", 0, "t".length(), textSize);
            canvas.drawText("t", x, y, ELEMENTS_PAINT);
            x += textSize.width();
            ELEMENTS_PAINT.getTextBounds(writeNumber(mPow), 0, writeNumber(mPow).length(), textSize);
            canvas.drawText(writeNumber(mPow), x, y + textSize.height(), ELEMENTS_PAINT);
            x += textSize.width();
        }
        if (!isEquals(ePow, 0)) {
            ELEMENTS_PAINT.getTextBounds("e", 0, "e".length(), textSize);
            canvas.drawText("e", x, y, ELEMENTS_PAINT);
            x += textSize.width();
            ELEMENTS_PAINT.getTextBounds(writeNumber(ePow), 0, writeNumber(ePow).length(), textSize);
            canvas.drawText(writeNumber(ePow) + "t", x, y - textSize.height(), ELEMENTS_PAINT);
            x += textSize.width();
        }
        return x;
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

    private int getHashCode() {
        // Speedable
        return mPow + new BigDecimal(ePow).setScale(10, BigDecimal.ROUND_HALF_DOWN).hashCode();
    }
}
