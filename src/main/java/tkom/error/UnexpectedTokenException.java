package tkom.error;

public class UnexpectedTokenException extends Exception {
    public UnexpectedTokenException(int line, int position) {
        super("Invalid token at line: " + line + " position: " + position);
    }
}
