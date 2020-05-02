package tkom.ast.nodes;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tkom.ast.Value;
import tkom.error.UndefinedReferenceException;
import tkom.execution.Environment;
import tkom.utils.NodeType;
import tkom.ast.Statement;

@Getter
@Setter
@ToString
public class AssignStatement implements Statement {

    private String identifier;

    private ExpressionNode assignable;

    public AssignStatement(String identifier, ExpressionNode assignable) {
        this.identifier = identifier;
        this.assignable = assignable;
    }

    @Override
    public NodeType getType() {
        return NodeType.ASSIGN_STATEMENT;
    }

    @Override
    public Value execute(Environment environment) throws UndefinedReferenceException {
        Value value = environment.getVariable(identifier);

        Value assign = assignable.evaluate(environment);

        //TODO castowanie typu
        if(assign.getType() == value.getType()) {
            environment.setVariable(identifier, assign);
        }

        return new IntNode(0);
    }
}
