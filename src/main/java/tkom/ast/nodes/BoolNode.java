package tkom.ast.nodes;

import lombok.Getter;
import tkom.ast.Value;
import tkom.error.RuntimeEnvironmentException;
import tkom.utils.NodeType;

import java.math.BigDecimal;

public class BoolNode implements Value {

    @Getter
    private boolean value;

    private void checkOperandType(Value rightOperand) throws RuntimeEnvironmentException {
        if (!Value.isBool(rightOperand)) {
            throw new RuntimeEnvironmentException("Cannot compare " + rightOperand.getType() +
                    " to " + getType());
        }
    }

    public BoolNode(boolean value) {
        this.value = value;
    }

    public BoolNode(Value from) {
        switch (from.getType()) {
            case CURRENCY:
                this.value = !((Currency) from).getValue().equals(BigDecimal.ZERO);
                break;
            case DOUBLE:
                this.value = ((DoubleNode) from).getValue() != 0;
                break;
            case INT:
                this.value = ((IntNode) from).getValue() != 0;
                break;
            case BOOL:
                this.value = ((BoolNode) from).isValue();
                break;
        }
    }

    @Override
    public NodeType getType() {
        return NodeType.BOOL;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public BoolNode and(Value rightOperand) throws RuntimeEnvironmentException {
        checkOperandType(rightOperand);

        return new BoolNode(value && Value.getBoolValue(rightOperand));
    }

    public BoolNode or(Value rightOperand) throws RuntimeEnvironmentException {
        checkOperandType(rightOperand);

        return new BoolNode(value || Value.getBoolValue(rightOperand));
    }

    @Override
    public BoolNode isEqual(Value rightOperand) throws RuntimeEnvironmentException {
        checkOperandType(rightOperand);

        return new BoolNode(value == Value.getBoolValue(rightOperand));
    }
}
