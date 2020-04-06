package tkom.lexer;

import lombok.ToString;
import tkom.input.Reader;
import tkom.utils.Token;
import tkom.utils.TokenType;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

public class Lexer {

    private Reader reader;
    private StringBuilder stringBuilder;

    private void skipWhiteSpaces() throws IOException {
        while(Character.isWhitespace(reader.read()));

        reader.unRead();
    }

    private boolean isWhiteSpace(int c) {
        return c == ' ' || c == '\n';
    }

    private boolean isEOF(Character c) {
        return c == Character.UNASSIGNED;
    }

    public Lexer(String fileName) throws FileNotFoundException {
        this.reader = new Reader(fileName);
    }

    public Token nextToken() throws IOException {
        skipWhiteSpaces();

        Token token = new Token();
        token.setPosition(reader.getCharacterPosition());
        token.setLine(reader.getLineNumber());
        stringBuilder = new StringBuilder();

        Character c;

        while (!isEOF(c=reader.read())) {

            if(Character.isWhitespace(c)) {
                break;
            }

            stringBuilder.append(c);
        }

        token.setType(TokenType.Test);
        token.setValue(stringBuilder.toString());

        if(stringBuilder.length() == 0) {
            return null;
        }

        return token;
    }

    public Integer getLineNumber() {
        return reader.getLineNumber();
    }

}
