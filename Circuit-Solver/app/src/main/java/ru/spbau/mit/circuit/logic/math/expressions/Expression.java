package ru.spbau.mit.circuit.logic.math.expressions;

import ru.spbau.mit.circuit.logic.math.algebra.Numerical;
import ru.spbau.mit.circuit.logic.math.algebra.QuotElement;

public class Expression extends QuotElement<Numerical, Monom, PolyExpression, Expression> {

    Expression() {
        up = PolyExpressions.zero().empty();
        down = PolyExpressions.zero().single();
    }

    Expression(PolyExpression f) {
        up = f;
        down = PolyExpressions.constant(1);
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

//    public static void main(String[] args) throws ZeroDeterminantException {
//
//        Variable<Expression> a = new ExpressionVariable("a");
//        Variable<Expression> b = new ExpressionVariable("b");
//
//        List<Variable<Expression>> l = Arrays.asList(a, b);
//
//        LinearSystem<Expression,
//                LArray<Expression, Variable<Expression>>,
//                Expression> system = new LinearSystem<>(2);
//
//        LArray<Expression, Variable<Expression>> v1 = new LArray<>(l, Expressions.zero());
//        LArray<Expression, Variable<Expression>> v2 = new LArray<>(l, Expressions.zero());
//
//        v1.setCoefficients(Arrays.asList(Expressions.variable(2, "R"), Expressions.zero()));
//        v2.setCoefficients(Arrays.asList(Expressions.variable(1, "R"), Expressions.variable(1,
//                "R")));
//
//        Equation<Expression, LArray<Expression, Variable<Expression>>, Expression> eq1
//                = new Equation<>(v1, Expressions.variable("R"));
//
//        Equation<Expression, LArray<Expression, Variable<Expression>>, Expression> eq2
//                = new Equation<>(v2, Expressions.variable("R"));
//
//        system.addEquation(eq1);
//        system.addEquation(eq2);
//
//        System.out.println(system);
//        system.solve();
//        System.out.println(system);
//    }
}
