package tkom.execution;

import lombok.Getter;
import lombok.Setter;
import tkom.ast.Value;
import tkom.ast.nodes.Function;
import tkom.error.UndefinedReferenceException;

import java.util.*;

@Getter
@Setter
public class Environment {

    private Map<String, Function> functions;
    private Deque<Scope> scopes;

    public Environment(List<Function> functionList) {
        this.functions = new HashMap<>();
        this.scopes = new ArrayDeque<>();

        for(Function function : functionList) {
            functions.put(function.getIdentifier(), function);
        }
    }

    public void createNewLocalScope(){
        Scope localScope = new Scope();

        if(!scopes.isEmpty()) {
            localScope.setParentScope(scopes.getFirst());
        }

        scopes.addFirst(localScope);
    }

    public void createNewScope() {
        Scope scope = new Scope();

        scopes.addFirst(scope);
    }

    public void destroyScope() {
        scopes.removeFirst();
    }

    public void addVariable(String identifier, Value value) {
        Scope currentScope = scopes.getFirst();

        currentScope.addVariable(identifier, value);
    }

    public Value getVariable(String identifier) throws UndefinedReferenceException {
        Scope scope = scopes.getFirst();

        if(scope.containsVariable(identifier)) {
            return scope.getVariable(identifier);
        }

        while (scope.getParentScope() != null) {
            if(scope.containsVariable(identifier)) {
                return scope.getVariable(identifier);
            }
            scope = scope.getParentScope();
        }

        throw new UndefinedReferenceException();
    }

    public void setVariable(String identifier, Value evaluate) throws UndefinedReferenceException {
        Scope scope = scopes.getFirst();

        scope.setVariable(identifier, evaluate);
    }

    public Function getFunction(String identifier) {
        return functions.get(identifier);
    }
}
