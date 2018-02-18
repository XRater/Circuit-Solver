package ru.spbau.mit.circuit.logic.math.expressions;


import java.util.Iterator;

import ru.spbau.mit.circuit.logic.math.algebra.Numerical;
import ru.spbau.mit.circuit.logic.math.algebra.PolyElement;

class PolyExpression extends PolyElement<Numerical, Monom, PolyExpression> {

    @Override
    protected PolyExpression empty() {
        return new PolyExpression();
    }

    @Override
    protected PolyExpression single() {
        PolyExpression ans = empty();
        ans.add(Numerical.identity(), Monom.identity());
        return ans;
    }

    @Override
    public PolyExpression getZero() {
        return PolyExpressions.zero();
    }

    @Override
    public PolyExpression getIdentity() {
        return PolyExpressions.identity();
    }

    Monom gcd() {
        if (data.size() == 0) {
            throw new IllegalArgumentException();
        }
        Iterator<Monom> iterator = data.keySet().iterator();
        Monom gcd = iterator.next();
        while (iterator.hasNext()) {
            gcd = Monom.gcd(gcd, iterator.next());
        }
        return gcd;
    }

    public double doubleValue() {
        if (isZero()) {
            return 0;
        }
        if (data.size() != 1) {
            throw new IllegalArgumentException();
        }
        if (!data.keySet().iterator().next().isIdentity()) {
            throw new IllegalArgumentException();
        }
        return data.values().iterator().next().first().value();
    }
}
