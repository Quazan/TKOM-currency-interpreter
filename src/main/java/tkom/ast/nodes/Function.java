package tkom.ast.nodes;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tkom.ast.Node;
import tkom.ast.Value;
import tkom.error.RuntimeEnvironmentException;
import tkom.error.UndefinedReferenceException;
import tkom.execution.Environment;
import tkom.utils.NodeType;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class Function extends Signature implements Node {

    private List<Signature> parameters;

    private StatementBlock statementBlock;

    public Function(String returnType, String identifier) {
        super(returnType, identifier);
        this.parameters = new ArrayList<>();
    }

    public void addParameter(Signature parameter) {
        parameters.add(parameter);
    }

    @Override
    public NodeType getType() {
        return NodeType.FUNCTION;
    }

    public Value execute(Environment environment, List<ExpressionNode> arguments) throws UndefinedReferenceException, RuntimeEnvironmentException {
        if(arguments.size() != parameters.size()) {
            throw new UndefinedReferenceException();
        }

        List<Value> argumentsValue = new ArrayList<>();
        for(ExpressionNode arg : arguments) {
            argumentsValue.add(arg.evaluate(environment));
        }

        environment.createNewScope();
        for(int i = 0; i < parameters.size(); i++){
            if(parameters.get(i).getReturnType().toUpperCase().equals(NodeType.DOUBLE.toString()) && argumentsValue.get(i).getType() == NodeType.INT) {
                argumentsValue.add(i, new DoubleNode(((IntNode) argumentsValue.get(i)).getValue()));
            }

            if(! (parameters.get(i).getReturnType().toUpperCase().equals(argumentsValue.get(i).getType().toString()) ||
                    (environment.containsCurrency(parameters.get(i).getReturnType()) && argumentsValue.get(i).getType() == NodeType.CURRENCY))) {
                throw new RuntimeEnvironmentException();
            }
            environment.addVariable(
                    parameters.get(i).getIdentifier(),
                    argumentsValue.get(i)
            );
        }

        Value ret = statementBlock.execute(environment);
        environment.destroyScope();

        if(ret.getType() == NodeType.CURRENCY && environment.containsCurrency(getReturnType())) {
           ret = new Currency(environment.getExchangeRates().toBaseCurrency(getReturnType(), ((Currency) ret).getValue()),
                   getReturnType(), environment.getExchangeRates());
        } else if(!ret.getType().toString().equals(getReturnType().toUpperCase())) {
            throw new RuntimeEnvironmentException();
        }

        return ret;
    }
}
