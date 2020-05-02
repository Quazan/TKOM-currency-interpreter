package tkom.ast;

import tkom.error.RuntimeEnvironmentException;
import tkom.error.UndefinedReferenceException;
import tkom.execution.Environment;

public interface Statement extends Node{
    Value execute(Environment environment) throws UndefinedReferenceException, RuntimeEnvironmentException;
}
