package tkom.execution;

import lombok.Getter;
import lombok.Setter;
import tkom.ast.Value;
import tkom.ast.nodes.Function;
import tkom.currency.Rates;
import tkom.error.RuntimeEnvironmentException;
import tkom.error.UndefinedReferenceException;

import java.util.*;

@Getter
@Setter
public class Environment {

    private Map<String, Function> functions;
    private Deque<Scope> scopes;
    private Rates exchangeRates;

    public Environment(List<Function> functionList, Rates exchangeRates) throws UndefinedReferenceException {
        this.functions = new HashMap<>();
        this.scopes = new ArrayDeque<>();
        this.exchangeRates = exchangeRates;

        for(Function function : functionList) {
            if(functions.put(function.getIdentifier(), function) != null) {
                throw new UndefinedReferenceException(function.getIdentifier());
            }
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

    public void addVariable(String identifier, Value value) throws RuntimeEnvironmentException {
        Scope currentScope = scopes.getFirst();

        currentScope.addVariable(identifier, value);
    }

    public Value getVariable(String identifier) throws UndefinedReferenceException {
        Scope scope = scopes.getFirst();

        if(scope.containsVariable(identifier)) {
            return scope.getVariable(identifier);
        }

        while ((scope = scope.getParentScope()) != null) {
            //scope = scope.getParentScope();
            if(scope.containsVariable(identifier)) {
                return scope.getVariable(identifier);
            }
        }

        throw new UndefinedReferenceException(identifier);
    }

    public void setVariable(String identifier, Value evaluate) throws UndefinedReferenceException {
        Scope scope = scopes.getFirst();

        if(scope.containsVariable(identifier)) {
            scope.setVariable(identifier, evaluate);
            return;
        }

        while (scope.getParentScope() != null) {
            scope = scope.getParentScope();
            if(scope.containsVariable(identifier)) {
                scope.setVariable(identifier, evaluate);
                return;
            }
        }

        throw new UndefinedReferenceException(identifier);
    }

    public Function getFunction(String identifier) {
        return functions.get(identifier);
    }

    public boolean containsCurrency(String currency) {
        return exchangeRates.contains(currency);
    }
}
