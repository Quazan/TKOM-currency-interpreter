package tkom.ast.nodes;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tkom.ast.Value;
import tkom.error.RuntimeEnvironmentException;
import tkom.error.UndefinedReferenceException;
import tkom.execution.Environment;
import tkom.utils.NodeType;
import tkom.ast.Statement;

@Getter
@ToString
public class AssignStatement implements Statement {

    private final String identifier;

    private final ExpressionNode assignable;

    public AssignStatement(String identifier, ExpressionNode assignable) {
        this.identifier = identifier;
        this.assignable = assignable;
    }

    @Override
    public NodeType getType() {
        return NodeType.ASSIGN_STATEMENT;
    }

    @Override
    public Value execute(Environment environment) throws UndefinedReferenceException, RuntimeEnvironmentException {
        Value value = environment.getVariable(identifier);

        Value assign = assignable.evaluate(environment);

        if (assign.getType() == NodeType.CURRENCY && value.getType() == NodeType.CURRENCY) {
            environment.setVariable(identifier, new Currency(environment.getExchangeRates().toBaseCurrency(((Currency) value).getCurrencyType(), Value.getCurrencyValue(assign)), ((Currency) value).getCurrencyType(), environment.getExchangeRates()));
        } else if (assign.getType() == value.getType()) {
            environment.setVariable(identifier, assign);
        } else if (value.getType() == NodeType.DOUBLE && assign.getType() == NodeType.INT) {
            environment.setVariable(identifier, new DoubleNode(((IntNode) assign).getValue()));
        } else {
            throw new RuntimeEnvironmentException("Cannot assign " + assign.getType() + " to " + value.getType());
        }

        return new IntNode(0);
    }
}
