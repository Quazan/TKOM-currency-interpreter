package tkom.ast.nodes;

import lombok.*;
import tkom.ast.Value;
import tkom.ast.Expression;
import tkom.error.RuntimeEnvironmentException;
import tkom.execution.Environment;
import tkom.utils.ExecuteStatus;
import tkom.utils.NodeType;
import tkom.ast.Statement;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class InitStatement extends Signature implements Statement {

    private Expression assignable;

    private Value getDefaultValue(Environment environment) throws RuntimeEnvironmentException {
        if (isReturnType(NodeType.INT)) {
            return new IntNode(0);
        } else if (isReturnType(NodeType.DOUBLE)) {
            return new DoubleNode(0);
        } else if (environment.containsCurrency(getReturnType())) {
            return new Currency(BigDecimal.ZERO, getReturnType(), environment.getExchangeRates());
        }

        throw new RuntimeEnvironmentException("Unexpected data type: " + getReturnType());
    }

    private Value initializeVariable(Environment environment, Value assign) throws RuntimeEnvironmentException {
        if (isReturnType(assign.getType())) {
            return assign;
        } else if (isReturnType(NodeType.DOUBLE) && Value.isInt(assign)) {
            return new DoubleNode(Value.getIntValue(assign));
        } else if (environment.containsCurrency(getReturnType())) {
            return new Currency(assign, getReturnType(), environment.getExchangeRates());
        } else {
            throw new RuntimeEnvironmentException("Cannot assign " + assign.getType()
                    + " to " + getReturnType().toUpperCase());
        }
    }

    public InitStatement(String returnType, String identifier) {
        super(returnType, identifier);
    }

    @Override
    public NodeType getType() {
        return NodeType.INIT_STATEMENT;
    }

    @Override
    public ExecuteOut execute(Environment environment) throws RuntimeEnvironmentException {
        if (assignable == null) {
            environment.addVariable(getIdentifier(), getDefaultValue(environment));
            return new ExecuteOut(ExecuteStatus.NORMAL);
        }

        Value assign = assignable.evaluate(environment);
        environment.addVariable(getIdentifier(), initializeVariable(environment, assign));

        return new ExecuteOut(ExecuteStatus.NORMAL);
    }
}
