package tkom.ast.nodes;

import lombok.*;
import tkom.ast.Expression;
import tkom.ast.Value;
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
            if (getReturnType().toUpperCase().equals(NodeType.INT.toString())) {
                environment.addVariable(getIdentifier(), new IntNode(0));
            } else if (getReturnType().toUpperCase().equals(NodeType.DOUBLE.toString())) {
                environment.addVariable(getIdentifier(), new DoubleNode(0));
            } else if (environment.containsCurrency(getReturnType())) {
                environment.addVariable(getIdentifier(), new Currency(new BigDecimal(0), getReturnType(), environment.getExchangeRates()));
            }

            return new IntNode(0);
        }

        Value assign = assignable.evaluate(environment);

        if (getReturnType().toUpperCase().equals(assign.getType().toString())) {
            environment.addVariable(getIdentifier(), assign);
        } else if (getReturnType().toUpperCase().equals(NodeType.DOUBLE.toString()) && assign.getType() == NodeType.INT) {
            environment.addVariable(getIdentifier(), new DoubleNode(((IntNode) assign).getValue()));
        } else if (environment.containsCurrency(getReturnType()) && assign.getType() == NodeType.INT) {
            environment.addVariable(getIdentifier(), new Currency(new BigDecimal(((IntNode) assign).getValue()), getReturnType(), environment.getExchangeRates()));
        } else if (environment.containsCurrency(getReturnType()) && assign.getType() == NodeType.DOUBLE) {
            environment.addVariable(getIdentifier(), new Currency(new BigDecimal(String.valueOf(((DoubleNode) assign).getValue())), getReturnType(), environment.getExchangeRates()));
        } else if (environment.containsCurrency(getReturnType()) && assign.getType() == NodeType.CURRENCY) {
            environment.addVariable(getIdentifier(), new Currency(environment.getExchangeRates().toCurrency(getReturnType(), ((Currency) assign).getValue()), getReturnType(), environment.getExchangeRates()));
        } else {
            throw new RuntimeEnvironmentException("Cannot assign " + assign.getType()
                    + " to " + getReturnType().toUpperCase());
        }

        return new IntNode(0);
    }
}
