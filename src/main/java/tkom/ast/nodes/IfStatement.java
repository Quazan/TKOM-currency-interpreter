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

        ExecuteOut ret = new ExecuteOut(ExecuteStatus.NORMAL);

        if (Value.getBoolValue(condition.evaluate(environment))) {
            ret = trueBlock.execute(environment);
        } else if (falseBlock != null) {
            ret = falseBlock.execute(environment);
        }

        environment.destroyScope();
        return ret;
    }
}
