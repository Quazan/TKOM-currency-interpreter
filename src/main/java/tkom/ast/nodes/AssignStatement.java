package tkom.ast.nodes;

import lombok.Data;
import tkom.ast.Node;
import tkom.ast.NodeType;
import tkom.ast.Statement;

@Data
public class AssignStatement implements Statement, Node {

    private Variable variable;

    private Expression assignable;

    @Override
    public NodeType getType() {
        return NodeType.ASSIGN_STATEMENT;
    }
}
