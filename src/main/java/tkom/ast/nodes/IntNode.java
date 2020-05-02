package tkom.ast.nodes;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tkom.ast.Expression;
import tkom.ast.Value;
import tkom.execution.Environment;
import tkom.utils.NodeType;

@Getter
@Setter
@ToString
public class IntNode implements Expression, Value {

    private int value;

    public IntNode(int value) {
        this.value = value;
    }

    @Override
    public NodeType getType() {
        return NodeType.INT;
    }

    @Override
    public Value evaluate(Environment environment) {
        return this;
    }

}
