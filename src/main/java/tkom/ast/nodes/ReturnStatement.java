package tkom.ast.nodes;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tkom.ast.Node;
import tkom.utils.NodeType;
import tkom.ast.Statement;

@Getter
@Setter
@ToString
public class ReturnStatement implements Statement, Node {

    public Expression expression;

    @Override
    public NodeType getType() {
        return NodeType.RETURN_STATEMENT;
    }
}
