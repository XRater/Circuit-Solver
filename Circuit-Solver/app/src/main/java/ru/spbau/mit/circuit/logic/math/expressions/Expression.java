package ru.spbau.mit.circuit.logic.math.expressions;

import ru.spbau.mit.circuit.logic.math.algebra.Numerical;
import ru.spbau.mit.circuit.logic.math.algebra.QuotElement;
import ru.spbau.mit.circuit.model.Result;

public class Expression extends QuotElement<Numerical, Monom, PolyExpression, Expression>
        implements Result {

    Expression() {
        up = PolyExpressions.zero().empty();
        down = PolyExpressions.zero().single();
    }

    Expression(PolyExpression f) {
        up = f;
        down = PolyExpressions.constant(1);
    }

    private Expression(PolyExpression up, PolyExpression down) {
        this.up = up;
        this.down = down;
        simplify();
    }

    @Override
    public Expression multiply(Expression f) {
        PolyExpression newUp = up.tryDivide(f.down);
        PolyExpression newDown = down.tryDivide(f.up);
        if (newUp != null || newDown != null) {
            if (newDown == null) {
                return new Expression(newUp, down.multiply(f.up));
            }
            if (newUp == null) {
                return new Expression(up.multiply(f.down), newDown);
            }
            return new Expression(newUp, newDown);
        }
        return super.multiply(f);
    }

    @Override
    protected Expression empty() {
        return new Expression();
    }

    @Override
    protected Expression single() {
        Expression e = new Expression();
        e.up = PolyExpressions.zero().single();
        return e;
    }

    @Override
    protected Monom gcd() {
        Monom upGcd = up.gcd();
        Monom downGcd = down.gcd();
        return Monom.gcd(upGcd, downGcd);
    }

    public double doubleValue() {
        if (isZero()) {
            return 0;
        }
        return up.doubleValue() / down.doubleValue();
    }

}
