package tkom.ast.nodes;

import lombok.Getter;
import lombok.Setter;
import tkom.ast.Expression;
import tkom.ast.Value;
import tkom.execution.Environment;
import tkom.utils.NodeType;

@Getter
public class StringNode implements Expression, Value {

    private final String value;

    public StringNode(String value) {
        this.value = value;
    }

    @Override
    public NodeType getType() {
        return NodeType.STRING;
    }

    @Override
    public Value evaluate(Environment environment) {
        return this;
    }

    @Override
    public String toString() {
        return value;
    }
}
