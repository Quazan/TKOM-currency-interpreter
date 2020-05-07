package tkom.ast.nodes;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tkom.ast.Expression;
import tkom.ast.Value;
import tkom.error.RuntimeEnvironmentException;
import tkom.execution.Environment;
import tkom.utils.NodeType;

@Getter
@ToString
public class Variable implements Expression {

    private final String identifier;

    public Variable(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public NodeType getType() {
        return NodeType.VARIABLE;
    }

    @Override
    public Value evaluate(Environment environment) throws RuntimeEnvironmentException {
        return environment.getVariable(identifier);
    }
}
