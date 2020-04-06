package tkom.lexer;

import tkom.input.Reader;
import tkom.utils.Keywords;
import tkom.utils.Token;
import tkom.utils.TokenType;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class Lexer {

    private Reader reader;
    private StringBuilder stringBuilder;
    private List<String> currencies;
    private Character sign;

    @SuppressWarnings("StatementWithEmptyBody")
    private void skipWhiteSpaces() throws IOException {
        while (Character.isWhitespace(reader.read())) ;
        reader.unRead();
    }

    private boolean isEndOfFile() {
        return sign == Character.UNASSIGNED;
    }

    private void setTokenPosition(Token token) {
        token.setPosition(reader.getCharacterPosition());
        token.setLine(reader.getLineNumber());
    }

    private String readNumber() throws IOException {
        do {
            readToBuilder();
        } while (Character.isDigit(sign) || sign == '.');
        reader.unRead();

        return stringBuilder.toString();
    }

    private String readIdentifier() throws IOException {
        do {
            readToBuilder();
        } while (Character.isLetterOrDigit(sign) || sign == '_');
        reader.unRead();

        return stringBuilder.toString();
    }

    private void readToBuilder() throws IOException {
        stringBuilder.append(sign);
        sign = reader.read();
    }

    private void parseOther(Token token) throws IOException {
        token.setType(
                Keywords.keywords.getOrDefault(sign.toString() + reader.peek(),
                        Keywords.keywords.getOrDefault(sign.toString(), TokenType.INVALID)
                ));
    }

    private void parseNumber(Token token) throws IOException {
        token.setValueAndType(readNumber(), TokenType.NUMBER);
    }

    private void parseKeywordOrIdentifier(Token token) throws IOException {
        String id = readIdentifier();

        if (Keywords.keywords.containsKey(id)) {
            token.setType(Keywords.keywords.get(id));
        } else if (currencies.contains(id)) {
            token.setValueAndType(id, TokenType.CURRENCY);
        } else {
            token.setValueAndType(id, TokenType.IDENTIFIER);
        }
    }

    private void parseToken(Token token) throws IOException {
        if (Character.isLetter(sign) || sign == '_') {
            parseKeywordOrIdentifier(token);
        } else if (Character.isDigit(sign) || sign == '.') {
            parseNumber(token);
        } else {
            parseOther(token);
        }
    }

    private void parseNextToken(Token token) throws IOException {
        sign = reader.read();
        if (isEndOfFile()) {
            token.setType(TokenType.END_OF_FILE);
            reader.close();
        } else {
            parseToken(token);
        }
    }

    private Token prepareNewToken() {
        Token token = new Token();
        stringBuilder = new StringBuilder();
        setTokenPosition(token);
        return token;
    }

    public Lexer(String fileName, List<String> currencies) throws FileNotFoundException {
        this.reader = new Reader(fileName);
        this.currencies = currencies;
    }

    public Token nextToken() throws IOException {
        skipWhiteSpaces();
        Token token = prepareNewToken();
        parseNextToken(token);
        return token;
    }
}
