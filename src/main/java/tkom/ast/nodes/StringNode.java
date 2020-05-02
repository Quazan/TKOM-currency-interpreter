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
public class StringNode implements Node, Expression {

    private String value;

    public StringNode(String value) {
        this.value = value;
    }

    @Override
    public NodeType getType() {
        return NodeType.STRING;
    }
}
