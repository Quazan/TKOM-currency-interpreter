package tkom.ast.nodes;

import lombok.Getter;
import lombok.ToString;
import tkom.ast.Value;
import tkom.ast.Node;
import tkom.ast.Statement;
import tkom.error.RuntimeEnvironmentException;
import tkom.execution.Environment;
import tkom.utils.ExecuteStatus;
import tkom.utils.NodeType;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class StatementBlock implements Node {

    private final List<Statement> statements;

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

    public ExecuteOut execute(Environment environment) throws RuntimeEnvironmentException {
        for (Statement statement : statements) {

            if (statement.getType() == NodeType.RETURN_STATEMENT) {
                return statement.execute(environment);
            }

            ExecuteOut ret = statement.execute(environment);
            if (ret.isReturnCall()) {
                return ret;
            }
        }

        return new ExecuteOut(ExecuteStatus.NORMAL);
    }
}
