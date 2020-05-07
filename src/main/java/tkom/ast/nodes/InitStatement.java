package tkom.ast.nodes;

import lombok.*;
import tkom.ast.Node;
import tkom.ast.Value;
import tkom.ast.Expression;
import tkom.error.RuntimeEnvironmentException;
import tkom.execution.Environment;
import tkom.utils.NodeType;
import tkom.ast.Statement;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class InitStatement extends Signature implements Statement {

    private Expression assignable;

    private void initializeDefaultValue(Environment environment) throws RuntimeEnvironmentException {
        if (isReturnType(NodeType.INT)) {
            environment.addVariable(getIdentifier(), new IntNode(0));
        } else if (isReturnType(NodeType.DOUBLE)) {
            environment.addVariable(getIdentifier(), new DoubleNode(0));
        } else if (environment.containsCurrency(getReturnType())) {
            environment.addVariable(getIdentifier(), new Currency(BigDecimal.ZERO,
                    getReturnType(), environment.getExchangeRates()));
        }
    }

    private void initializeVariable(Environment environment, Value assign) throws RuntimeEnvironmentException {
        if (isReturnType(assign.getType())) {
            environment.addVariable(getIdentifier(), assign);
        } else if (isReturnType(NodeType.DOUBLE) && Value.isInt(assign)) {
            environment.addVariable(getIdentifier(), new DoubleNode(((IntNode) assign).getValue()));
        } else if (environment.containsCurrency(getReturnType())) {
            environment.addVariable(getIdentifier(), new Currency(assign,
                    getReturnType(), environment.getExchangeRates()));
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
    public Value execute(Environment environment) throws RuntimeEnvironmentException {
        if (assignable == null) {
            initializeDefaultValue(environment);
            return new IntNode(0);
        }

        Value assign = assignable.evaluate(environment);

        initializeVariable(environment, assign);

        return new IntNode(0);
    }
}
