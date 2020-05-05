package tkom.error;


//TODO usunąć i zastąpićruntimem
public class UndefinedReferenceException extends Exception {
    public UndefinedReferenceException(String identifier) {
        super("Undefined reference to " + identifier);
    }
}
