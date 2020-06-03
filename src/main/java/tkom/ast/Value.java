package tkom.ast;

import tkom.ast.nodes.BoolNode;
import tkom.ast.nodes.Currency;
import tkom.ast.nodes.DoubleNode;
import tkom.ast.nodes.IntNode;
import tkom.error.RuntimeEnvironmentException;
import tkom.utils.NodeType;

import java.math.BigDecimal;
import java.math.RoundingMode;

public interface Value extends Node {

    BoolNode isEqual(Value rightOperand) throws RuntimeEnvironmentException;

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

    static boolean isInt(Value operand) {
        return operand.getType() == NodeType.INT;
    }

    static boolean isDouble(Value operand) {
        return operand.getType() == NodeType.DOUBLE;
    }

    static boolean isCurrency(Value operand) {
        return operand.getType() == NodeType.CURRENCY;
    }

    static boolean isBool(Value operand) {
        return operand.getType() == NodeType.BOOL;
    }
}
