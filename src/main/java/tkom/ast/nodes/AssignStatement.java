package tkom.ast.nodes;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import tkom.ast.Node;
import tkom.utils.NodeType;
import tkom.ast.Statement;

@Getter
@Setter
public class AssignStatement implements Statement, Node {

    private String identifier;

    private Expression assignable;

    @Override
    public NodeType getType() {
        return NodeType.ASSIGN_STATEMENT;
    }
}
