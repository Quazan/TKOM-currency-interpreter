package tkom.lexer;

import tkom.error.InvalidTokenException;
import tkom.input.Reader;
import tkom.utils.Keywords;
import tkom.utils.Token;
import tkom.utils.TokenType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Lexer {

    private final Reader reader;
    private StringBuilder stringBuilder;
    private final List<String> currencies;
    private Character sign;
    private Token token;
    private final int TOKEN_MAX_SIZE = 100;

    private void skipWhiteSpaces() throws IOException {
        while (Character.isWhitespace(reader.read())) ;
        reader.unRead();
    }

    private boolean isEndOfFile() {
        return sign == Character.UNASSIGNED;
    }

    private void validateNumber() throws InvalidTokenException {
        int dotCounter = 0;
        int beforeDot = 0;
        int afterDot = 0;

        for (int i = 0; i < stringBuilder.length(); i++) {
            if (dotCounter == 0) {
                beforeDot++;
            } else {
                afterDot++;
            }
            if (stringBuilder.charAt(i) == '.') {
                dotCounter++;
            }
            if (dotCounter > 1 || beforeDot > 32 || afterDot > 32) {
                throw new InvalidTokenException(token.getLine(), token.getPosition());
            }
        }
    }

    private String readNumber() throws IOException, InvalidTokenException {
        do {
            readToBuilder();
        } while (Character.isDigit(sign) || sign == '.');
        reader.unRead();

        validateNumber();
        return stringBuilder.toString();
    }

    private String readString() throws IOException, InvalidTokenException {
        do {
            readToBuilder();
        } while ((Character.isLetterOrDigit(sign) || sign == '_') && sign != '"');
        stringBuilder.append(sign);

        if (stringBuilder.charAt(stringBuilder.length() - 1) != '"') {
            throw new InvalidTokenException(token.getLine(), token.getPosition());
        }

        return stringBuilder.toString();
    }

    private String readIdentifier() throws IOException, InvalidTokenException {
        do {
            readToBuilder();
        } while (Character.isLetterOrDigit(sign) || sign == '_');
        reader.unRead();
        return stringBuilder.toString();
    }

    private void readToBuilder() throws IOException, InvalidTokenException {
        stringBuilder.append(sign);
        if (stringBuilder.length() == TOKEN_MAX_SIZE) {
            throw new InvalidTokenException(token.getLine(), token.getPosition());
        }
        sign = reader.read();
    }

    private void parseOther() throws IOException, InvalidTokenException {
        if (Keywords.doubleSigns.containsKey(sign.toString() + reader.peek())) {
            token.setType(Keywords.doubleSigns.get(sign.toString() + reader.read()));
        } else if (Keywords.singleSigns.containsKey(sign.toString())) {
            token.setType(Keywords.singleSigns.get(sign.toString()));
        } else {
            throw new InvalidTokenException(token.getLine(), token.getPosition());
        }
    }

    private void parseNumber() throws IOException, InvalidTokenException {
        token.setTypeAndValue(TokenType.NUMBER, readNumber());
    }

    private void parseKeywordOrIdentifier() throws IOException, InvalidTokenException {
        String id = readIdentifier();

        if (Keywords.keywords.containsKey(id)) {
            token.setTypeAndValue(Keywords.keywords.get(id), id);
        } else if (currencies.contains(id)) {
            token.setTypeAndValue(TokenType.CURRENCY, id);
        } else {
            token.setTypeAndValue(TokenType.IDENTIFIER, id);
        }
    }

    private void parseToken() throws IOException, InvalidTokenException {
        if (sign == '"') {
            parseString();
        } else if (Character.isLetter(sign) || sign == '_') {
            parseKeywordOrIdentifier();
        } else if (Character.isDigit(sign)) {
            parseNumber();
        } else {
            parseOther();
        }
    }

    private void parseString() throws IOException, InvalidTokenException {
        token.setTypeAndValue(TokenType.STRING, readString());
    }

    private void setNewToken() {
        stringBuilder = new StringBuilder();
        token = new Token(reader.getLineNumber(), reader.getCharacterPosition());
    }

    public Lexer(java.io.Reader abstractReader, List<String> currencies) {
        this.reader = new Reader(abstractReader);
        this.currencies = Objects.requireNonNullElseGet(currencies, ArrayList::new);
    }

    public Token nextToken() throws IOException, InvalidTokenException {
        skipWhiteSpaces();

        sign = reader.read();
        setNewToken();

        if (isEndOfFile()) {
            token.setType(TokenType.END_OF_FILE);
        } else {
            parseToken();
        }

        return token;
    }

    public Token getToken() {
        return token;
    }
}
