package tkom.ast.nodes;

import lombok.*;
import tkom.ast.Value;
import tkom.error.RuntimeEnvironmentException;
import tkom.error.UndefinedReferenceException;
import tkom.execution.Environment;
import tkom.utils.NodeType;
import tkom.ast.Statement;

@Getter
@Setter
@ToString
public class InitStatement extends Signature implements Statement{

    private ExpressionNode assignable;

    public InitStatement(String returnType, String identifier) {
        super(returnType, identifier);
    }

    @Override
    public NodeType getType() {
        return NodeType.INIT_STATEMENT;
    }

    @Override
    public Value execute(Environment environment) throws UndefinedReferenceException, RuntimeEnvironmentException {
        Value assign = assignable.evaluate(environment);

        //TODO check if they are compatible
        if(getReturnType().toUpperCase().equals(assign.getType().toString())) {
            environment.addVariable(getIdentifier(), assign);
        }

        return new IntNode(0);
    }
}
