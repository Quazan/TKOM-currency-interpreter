package tkom.ast.nodes;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import tkom.ast.Node;
import tkom.utils.NodeType;
import tkom.ast.Statement;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
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
