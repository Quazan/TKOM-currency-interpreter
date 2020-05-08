package tkom.ast;

import tkom.ast.nodes.ExecuteOut;
import tkom.error.RuntimeEnvironmentException;
import tkom.execution.Environment;

public interface Statement extends Node {
    ExecuteOut execute(Environment environment) throws RuntimeEnvironmentException;
}
