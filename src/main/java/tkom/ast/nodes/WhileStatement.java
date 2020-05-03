package tkom.ast.nodes;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tkom.ast.Value;
import tkom.error.RuntimeEnvironmentException;
import tkom.error.UndefinedReferenceException;
import tkom.execution.Environment;
import tkom.utils.NodeType;
import tkom.ast.Statement;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class WhileStatement implements Statement{

    private Condition condition;

    private StatementBlock whileBlock;

    @Override
    public NodeType getType() {
        return NodeType.WHILE_STATEMENT;
    }

    @Override
    public Value execute(Environment environment) throws UndefinedReferenceException, RuntimeEnvironmentException {
        environment.createNewLocalScope();
        Value ret = new IntNode(0);

        while(((BoolNode) condition.evaluate(environment)).isValue()) {
            ret = whileBlock.execute(environment);
            if(ret.getType() == NodeType.RETURN_CALL) {
                return ret;
            }
        }

        environment.destroyScope();
        return ret;
    }


}
