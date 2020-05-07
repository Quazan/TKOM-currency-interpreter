package tkom.ast;

import tkom.ast.nodes.BoolNode;
import tkom.ast.nodes.Currency;
import tkom.ast.nodes.DoubleNode;
import tkom.ast.nodes.IntNode;

import java.math.BigDecimal;

public interface Value extends Node {
    static int getIntValue(Value operand) {
        return ((IntNode) operand).getValue();
    }

    static double getDoubleValue(Value operand) {
        return ((DoubleNode) operand).getValue();
    }

    static BigDecimal getCurrencyValue(Value operand) {
        return ((Currency) operand).getValue();
    }

    static boolean getBoolValue(Value operand) {
        return ((BoolNode) operand).isValue();
    }
}
