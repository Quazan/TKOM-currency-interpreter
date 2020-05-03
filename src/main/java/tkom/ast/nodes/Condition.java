package tkom.ast.nodes;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tkom.ast.Expression;
import tkom.ast.Node;
import tkom.ast.Value;
import tkom.error.RuntimeEnvironmentException;
import tkom.error.UndefinedReferenceException;
import tkom.execution.Environment;
import tkom.utils.NodeType;
import tkom.utils.TokenType;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class Condition implements Expression {

    boolean negated = false;

    private TokenType operator;

    private List<Expression> operands;

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
    public Value evaluate(Environment environment) throws UndefinedReferenceException, RuntimeEnvironmentException {
        Value leftOperand = operands.get(0).evaluate(environment);;

        for(int i = 1; i < operands.size(); i++) {
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
                default:
                    throw new RuntimeEnvironmentException();
            }
        }

        return new IntNode(0);
    }

    private Value isEqual(Value leftOperand, Value rightOperand) {

        return new IntNode(0);
    }


    private Value isLess(Value leftOperand, Value rightOperand) {

        return new IntNode(0);
    }

    private Value isLessOrEqual(Value leftOperand, Value rightOperand) {

        return new IntNode(0);
    }

    private Value isGreater(Value leftOperand, Value rightOperand) {

        return new IntNode(0);
    }

    private Value isGreaterOrEqual(Value leftOperand, Value rightOperand) {

        return new IntNode(0);
    }

    private Value and(Value leftOperand, Value rightOperand) {

        return new IntNode(0);
    }

    private Value or(Value leftOperand, Value rightOperand) {

        return new IntNode(0);
    }

    private Value negate(Value operand) {

        return new IntNode(0);
    }
}
