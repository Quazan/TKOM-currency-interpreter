package tkom.ast;

import tkom.error.UndefinedReferenceException;
import tkom.execution.Environment;

public interface Expression extends Node{

    Value evaluate(Environment environment) throws UndefinedReferenceException;
}
