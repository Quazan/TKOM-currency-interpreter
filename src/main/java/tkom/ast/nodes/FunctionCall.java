package tkom.ast.nodes;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tkom.ast.Expression;
import tkom.ast.Statement;
import tkom.ast.Value;
import tkom.error.RuntimeEnvironmentException;
import tkom.execution.Environment;
import tkom.utils.NodeType;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class FunctionCall implements Statement, Expression {

    private final String identifier;
    private final List<Expression> arguments;

    public FunctionCall(String identifier) {
        this.identifier = identifier;
        this.arguments = new ArrayList<>();
    }

    public void addArgument(Expression expression) {
        arguments.add(expression);
    }

    @Override
    public NodeType getType() {
        return NodeType.FUNCTION_CALL;
    }

    @Override
    public Value evaluate(Environment environment) throws RuntimeEnvironmentException {
        return execute(environment);
    }

    @Override
    public Value execute(Environment environment) throws RuntimeEnvironmentException {

        Function function = environment.getFunction(identifier);

        if (function == null) {
            throw new RuntimeEnvironmentException("Undefined Reference to: " + identifier);
        }

        return function.execute(environment, arguments);
    }
}
