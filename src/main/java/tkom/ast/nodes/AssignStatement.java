package tkom.ast.nodes;

import lombok.Data;
import tkom.ast.Node;
import tkom.utils.NodeType;
import tkom.ast.Statement;

@Data
public class AssignStatement implements Statement, Node {

    private String identifier;

    private Expression assignable;

    @Override
    public NodeType getType() {
        return NodeType.ASSIGN_STATEMENT;
    }
}
