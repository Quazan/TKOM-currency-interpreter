package tkom.execution;

import lombok.Getter;
import tkom.ast.Value;
import tkom.ast.nodes.Function;
import tkom.currency.Rates;
import tkom.error.RuntimeEnvironmentException;

import java.util.*;


public class Environment {

    private final Map<String, Function> functions;
    private final Deque<Scope> scopes;

    @Getter
    private final Rates exchangeRates;

    public Environment(List<Function> functionList, Rates exchangeRates) throws RuntimeEnvironmentException {
        this.functions = new HashMap<>();
        this.scopes = new ArrayDeque<>();
        this.exchangeRates = exchangeRates;

        for (Function function : functionList) {
            if (functions.put(function.getIdentifier(), function) != null) {
                throw new RuntimeEnvironmentException("Multiple functions with the same name: "
                        + function.getIdentifier());
            }
        }
    }

    public void createNewLocalScope() {
        Scope localScope = new Scope();

        if (!scopes.isEmpty()) {
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

    public void addFunction(Function function) throws RuntimeEnvironmentException {
        if (functions.put(function.getIdentifier(), function) != null) {
            throw new RuntimeEnvironmentException("Multiple functions with the same name: "
                    + function.getIdentifier());
        }
    }

    public Value getVariable(String identifier) throws RuntimeEnvironmentException {
        Scope scope = scopes.getFirst();

        if (scope.containsVariable(identifier)) {
            return scope.getVariable(identifier);
        }

        while ((scope = scope.getParentScope()) != null) {
            //scope = scope.getParentScope();
            if (scope.containsVariable(identifier)) {
                return scope.getVariable(identifier);
            }
        }

        throw new RuntimeEnvironmentException("Undefined Reference to:" + identifier);
    }

    public void setVariable(String identifier, Value evaluate) throws RuntimeEnvironmentException {
        Scope scope = scopes.getFirst();

        if (scope.containsVariable(identifier)) {
            scope.setVariable(identifier, evaluate);
            return;
        }

        while (scope.getParentScope() != null) {
            scope = scope.getParentScope();
            if (scope.containsVariable(identifier)) {
                scope.setVariable(identifier, evaluate);
                return;
            }
        }

        throw new RuntimeEnvironmentException("Undefined Reference to:" + identifier);
    }

    public Function getFunction(String identifier) {
        return functions.get(identifier);
    }

    public boolean containsCurrency(String currency) {
        return exchangeRates.contains(currency);
    }
}
