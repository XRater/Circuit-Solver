package ru.spbau.mit.circuit.logic.math.expressions;

import java.util.Arrays;
import java.util.List;

import ru.spbau.mit.circuit.logic.math.algebra.Numerical;
import ru.spbau.mit.circuit.logic.math.algebra.QuotElement;
import ru.spbau.mit.circuit.logic.math.linearContainers.Vector;
import ru.spbau.mit.circuit.logic.math.linearSystems.Equation;
import ru.spbau.mit.circuit.logic.math.linearSystems.LinearSystem;
import ru.spbau.mit.circuit.logic.math.linearSystems.exceptions.ZeroDeterminantException;
import ru.spbau.mit.circuit.logic.math.variables.ExpressionVariable;
import ru.spbau.mit.circuit.logic.math.variables.Variable;

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

    public static void main(String[] args) throws ZeroDeterminantException {

        Variable<Expression> a = new ExpressionVariable("a");
        Variable<Expression> b = new ExpressionVariable("b");

        List<Variable<Expression>> l = Arrays.asList(a, b);

        LinearSystem<Expression,
                Vector<Expression, Variable<Expression>>,
                Expression> system = new LinearSystem<>(2);

        Vector<Expression, Variable<Expression>> v1 = new Vector<>(l, Expressions.zero());
        Vector<Expression, Variable<Expression>> v2 = new Vector<>(l, Expressions.zero());

        v1.setCoefficients(Arrays.asList(Expressions.variable(2, "R"), Expressions.zero()));
        v2.setCoefficients(Arrays.asList(Expressions.variable(1, "R"), Expressions.variable(1,
                "R")));

        Equation<Expression, Vector<Expression, Variable<Expression>>, Expression> eq1
                = new Equation<>(v1, Expressions.variable("R"));

        Equation<Expression, Vector<Expression, Variable<Expression>>, Expression> eq2
                = new Equation<>(v2, Expressions.variable("R"));

        system.addEquation(eq1);
        system.addEquation(eq2);

        System.out.println(system);
        system.solve();
        System.out.println(system);
    }
}
