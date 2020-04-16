package tkom.ast.nodes;

import lombok.Data;
import tkom.ast.Node;
import tkom.utils.NodeType;
import tkom.ast.Statement;

@Data
public class WhileStatement implements Statement, Node {

    private Condition condition;

    private StatementBlock whileBlock;

    @Override
    public NodeType getType() {
        return NodeType.WHILE_STATEMENT;
    }
}
