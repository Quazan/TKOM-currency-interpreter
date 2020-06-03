package tkom.ast.nodes;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tkom.ast.Value;
import tkom.ast.Expression;
import tkom.ast.Node;
import tkom.error.RuntimeEnvironmentException;
import tkom.execution.Environment;
import tkom.utils.ExecuteStatus;
import tkom.utils.NodeType;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class Function extends Signature implements Node {

    private List<Signature> parameters;

    private StatementBlock statementBlock;

    private void checkNumberOfParameters(List<Expression> arguments) throws RuntimeEnvironmentException {
        if (arguments.size() != parameters.size()) {
            throw new RuntimeEnvironmentException("Invalid arguments");
        }
    }

    private List<Value> prepareArguments(Environment environment, List<Expression> arguments) throws RuntimeEnvironmentException {
        List<Value> argumentsValue = new ArrayList<>();
        for (Expression arg : arguments) {
            argumentsValue.add(arg.evaluate(environment));
        }

        return argumentsValue;
    }

    private void checkArgumentType(Environment environment, Signature parameter, Value argument) throws RuntimeEnvironmentException {
        if (!((parameter.isReturnType(argument.getType())) ||
                (environment.containsCurrency(parameter.getReturnType()) && Value.isCurrency(argument)))) {
            throw new RuntimeEnvironmentException("Unexpected argument type. Expected: "
                    + parameter.getReturnType().toUpperCase() + " actual: " + argument.getType());
        }
    }

    private Value validateArgument(Environment environment, Signature parameter, Value argument) throws RuntimeEnvironmentException {
        if (parameter.isReturnType(NodeType.DOUBLE) && Value.isInt(argument)) {
            return new DoubleNode(((IntNode) argument).getValue());
        }

        checkArgumentType(environment, parameter, argument);

        return argument;
    }

    private void addVariablesToEnvironment(Environment environment, List<Value> argumentsValue) throws RuntimeEnvironmentException {
        for (int i = 0; i < parameters.size(); i++) {
            Signature parameter = parameters.get(i);
            Value argument = validateArgument(environment, parameter, argumentsValue.get(i));

            environment.addVariable(parameter.getIdentifier(), argument);
        }
    }

    private void prepareEnvironment(Environment environment, List<Expression> arguments) throws RuntimeEnvironmentException {
        checkNumberOfParameters(arguments);

        List<Value> argumentsValue = prepareArguments(environment, arguments);

        environment.createNewScope();

        addVariablesToEnvironment(environment, argumentsValue);
    }

    private void checkIfWasReturned(ExecuteOut ret) throws RuntimeEnvironmentException {
        if (!ret.isReturnCall()) {
            throw new RuntimeEnvironmentException("Function without return statement");
        }
    }

    private Value executeFunction(Environment environment, List<Expression> arguments) throws RuntimeEnvironmentException {
        prepareEnvironment(environment, arguments);

        ExecuteOut ret = statementBlock.execute(environment);
        environment.destroyScope();
        checkIfWasReturned(ret);

        return ret.getReturnedValue();
    }

    private Value castReturnedType(Environment environment, Value ret) throws RuntimeEnvironmentException {
        if (Value.isCurrency(ret) && environment.containsCurrency(getReturnType())) {
            return new Currency(Value.getCurrencyValue(ret),
                    getReturnType(), environment.getExchangeRates());
        } else if (Value.isInt(ret) && isReturnType(NodeType.DOUBLE)) {
            return new DoubleNode(((IntNode) ret).getValue());
        } else if (isReturnType(ret.getType())) {
            return ret;
        }

        throw new RuntimeEnvironmentException("Unexpected return type. Expected: " + getReturnType().toUpperCase()
                + " actual: " + ret.getType());
    }

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

    public Value execute(Environment environment, List<Expression> arguments) throws RuntimeEnvironmentException {

        Value ret = executeFunction(environment, arguments);

        return castReturnedType(environment, ret);
    }
}
