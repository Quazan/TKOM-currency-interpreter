package tkom.ast.nodes;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tkom.ast.Expression;
import tkom.ast.Node;
import tkom.utils.NodeType;

@Getter
@Setter
@ToString
public class IntNode implements Node, Expression {

    private int value;

    public IntNode(int value) {
        this.value = value;
    }

    @Override
    public NodeType getType() {
        return NodeType.INT;
    }
}
