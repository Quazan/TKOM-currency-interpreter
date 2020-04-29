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
public class DoubleNode implements Node, Expression {

    private double value;

    public DoubleNode(double value) {
        this.value = value;
    }

    @Override
    public NodeType getType() {
        return NodeType.DOUBLE;
    }
}
