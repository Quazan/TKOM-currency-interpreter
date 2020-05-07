package tkom.ast.nodes;

import lombok.Getter;
import lombok.ToString;
import tkom.ast.Value;
import tkom.ast.Expression;
import tkom.ast.Statement;
import tkom.error.RuntimeEnvironmentException;
import tkom.execution.Environment;
import tkom.utils.NodeType;

@Getter
@ToString
public class AssignStatement implements Statement {

    private final String identifier;

    private final Expression assignable;

    public AssignStatement(String identifier, Expression assignable) {
        this.identifier = identifier;
        this.assignable = assignable;
    }

    @Override
    public NodeType getType() {
        return NodeType.ASSIGN_STATEMENT;
    }

    @Override
    public Value execute(Environment environment) throws RuntimeEnvironmentException {
        Value value = environment.getVariable(identifier);

        Value assign = assignable.evaluate(environment);

        if (Value.isCurrency(assign) && Value.isCurrency(value)) {
            environment.setVariable(identifier,
                    new Currency(assign, ((Currency) value).getCurrencyType(), environment.getExchangeRates()));
        } else if (assign.getType() == value.getType()) {
            environment.setVariable(identifier, assign);
        } else if (Value.isDouble(value) && Value.isInt(assign)) {
            environment.setVariable(identifier,
                    new DoubleNode(((IntNode) assign).getValue()));
        } else {
            throw new RuntimeEnvironmentException("Cannot assign " + assign.getType() + " to " + value.getType());
        }

        return new IntNode(0);
    }
}
