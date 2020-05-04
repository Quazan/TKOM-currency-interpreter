package tkom.ast.nodes;

import lombok.Getter;
import tkom.ast.Expression;
import tkom.ast.Node;
import tkom.ast.Statement;
import tkom.ast.Value;
import tkom.error.RuntimeEnvironmentException;
import tkom.error.UndefinedReferenceException;
import tkom.execution.Environment;
import tkom.utils.NodeType;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PrintStatement implements Node, Statement {

    List<Expression> arguments;

    public PrintStatement() {
        this.arguments = new ArrayList<>();
    }

    public void addArgument(Expression argument) {
        arguments.add(argument);
    }

    @Override
    public Value execute(Environment environment) throws UndefinedReferenceException, RuntimeEnvironmentException {
        StringBuilder sb = new StringBuilder();

        for(Expression exp : arguments){
            sb.append(exp.evaluate(environment));
        }

        System.out.println(sb.toString());

        return new IntNode(0);
    }

    @Override
    public NodeType getType() {
        return NodeType.PRINT;
    }
}
