package tkom.execution;

import lombok.Getter;
import lombok.Setter;
import tkom.ast.Value;
import tkom.error.RuntimeEnvironmentException;
import tkom.error.UndefinedReferenceException;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class Scope {

    private Scope parentScope;
    private Map<String, Value> variables;

    public Scope() {
        this.variables = new HashMap<>();
    }


    public void addVariable(String identifier, Value value) throws RuntimeEnvironmentException {
        if(variables.put(identifier, value) != null) {
            throw new RuntimeEnvironmentException("Variable " + identifier + " is already defined.");
        }
    }

    public Value getVariable(String identifier) {
        return variables.get(identifier);
    }

    public boolean containsVariable(String identifier){
        return variables.containsKey(identifier);
    }

    public void setVariable(String identifier, Value value) throws UndefinedReferenceException {
        if(variables.replace(identifier, value) == null ) {
            throw new UndefinedReferenceException(identifier);
        }
    }
}
