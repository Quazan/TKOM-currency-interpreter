package tkom.ast.nodes;

import lombok.Data;
import tkom.ast.Statement;

@Data
public class IfStatement implements Statement {

    private Condition condition;

    private StatementBlock trueBlock;

    private StatementBlock falseBlock;

}
