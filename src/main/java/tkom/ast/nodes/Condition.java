package tkom.ast.nodes;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tkom.ast.ArithmeticValue;
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

    private ArithmeticValue castArithmeticType(Value operand) throws RuntimeEnvironmentException {
        if (Value.isBool(operand)) {
            throw new RuntimeEnvironmentException("Cannot compare " + operand.getType() +
                    " to ArithmeticValue");
        }

        return (ArithmeticValue) operand;
    }

    private Value executeOperation(TokenType operator, Value leftOperand, Value rightOperand) throws RuntimeEnvironmentException {
        switch (operator) {
            case EQUALITY:
                return leftOperand.isEqual(rightOperand);
            case INEQUALITY:
                return negate(leftOperand.isEqual(rightOperand));
            case LESS:
                return castArithmeticType(leftOperand).isLess(rightOperand);
            case LESS_OR_EQUAL:
                return castArithmeticType(leftOperand).isLessOrEqual(rightOperand);
            case GREATER:
                return castArithmeticType(leftOperand).isGrater(rightOperand);
            case GREATER_OR_EQUAL:
                return castArithmeticType(leftOperand).isGraterOrEqual(rightOperand);
            case AND:
                return and(leftOperand, rightOperand);
            case OR:
                return or(leftOperand, rightOperand);
            default:
                throw new RuntimeEnvironmentException(operator + "is not valid operation.");
        }
    }

    private Value and(Value leftOperand, Value rightOperand) throws RuntimeEnvironmentException {
        BoolNode left = new BoolNode(leftOperand);
        BoolNode right = new BoolNode(rightOperand);
        return left.and(right);
    }

    private Value or(Value leftOperand, Value rightOperand) throws RuntimeEnvironmentException {
        BoolNode left = new BoolNode(leftOperand);
        BoolNode right = new BoolNode(rightOperand);
        return left.or(right);
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

            leftOperand = executeOperation(operator, leftOperand, rightOperand);
        }

        if (isNegated()) {
            leftOperand = negate(leftOperand);
        }

        return leftOperand;
    }
}
