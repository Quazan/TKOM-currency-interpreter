package tkom.ast.nodes;

import lombok.Data;
import tkom.ast.Node;
import tkom.utils.NodeType;
import tkom.ast.Statement;

@Data
public class IfStatement implements Statement, Node {

    private Condition condition;

    private StatementBlock trueBlock;

    private StatementBlock falseBlock;

    @Override
    public NodeType getType() {
        return NodeType.IF_STATEMENT;
    }
}
