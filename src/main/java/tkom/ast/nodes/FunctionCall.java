package tkom.ast.nodes;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tkom.ast.Expression;
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
public class FunctionCall implements Statement, Expression {

    private String identifier;
    List<ExpressionNode> arguments;

    public FunctionCall() {
        this.arguments = new ArrayList<>();
    }

    public FunctionCall(String identifier) {
        this.identifier = identifier;
        this.arguments = new ArrayList<>();
    }

    public void addArgument(ExpressionNode expressionNode) {
        arguments.add(expressionNode);
    }

    @Override
    public NodeType getType() {
        return NodeType.FUNCTION_CALL;
    }

    @Override
    public Value evaluate(Environment environment) throws UndefinedReferenceException, RuntimeEnvironmentException {
        return execute(environment);
    }

    @Override
    public Value execute(Environment environment) throws UndefinedReferenceException, RuntimeEnvironmentException {

        Function function = environment.getFunction(identifier);

        if(function == null) {
            throw  new UndefinedReferenceException();
        }

        return function.execute(environment, arguments);
    }
}
