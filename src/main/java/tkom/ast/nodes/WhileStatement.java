package tkom.ast.nodes;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tkom.ast.Value;
import tkom.ast.Statement;
import tkom.error.RuntimeEnvironmentException;
import tkom.execution.Environment;
import tkom.utils.ExecuteStatus;
import tkom.utils.NodeType;

@Getter
@Setter
@ToString
public class WhileStatement implements Statement {

    private Condition condition;

    private StatementBlock whileBlock;

    @Override
    public NodeType getType() {
        return NodeType.WHILE_STATEMENT;
    }

    @Override
    public ExecuteOut execute(Environment environment) throws RuntimeEnvironmentException {
        ExecuteOut ret = new ExecuteOut(ExecuteStatus.NORMAL);

        while (((BoolNode) condition.evaluate(environment)).isValue()) {
            environment.createNewLocalScope();
            ret = whileBlock.execute(environment);
            if (ret.isReturnCall()) {
                environment.destroyScope();
                return ret;
            }
            environment.destroyScope();
        }

        return ret;
    }


}
