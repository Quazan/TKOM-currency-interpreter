package tkom.ast.nodes;

import tkom.ast.Node;
import tkom.ast.NodeType;
import tkom.ast.Statement;

import java.util.List;

public class StatementBlock implements Node {

    List<Statement> statements;

    @Override
    public NodeType getType() {
        return NodeType.STATEMENT_BLOCK;
    }
}
