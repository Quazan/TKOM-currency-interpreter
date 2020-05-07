package tkom.ast.nodes;

import lombok.Getter;
import lombok.ToString;
import tkom.ast.ArithmeticValue;
import tkom.ast.Value;
import tkom.ast.Expression;
import tkom.error.RuntimeEnvironmentException;
import tkom.execution.Environment;
import tkom.utils.NodeType;
import tkom.utils.TokenType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class ExpressionNode implements Expression {

    private final List<TokenType> operations;
    private final List<Expression> operands;

    private ArithmeticValue executeOperation(TokenType operation,
                                             ArithmeticValue leftOperand,
                                             ArithmeticValue rightOperand) throws RuntimeEnvironmentException {
        switch (operation) {
            case DIVIDE:
                return leftOperand.divide(rightOperand);
            case MULTIPLY:
                return leftOperand.multiply(rightOperand);
            case PLUS:
                return leftOperand.add(rightOperand);
            case MINUS:
                return leftOperand.subtract(rightOperand);
            default:
                throw new RuntimeEnvironmentException("Invalid operation: " + operation);
        }
    }

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
    public Value evaluate(Environment environment) throws RuntimeEnvironmentException {
        int currentIndex = 0;
        ArithmeticValue leftOperand = (ArithmeticValue) operands.get(currentIndex).evaluate(environment);

        for (TokenType operation : operations) {
            currentIndex++;
            ArithmeticValue rightOperand = (ArithmeticValue) operands.get(currentIndex).evaluate(environment);

            leftOperand = executeOperation(operation, leftOperand, rightOperand);
        }

        return leftOperand;
    }
}
