package tkom.ast.nodes;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tkom.ast.Expression;
import tkom.ast.Node;
import tkom.ast.Value;
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
    public Value evaluate(Environment environment) throws UndefinedReferenceException {
        Value leftOperand = operands.get(0).evaluate(environment);;

        for(int i = 1; i < operands.size(); i++) {
            Value rightOperand = operands.get(i).evaluate(environment);

            switch (operator) {
                case EQUALITY:
                case INEQUALITY:
                case LESS:
                case LESS_OR_EQUAL:
                case GREATER:
                case GREATER_OR_EQUAL:
                case AND:
                case OR:
                    return new IntNode(1);
                default:
                    throw new RuntimeException();
            }
        }

        return new IntNode(0);
    }
}
