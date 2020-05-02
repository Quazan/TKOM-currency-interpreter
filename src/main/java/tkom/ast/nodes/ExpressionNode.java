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
public class ExpressionNode implements Expression {

    private List<TokenType> operations;
    private List<Expression> operands;

    public ExpressionNode() {
        this.operations = new ArrayList<>();
        this.operands = new ArrayList<>();
    }

    public void addOperation(TokenType operationType) {
        operations.add(operationType);
    }

    public void addOperand(Expression operand) {
        operands.add(operand);
    }

    @Override
    public NodeType getType() {
        return NodeType.EXPRESSION;
    }

    @Override
    public Value evaluate(Environment environment) throws UndefinedReferenceException, RuntimeEnvironmentException {
        int currentIndex = 0;
        Value leftOperand = operands.get(currentIndex).evaluate(environment);

        //TODO overload operators
        for(TokenType operation : operations) {
            currentIndex++;
            switch (operation) {
                case DIVIDE:
                    leftOperand = divide((IntNode) leftOperand,(IntNode)  operands.get(currentIndex).evaluate(environment));
                   break;
                case MULTIPLY:
                    leftOperand = multiply((IntNode) leftOperand,(IntNode)  operands.get(currentIndex).evaluate(environment));
                    break;
                case PLUS:
                    leftOperand = add((IntNode) leftOperand,(IntNode)  operands.get(currentIndex).evaluate(environment));
                    break;
                case MINUS:
                    leftOperand = subtract((IntNode) leftOperand,(IntNode)  operands.get(currentIndex).evaluate(environment));;
                    break;
            }
        }

        return leftOperand;

    }

    private Value divide(IntNode leftOperand, IntNode rightOperand) {
        return  new IntNode(leftOperand.getValue() / rightOperand.getValue());
    }

    private Value multiply(IntNode leftOperand, IntNode rightOperand) {
        return  new IntNode(leftOperand.getValue() * rightOperand.getValue());
    }

    private Value subtract(IntNode leftOperand, IntNode rightOperand) {
        return  new IntNode(leftOperand.getValue() - rightOperand.getValue());
    }

    public Value add(IntNode leftOperand, IntNode rightOperand) {
        return  new IntNode(leftOperand.getValue() + rightOperand.getValue());
    }
}
