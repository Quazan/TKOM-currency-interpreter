package tkom.ast.nodes;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tkom.ast.Node;
import tkom.utils.NodeType;
import tkom.ast.Statement;

@Getter
@Setter
@ToString
public class AssignStatement implements Statement, Node {

    private String identifier;

    private ExpressionNode assignable;

    public AssignStatement(String identifier, ExpressionNode assignable) {
        this.identifier = identifier;
        this.assignable = assignable;
    }

    @Override
    public NodeType getType() {
        return NodeType.ASSIGN_STATEMENT;
    }
}
