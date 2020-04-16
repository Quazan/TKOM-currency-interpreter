package tkom.ast.nodes;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import tkom.ast.Node;
import tkom.utils.NodeType;
import tkom.ast.Statement;

@Getter
@Setter
public class IfStatement implements Statement, Node {

    private Condition condition;

    private StatementBlock trueBlock;

    private StatementBlock falseBlock;

    @Override
    public NodeType getType() {
        return NodeType.IF_STATEMENT;
    }
}
