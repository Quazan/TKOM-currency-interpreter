package tkom.ast.nodes;

import lombok.Data;
import tkom.ast.Node;
import tkom.utils.NodeType;
import tkom.ast.Statement;

@Data
public class ReturnStatement implements Statement, Node {

    public Expression expression;

    @Override
    public NodeType getType() {
        return NodeType.RETURN_STATEMENT;
    }
}
