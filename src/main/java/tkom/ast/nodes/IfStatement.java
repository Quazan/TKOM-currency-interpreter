package tkom.ast.nodes;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tkom.ast.Value;
import tkom.error.RuntimeEnvironmentException;
import tkom.execution.Environment;
import tkom.utils.ExecuteStatus;
import tkom.utils.NodeType;
import tkom.ast.Statement;

@Getter
@ToString
public class IfStatement implements Statement {

    private final Condition condition;

    @Setter
    private StatementBlock trueBlock;

    @Setter
    private StatementBlock falseBlock;

    private ExecuteOut executeIfStatement(Environment environment) throws RuntimeEnvironmentException {
        if (Value.getBoolValue(condition.evaluate(environment))) {
            return trueBlock.execute(environment);
        } else if (falseBlock != null) {
            return falseBlock.execute(environment);
        }

        return new ExecuteOut(ExecuteStatus.NORMAL);
    }

    public IfStatement(Condition condition) {
        this.condition = condition;
    }

    @Override
    public NodeType getType() {
        return NodeType.IF_STATEMENT;
    }


    @Override
    public ExecuteOut execute(Environment environment) throws RuntimeEnvironmentException {
        environment.createNewLocalScope();

        ExecuteOut ret = executeIfStatement(environment);

        environment.destroyScope();
        return ret;
    }
}
