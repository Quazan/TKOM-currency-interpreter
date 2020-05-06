package tkom.ast.nodes;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tkom.ast.Node;
import tkom.ast.Value;
import tkom.error.RuntimeEnvironmentException;
import tkom.error.UndefinedReferenceException;
import tkom.execution.Environment;
import tkom.utils.NodeType;
import tkom.ast.Statement;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
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

    public Value execute(Environment environment) throws UndefinedReferenceException, RuntimeEnvironmentException {
        Value ret = new IntNode(0);
        for (Statement statement : statements) {

            if(statement.getType() == NodeType.RETURN_STATEMENT) {
                return statement.execute(environment);
            }

            ret = statement.execute(environment);

            if(ret.getType() == NodeType.RETURN_CALL) {
                return ret;
            }
        }

        return ret;
    }
}
