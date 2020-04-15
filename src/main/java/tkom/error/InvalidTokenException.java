package tkom.error;

public class InvalidTokenException extends Exception {
    public InvalidTokenException(int line, int position) {
        super("Invalid token at line: " + line + " position: " + position);
    }
}
