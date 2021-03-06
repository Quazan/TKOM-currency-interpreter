package tkom.ast.nodes;

import lombok.Getter;
import tkom.ast.ArithmeticValue;
import tkom.ast.Value;
import tkom.ast.Expression;
import tkom.error.RuntimeEnvironmentException;
import tkom.execution.Environment;
import tkom.utils.NodeType;

@Getter
public class IntNode implements Expression, ArithmeticValue {

    private final int value;

    public IntNode(int value) {
        this.value = value;
    }

    @Override
    public NodeType getType() {
        return NodeType.INT;
    }

    @Override
    public Value evaluate(Environment environment) {
        return this;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public ArithmeticValue add(Value rightOperand) throws RuntimeEnvironmentException {
        switch (rightOperand.getType()) {
            case INT:
                return new IntNode(value + Value.getIntValue(rightOperand));
            case DOUBLE:
                return new DoubleNode(value + Value.getDoubleValue(rightOperand));
            default:
                throw new RuntimeEnvironmentException("Cannot add " + rightOperand.getType() +
                        " to " + rightOperand.getType());
        }
    }

    @Override
    public ArithmeticValue subtract(Value rightOperand) throws RuntimeEnvironmentException {
        switch (rightOperand.getType()) {
            case INT:
                return new IntNode(value - Value.getIntValue(rightOperand));
            case DOUBLE:
                return new DoubleNode(value - Value.getDoubleValue(rightOperand));
            default:
                throw new RuntimeEnvironmentException("Cannot subtract " + rightOperand.getType() +
                        " from " + getType());
        }
    }

    public ArithmeticValue multiply(Value rightOperand) throws RuntimeEnvironmentException {
        switch (rightOperand.getType()) {
            case INT:
                return new IntNode(value * Value.getIntValue(rightOperand));
            case DOUBLE:
                return new DoubleNode(value * Value.getDoubleValue(rightOperand));
            case CURRENCY:
                return new Currency(ArithmeticValue.multiplyBigDecimals(this, rightOperand),
                        "EUR",
                        ((Currency) rightOperand).getExchangeRates());
            default:
                throw new RuntimeEnvironmentException("Cannot multiply " + getType() +
                        " by " + rightOperand.getType());
        }
    }

    @Override
    public ArithmeticValue divide(Value rightOperand) throws RuntimeEnvironmentException {
        if (ArithmeticValue.isZero(rightOperand)) {
            throw new RuntimeEnvironmentException("Cannot divide by 0");
        }

        switch (rightOperand.getType()) {
            case INT:
                return new IntNode(value / Value.getIntValue(rightOperand));
            case DOUBLE:
                return new DoubleNode(value / Value.getDoubleValue(rightOperand));
            default:
                throw new RuntimeEnvironmentException("Cannot divide " + getType() +
                        " by " + rightOperand.getType());
        }
    }

    @Override
    public BoolNode isLess(Value rightOperand) throws RuntimeEnvironmentException {
        switch (rightOperand.getType()) {
            case INT:
                return new BoolNode(value < Value.getIntValue(rightOperand));
            case DOUBLE:
                return new BoolNode(value < Value.getDoubleValue(rightOperand));
            default:
                throw new RuntimeEnvironmentException("Cannot compare " + rightOperand.getType() +
                        " to " + getType());
        }
    }

    @Override
    public BoolNode isLessOrEqual(Value rightOperand) throws RuntimeEnvironmentException {
        switch (rightOperand.getType()) {
            case INT:
                return new BoolNode(value <= Value.getIntValue(rightOperand));
            case DOUBLE:
                return new BoolNode(value <= Value.getDoubleValue(rightOperand));
            default:
                throw new RuntimeEnvironmentException("Cannot compare " + rightOperand.getType() +
                        " to " + getType());
        }
    }

    @Override
    public BoolNode isGrater(Value rightOperand) throws RuntimeEnvironmentException {
        switch (rightOperand.getType()) {
            case INT:
                return new BoolNode(value > Value.getIntValue(rightOperand));
            case DOUBLE:
                return new BoolNode(value > Value.getDoubleValue(rightOperand));
            default:
                throw new RuntimeEnvironmentException("Cannot compare " + rightOperand.getType() +
                        " to " + getType());
        }
    }

    @Override
    public BoolNode isGraterOrEqual(Value rightOperand) throws RuntimeEnvironmentException {
        switch (rightOperand.getType()) {
            case INT:
                return new BoolNode(value >= Value.getIntValue(rightOperand));
            case DOUBLE:
                return new BoolNode(value >= Value.getDoubleValue(rightOperand));
            default:
                throw new RuntimeEnvironmentException("Cannot compare " + rightOperand.getType() +
                        " to " + getType());
        }
    }

    @Override
    public BoolNode isEqual(Value rightOperand) throws RuntimeEnvironmentException {
        switch (rightOperand.getType()) {
            case INT:
                return new BoolNode(value == Value.getIntValue(rightOperand));
            case DOUBLE:
                return new BoolNode(value == Value.getDoubleValue(rightOperand));
            default:
                throw new RuntimeEnvironmentException("Cannot compare " + rightOperand.getType() +
                        " to " + getType());
        }
    }
}
