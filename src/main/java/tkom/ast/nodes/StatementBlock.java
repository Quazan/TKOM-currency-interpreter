package tkom.ast.nodes;

import lombok.Data;
import tkom.ast.Node;
import tkom.ast.NodeType;
import tkom.ast.Statement;

import java.util.ArrayList;
import java.util.List;

@Data
public class StatementBlock implements Node {

    List<Statement> statements;

    public StatementBlock() {
        statements = new ArrayList<>();
    }

    public void addStatement(Statement statement) {
        statements.add(statement);
    }

    @Override
    public NodeType getType() {
        return NodeType.STATEMENT_BLOCK;
    }
}
