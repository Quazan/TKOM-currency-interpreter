package tkom.error;

public class UndefinedReferenceException extends Exception {
    public UndefinedReferenceException(String identifier) {
        super("Undefined reference to " + identifier);
    }
}
