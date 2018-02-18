package ru.spbau.mit.circuit.ui.parser;


import org.apache.commons.lang3.math.NumberUtils;

import ru.spbau.mit.circuit.logic.math.expressions.Expression;
import ru.spbau.mit.circuit.logic.math.expressions.Expressions;

public class ValueParser {

    public static Expression convert(String input) {
        if (NumberUtils.isNumber(input)) {
            return Expressions.constant(Double.parseDouble(input));
        } else {
//            if (input.matches("[a-zA-Z]_\\d+") || input.matches("[a-zA-Z]")) {
//                Expressions.variable();
//            }
        }
        throw new RuntimeException();
    }

}
