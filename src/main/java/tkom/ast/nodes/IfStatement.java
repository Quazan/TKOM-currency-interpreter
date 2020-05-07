package tkom.ast.nodes;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tkom.ast.Value;
import tkom.error.RuntimeEnvironmentException;
import tkom.execution.Environment;
import tkom.utils.NodeType;
import tkom.ast.Statement;

@Getter
@Setter
@ToString
public class IfStatement implements Statement {

    private Condition condition;

    private StatementBlock trueBlock;

    private StatementBlock falseBlock;

    @Override
    public NodeType getType() {
        return NodeType.IF_STATEMENT;
    }

    @Override
    public Value execute(Environment environment) throws RuntimeEnvironmentException {
        environment.createNewLocalScope();

        Value ret = new IntNode(0);

        if (Value.getBoolValue(condition.evaluate(environment))) {
            ret = trueBlock.execute(environment);
        } else if (falseBlock != null) {
            ret = falseBlock.execute(environment);
        }

        environment.destroyScope();
        return ret;
    }
}
