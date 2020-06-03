package tkom.ast.nodes;

import lombok.Getter;
import tkom.ast.Expression;
import tkom.ast.Node;
import tkom.ast.Statement;
import tkom.error.RuntimeEnvironmentException;
import tkom.execution.Environment;
import tkom.utils.ExecuteStatus;
import tkom.utils.NodeType;

import java.util.ArrayList;
import java.util.List;


public class PrintStatement implements Node, Statement {

    @Getter
    List<Expression> arguments;

    public PrintStatement() {
        this.arguments = new ArrayList<>();
    }

    public void addArgument(Expression argument) {
        arguments.add(argument);
    }

    @Override
    public ExecuteOut execute(Environment environment) throws RuntimeEnvironmentException {
        StringBuilder sb = new StringBuilder();

        for (Expression exp : arguments) {
            sb.append(exp.evaluate(environment));
        }

        System.out.println(sb.toString());

        return new ExecuteOut(ExecuteStatus.NORMAL);
    }

    @Override
    public NodeType getType() {
        return NodeType.PRINT;
    }
}
