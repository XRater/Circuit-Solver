package ru.spbau.mit.circuit.ui;

import org.apache.commons.lang3.math.NumberUtils;

import ru.spbau.mit.circuit.logic.math.expressions.Expressions;
import ru.spbau.mit.circuit.model.circuitObjects.elements.Element;

public class InputParser {

    public static void parseInput(Element element, String s) {
        if (NumberUtils.isNumber(s)) {
            element.setCharacteristicValue(Expressions.constant(Double.parseDouble(s)));
        }

        if (s.matches("[a-zA-Z]") || s.matches("[a-zA-Z]_/d")) {
            element.setCharacteristicValue(Expressions.variable(s));
        }
    }

}
