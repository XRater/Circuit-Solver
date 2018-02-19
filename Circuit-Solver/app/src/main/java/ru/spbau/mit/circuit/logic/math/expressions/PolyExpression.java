package ru.spbau.mit.circuit.logic.math.expressions;


import android.support.annotation.NonNull;

import java.util.Iterator;

import ru.spbau.mit.circuit.logic.math.algebra.Numerical;
import ru.spbau.mit.circuit.logic.math.algebra.PolyElement;

class PolyExpression extends PolyElement<Numerical, Monom, PolyExpression> {

    @NonNull
    @Override
    protected PolyExpression empty() {
        return new PolyExpression();
    }

    @NonNull
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
}
