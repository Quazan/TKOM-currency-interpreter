package tkom.ast.nodes;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tkom.ast.Expression;
import tkom.ast.Value;
import tkom.error.RuntimeEnvironmentException;
import tkom.error.UndefinedReferenceException;
import tkom.execution.Environment;
import tkom.utils.NodeType;
import tkom.utils.TokenType;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
            Value rightOperand = operands.get(currentIndex).evaluate(environment);

            switch (operation) {
                case DIVIDE:
                    leftOperand = divide(leftOperand, rightOperand, environment);
                   break;
                case MULTIPLY:
                    leftOperand = multiply(leftOperand, rightOperand, environment);
                    break;
                case PLUS:
                    leftOperand = add(leftOperand, rightOperand, environment);
                    break;
                case MINUS:
                    leftOperand = subtract(leftOperand, rightOperand, environment);
                    break;
            }
        }

        return leftOperand;
    }

    private Value divide(Value leftOperand, Value rightOperand, Environment environment) throws RuntimeEnvironmentException {
        if(leftOperand.getType() == NodeType.INT && rightOperand.getType() == NodeType.INT) {
            return new IntNode(Value.getIntValue(leftOperand) / Value.getIntValue(rightOperand));
        } else if (leftOperand.getType() == NodeType.INT && rightOperand.getType() == NodeType.DOUBLE) {
            return new DoubleNode(Value.getIntValue(leftOperand) / Value.getDoubleValue(rightOperand));
        } else if (leftOperand.getType() == NodeType.DOUBLE && rightOperand.getType() == NodeType.INT) {
            return new DoubleNode(Value.getDoubleValue(leftOperand) / Value.getIntValue(rightOperand));
        } else if(leftOperand.getType() == NodeType.DOUBLE && rightOperand.getType() == NodeType.DOUBLE) {
            return new DoubleNode(Value.getDoubleValue(leftOperand) / Value.getDoubleValue(rightOperand));
        }  else if(leftOperand.getType() == NodeType.CURRENCY && rightOperand.getType() == NodeType.INT) {
            return new Currency(new BigDecimal(String.valueOf(Value.getCurrencyValue(leftOperand)
                    .divide(BigDecimal.valueOf(Value.getIntValue(rightOperand)),5 ,RoundingMode.HALF_EVEN))), "EUR" , environment.getExchangeRates());
        } else if(leftOperand.getType() == NodeType.CURRENCY && rightOperand.getType() == NodeType.DOUBLE) {
            return new Currency(new BigDecimal(String.valueOf(Value.getCurrencyValue(leftOperand)
                    .divide(BigDecimal.valueOf(Value.getDoubleValue(rightOperand)),5 , RoundingMode.HALF_EVEN))), "EUR", environment.getExchangeRates());
        }
        throw new RuntimeEnvironmentException();
    }

    private Value multiply(Value leftOperand, Value rightOperand, Environment environment) throws RuntimeEnvironmentException {
        if(leftOperand.getType() == NodeType.INT && rightOperand.getType() == NodeType.INT) {
            return new IntNode(Value.getIntValue(leftOperand) * Value.getIntValue(rightOperand));
        } else if (leftOperand.getType() == NodeType.INT && rightOperand.getType() == NodeType.DOUBLE) {
            return new DoubleNode(Value.getIntValue(leftOperand) * Value.getDoubleValue(rightOperand));
        } else if (leftOperand.getType() == NodeType.DOUBLE && rightOperand.getType() == NodeType.INT) {
            return new DoubleNode(Value.getDoubleValue(leftOperand) * Value.getIntValue(rightOperand));
        } else if(leftOperand.getType() == NodeType.DOUBLE && rightOperand.getType() == NodeType.DOUBLE) {
            return new DoubleNode(Value.getDoubleValue(leftOperand) * Value.getDoubleValue(rightOperand));
        }  else if(leftOperand.getType() == NodeType.CURRENCY && rightOperand.getType() == NodeType.INT) {
            return new Currency(new BigDecimal(String.valueOf(Value.getCurrencyValue(leftOperand)
                    .multiply(BigDecimal.valueOf(Value.getIntValue(rightOperand))))), "EUR", environment.getExchangeRates());
        } else if(leftOperand.getType() == NodeType.CURRENCY && rightOperand.getType() == NodeType.DOUBLE) {
            return new Currency(new BigDecimal(String.valueOf(Value.getCurrencyValue(leftOperand)
                    .multiply(BigDecimal.valueOf(Value.getDoubleValue(rightOperand))))), "EUR", environment.getExchangeRates());
        }
        throw new RuntimeEnvironmentException();
    }

    private Value subtract(Value leftOperand, Value rightOperand, Environment environment) throws RuntimeEnvironmentException {
        if(leftOperand.getType() == NodeType.INT && rightOperand.getType() == NodeType.INT) {
            return new IntNode(Value.getIntValue(leftOperand) - Value.getIntValue(rightOperand));
        } else if (leftOperand.getType() == NodeType.INT && rightOperand.getType() == NodeType.DOUBLE) {
            return new DoubleNode(Value.getIntValue(leftOperand) - Value.getDoubleValue(rightOperand));
        } else if (leftOperand.getType() == NodeType.DOUBLE && rightOperand.getType() == NodeType.INT) {
            return new DoubleNode(Value.getDoubleValue(leftOperand) - Value.getIntValue(rightOperand));
        } else if(leftOperand.getType() == NodeType.DOUBLE && rightOperand.getType() == NodeType.DOUBLE) {
            return new DoubleNode(Value.getDoubleValue(leftOperand) - Value.getDoubleValue(rightOperand));
        } else if(leftOperand.getType() == NodeType.CURRENCY && rightOperand.getType() == NodeType.CURRENCY) {
            return new Currency(new BigDecimal(String.valueOf(
                    Value.getCurrencyValue(leftOperand).subtract(Value.getCurrencyValue(rightOperand)))
            ), "EUR", environment.getExchangeRates());
        }
        throw new RuntimeEnvironmentException();
    }

    public Value add(Value leftOperand, Value rightOperand, Environment environment) throws RuntimeEnvironmentException {
        if(leftOperand.getType() == NodeType.INT && rightOperand.getType() == NodeType.INT) {
            return new IntNode(Value.getIntValue(leftOperand) + Value.getIntValue(rightOperand));
        } else if (leftOperand.getType() == NodeType.INT && rightOperand.getType() == NodeType.DOUBLE) {
            return new DoubleNode(Value.getIntValue(leftOperand) + Value.getDoubleValue(rightOperand));
        } else if (leftOperand.getType() == NodeType.DOUBLE && rightOperand.getType() == NodeType.INT) {
            return new DoubleNode(Value.getDoubleValue(leftOperand) + Value.getIntValue(rightOperand));
        } else if(leftOperand.getType() == NodeType.DOUBLE && rightOperand.getType() == NodeType.DOUBLE) {
            return new DoubleNode(Value.getDoubleValue(leftOperand) + Value.getDoubleValue(rightOperand));
        } else if(leftOperand.getType() == NodeType.CURRENCY && rightOperand.getType() == NodeType.CURRENCY) {
            return new Currency(new BigDecimal(String.valueOf(
                    Value.getCurrencyValue(leftOperand).add(Value.getCurrencyValue(rightOperand)))
            ), "EUR", environment.getExchangeRates());
        }

        throw new RuntimeEnvironmentException();
    }
}
