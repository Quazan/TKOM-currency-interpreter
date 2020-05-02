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

@Getter
@Setter
@ToString
public class Variable implements Expression {

    private String identifier;

    public Variable(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public NodeType getType() {
        return NodeType.VARIABLE;
    }

    @Override
    public Value evaluate(Environment environment) throws UndefinedReferenceException {
        return environment.getVariable(identifier);
    }
}
