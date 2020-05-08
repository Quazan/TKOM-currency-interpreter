package tkom.ast;

import tkom.error.RuntimeEnvironmentException;
import tkom.execution.Environment;

public interface Expression extends Node {

    Value evaluate(Environment environment) throws RuntimeEnvironmentException;

}
