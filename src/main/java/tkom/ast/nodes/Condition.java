package tkom.ast.nodes;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tkom.ast.Value;
import tkom.ast.Expression;
import tkom.error.RuntimeEnvironmentException;
import tkom.execution.Environment;
import tkom.utils.NodeType;
import tkom.utils.TokenType;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class Condition implements Expression {

    @Setter
    boolean negated = false;

    @Setter
    private TokenType operator;

    private final List<Expression> operands;

    private boolean isIntToInt(Value leftOperand, Value rightOperand) {
        return Value.isInt(leftOperand) && Value.isInt(rightOperand);
    }

    private boolean isDoubleToDouble(Value leftOperand, Value rightOperand) {
        return Value.isDouble(leftOperand) && Value.isDouble(rightOperand);
    }

    private boolean isCurrencyToCurrency(Value leftOperand, Value rightOperand) {
        return Value.isCurrency(leftOperand) && Value.isCurrency(rightOperand);
    }

    private boolean isBoolToBool(Value leftOperand, Value rightOperand) {
        return Value.isBool(leftOperand) && Value.isBool(rightOperand);
    }

    private boolean isIntToDouble(Value leftOperand, Value rightOperand) {
        return Value.isInt(leftOperand) && Value.isDouble(rightOperand);
    }

    private boolean isDoubleToInt(Value leftOperand, Value rightOperand) {
        return Value.isDouble(leftOperand) && Value.isInt(rightOperand);
    }

    private BoolNode isEqual(Value leftOperand, Value rightOperand) throws RuntimeEnvironmentException {
        if (isIntToInt(leftOperand, rightOperand)) {
            return new BoolNode(Value.getIntValue(leftOperand) == Value.getIntValue(rightOperand));
        } else if (isDoubleToDouble(leftOperand, rightOperand))  {
            return new BoolNode(Value.getDoubleValue(leftOperand) == Value.getDoubleValue(rightOperand));
        } else if (isBoolToBool(leftOperand, rightOperand)) {
            return new BoolNode(Value.getBoolValue(leftOperand) == Value.getBoolValue(rightOperand));
        } else if (isCurrencyToCurrency(leftOperand, rightOperand)) {
            return new BoolNode(Value.getCurrencyValue(leftOperand).equals(Value.getCurrencyValue(rightOperand)));
        } else if (isIntToDouble(leftOperand, rightOperand)) {
            return new BoolNode(Value.getIntValue(leftOperand) == Value.getDoubleValue(rightOperand));
        } else if (isDoubleToInt(leftOperand, rightOperand)) {
            return new BoolNode(Value.getDoubleValue(leftOperand) == Value.getIntValue(rightOperand));
        } else {
            throw new RuntimeEnvironmentException("Cannot compare " + leftOperand.getType()
                    + " to " + rightOperand.getType());
        }
    }


    private Value isLess(Value leftOperand, Value rightOperand) throws RuntimeEnvironmentException {
        if (isCurrencyToCurrency(leftOperand, rightOperand)) {
            return new BoolNode(Value.getCurrencyValue(leftOperand).compareTo(Value.getCurrencyValue(rightOperand)) < 0);
        } else if (isIntToInt(leftOperand, rightOperand)) {
            return new BoolNode(Value.getIntValue(leftOperand) < Value.getIntValue(rightOperand));
        } else if (isDoubleToDouble(leftOperand, rightOperand)) {
            return new BoolNode(Value.getDoubleValue(leftOperand) < Value.getDoubleValue(rightOperand));
        } else if (isIntToDouble(leftOperand, rightOperand)) {
            return new BoolNode(Value.getIntValue(leftOperand) < Value.getDoubleValue(rightOperand));
        } else if (isDoubleToInt(leftOperand, rightOperand)) {
            return new BoolNode(Value.getDoubleValue(leftOperand) < Value.getIntValue(rightOperand));
        } else {
            throw new RuntimeEnvironmentException("Cannot compare " + leftOperand.getType()
                    + " to " + rightOperand.getType());
        }
    }

    private Value isLessOrEqual(Value leftOperand, Value rightOperand) throws RuntimeEnvironmentException {
        if (isCurrencyToCurrency(leftOperand, rightOperand)) {
            return new BoolNode(Value.getCurrencyValue(leftOperand).compareTo(Value.getCurrencyValue(rightOperand)) <= 0);
        } else if (isIntToInt(leftOperand, rightOperand)) {
            return new BoolNode(Value.getIntValue(leftOperand) <= Value.getIntValue(rightOperand));
        } else if (isDoubleToDouble(leftOperand, rightOperand)) {
            return new BoolNode(Value.getDoubleValue(leftOperand) <= Value.getDoubleValue(rightOperand));
        } else if (isIntToDouble(leftOperand, rightOperand)) {
            return new BoolNode(Value.getIntValue(leftOperand) <= Value.getDoubleValue(rightOperand));
        } else if (isDoubleToInt(leftOperand, rightOperand)) {
            return new BoolNode(Value.getDoubleValue(leftOperand) <= Value.getIntValue(rightOperand));
        } else {
            throw new RuntimeEnvironmentException("Cannot compare " + leftOperand.getType()
                    + " to " + rightOperand.getType());
        }
    }

    private Value isGreater(Value leftOperand, Value rightOperand) throws RuntimeEnvironmentException {
        if (isCurrencyToCurrency(leftOperand, rightOperand)) {
            return new BoolNode(Value.getCurrencyValue(leftOperand).compareTo(Value.getCurrencyValue(rightOperand)) > 0);
        } else if (isIntToInt(leftOperand, rightOperand)) {
            return new BoolNode(Value.getIntValue(leftOperand) > Value.getIntValue(rightOperand));
        } else if (isDoubleToDouble(leftOperand, rightOperand)) {
            return new BoolNode(Value.getDoubleValue(leftOperand) > Value.getDoubleValue(rightOperand));
        } else if (isIntToDouble(leftOperand, rightOperand)) {
            return new BoolNode(Value.getIntValue(leftOperand) > Value.getDoubleValue(rightOperand));
        } else if (isDoubleToInt(leftOperand, rightOperand)) {
            return new BoolNode(Value.getDoubleValue(leftOperand) > Value.getIntValue(rightOperand));
        } else {
            throw new RuntimeEnvironmentException("Cannot compare " + leftOperand.getType()
                    + " to " + rightOperand.getType());
        }
    }

    private Value isGreaterOrEqual(Value leftOperand, Value rightOperand) throws RuntimeEnvironmentException {
        if (isCurrencyToCurrency(leftOperand, rightOperand)) {
            return new BoolNode(Value.getCurrencyValue(leftOperand).compareTo(Value.getCurrencyValue(rightOperand)) >= 0);
        } else if (isIntToInt(leftOperand, rightOperand)) {
            return new BoolNode(Value.getIntValue(leftOperand) >= Value.getIntValue(rightOperand));
        } else if (isDoubleToDouble(leftOperand, rightOperand)) {
            return new BoolNode(Value.getDoubleValue(leftOperand) >= Value.getDoubleValue(rightOperand));
        } else if (isIntToDouble(leftOperand, rightOperand)) {
            return new BoolNode(Value.getIntValue(leftOperand) >= Value.getDoubleValue(rightOperand));
        } else if (isDoubleToInt(leftOperand, rightOperand)) {
            return new BoolNode(Value.getDoubleValue(leftOperand) >= Value.getIntValue(rightOperand));
        } else {
            throw new RuntimeEnvironmentException("Cannot compare " + leftOperand.getType()
                    + " to " + rightOperand.getType());
        }
    }

    private Value and(Value leftOperand, Value rightOperand) {
        BoolNode left = new BoolNode(leftOperand);
        BoolNode right = new BoolNode(rightOperand);
        return new BoolNode(left.isValue() && right.isValue());
    }

    private Value or(Value leftOperand, Value rightOperand) {
        BoolNode left = new BoolNode(leftOperand);
        BoolNode right = new BoolNode(rightOperand);
        return new BoolNode(left.isValue() || right.isValue());
    }

    private Value negate(Value operand) {
        BoolNode op = new BoolNode(operand);
        return new BoolNode(!op.isValue());
    }

    public Condition() {
        this.operands = new ArrayList<>();
    }

    public void addOperand(Expression operand) {
        this.operands.add(operand);
    }

    public boolean isNegated() {
        return negated;
    }

    @Override
    public NodeType getType() {
        return NodeType.CONDITION;
    }

    @Override
    public Value evaluate(Environment environment) throws RuntimeEnvironmentException {
        Value leftOperand = operands.get(0).evaluate(environment);

        for (int i = 1; i < operands.size(); i++) {
            Value rightOperand = operands.get(i).evaluate(environment);

            switch (operator) {
                case EQUALITY:
                    leftOperand = isEqual(leftOperand, rightOperand);
                    break;
                case INEQUALITY:
                    leftOperand = negate(isEqual(leftOperand, rightOperand));
                    break;
                case LESS:
                    leftOperand = isLess(leftOperand, rightOperand);
                    break;
                case LESS_OR_EQUAL:
                    leftOperand = isLessOrEqual(leftOperand, rightOperand);
                    break;
                case GREATER:
                    leftOperand = isGreater(leftOperand, rightOperand);
                    break;
                case GREATER_OR_EQUAL:
                    leftOperand = isGreaterOrEqual(leftOperand, rightOperand);
                    break;
                case AND:
                    leftOperand = and(leftOperand, rightOperand);
                    break;
                case OR:
                    leftOperand = or(leftOperand, rightOperand);
                    break;
            }
        }

        if (isNegated()) {
            leftOperand = negate(leftOperand);
        }

        return leftOperand;
    }
}
