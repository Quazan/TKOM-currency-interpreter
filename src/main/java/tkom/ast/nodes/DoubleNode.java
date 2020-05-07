package tkom.ast.nodes;

import lombok.Getter;
import tkom.ast.ArithmeticValue;
import tkom.ast.Value;
import tkom.ast.Expression;
import tkom.error.RuntimeEnvironmentException;
import tkom.execution.Environment;
import tkom.utils.NodeType;

@Getter
public class DoubleNode implements Expression, ArithmeticValue {

    private final double value;

    public DoubleNode(double value) {
        this.value = value;
    }

    @Override
    public NodeType getType() {
        return NodeType.DOUBLE;
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
                return new DoubleNode(value + Value.getIntValue(rightOperand));
            case DOUBLE:
                return new DoubleNode(value + Value.getDoubleValue(rightOperand));
            default:
                throw new RuntimeEnvironmentException("Cannot add " + rightOperand.getType() +
                        " to " + getType());
        }
    }

    @Override
    public ArithmeticValue subtract(Value rightOperand) throws RuntimeEnvironmentException {
        switch (rightOperand.getType()) {
            case INT:
                return new DoubleNode(value - Value.getIntValue(rightOperand));
            case DOUBLE:
                return new DoubleNode(value - Value.getDoubleValue(rightOperand));
            default:
                throw new RuntimeEnvironmentException("Cannot subtract " + rightOperand.getType() +
                        " from " + getType());
        }
    }

    @Override
    public ArithmeticValue multiply(Value rightOperand) throws RuntimeEnvironmentException {
        switch (rightOperand.getType()) {
            case INT:
                return new DoubleNode(value * Value.getIntValue(rightOperand));
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
        if(ArithmeticValue.isZero(rightOperand)) {
            throw new RuntimeEnvironmentException("Cannot divide by 0");
        }

        switch (rightOperand.getType()) {
            case INT:
                return new DoubleNode(value / Value.getIntValue(rightOperand));
            case DOUBLE:
                return new DoubleNode(value / Value.getDoubleValue(rightOperand));
            default:
                throw new RuntimeEnvironmentException("Cannot divide " + getType() +
                        " by " + rightOperand.getType());
        }
    }
}
