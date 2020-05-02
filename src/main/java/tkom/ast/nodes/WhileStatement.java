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

    private boolean checkCondition(Value condition) throws RuntimeEnvironmentException {
        switch (condition.getType()) {
            case INT:
                if (((IntNode) condition).getValue() != 0) {
                    return true;
                } else {
                    return false;
                }
            case DOUBLE:
                if (((DoubleNode) condition).getValue() != 0) {
                    return true;
                } else {
                    return false;
                }
            case CURRENCY:
                if (!((Currency) condition).getValue().equals(BigDecimal.ZERO)) {
                    return true;
                } else {
                    return false;
                }
            default:
                throw new RuntimeEnvironmentException();
        }
    }

    @Override
    public NodeType getType() {
        return NodeType.WHILE_STATEMENT;
    }

    @Override
    public Value execute(Environment environment) throws UndefinedReferenceException, RuntimeEnvironmentException {
        environment.createNewLocalScope();

        while(checkCondition(condition.evaluate(environment))) {
            whileBlock.execute(environment);
        }

        environment.destroyScope();
        return new IntNode(0);
    }


}
