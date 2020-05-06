package tkom.error;


//TODO usunąć i zastąpić runtimem
public class UndefinedReferenceException extends Exception {
    public UndefinedReferenceException(String identifier) {
        super("Undefined reference to " + identifier);
    }
}
