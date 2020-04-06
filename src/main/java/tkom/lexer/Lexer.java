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

    private void skipWhiteSpaces() throws IOException {
        while (Character.isWhitespace(reader.read())) ;
        reader.unRead();
    }

    private boolean isEOF(Character c) {
        return c == Character.UNASSIGNED;
    }

    public Lexer(String fileName, List<String> currencies) throws FileNotFoundException {
        this.reader = new Reader(fileName);
        this.currencies = currencies;
    }

    public Token nextToken() throws IOException {
        Token token = new Token();
        stringBuilder = new StringBuilder();

        skipWhiteSpaces();
        setTokenPosition(token);

        Character sign = reader.read();
        if (isEOF(sign)) {
            token.setType(TokenType.END_OF_FILE);
        }

        parseToken(token, sign);

        return token;
    }

    private void setTokenPosition(Token token) {
        token.setPosition(reader.getCharacterPosition());
        token.setLine(reader.getLineNumber());
    }

    private void parseToken(Token token, Character sign) throws IOException {
        if (Character.isLetter(sign) || sign == '_') {
            parseKeywordOrIdentifier(token, sign);
        } else if (Character.isDigit(sign) || sign == '.') {
            parseNumber(token, sign);
        } else {
            parseOther(token, sign);
        }
    }

    private void parseOther(Token token, Character sign) throws IOException {
        switch (sign) {
            case '=': {
                tryDoubleKeyword(token, '=', TokenType.EQUALITY, TokenType.ASSIGNMENT);
                break;
            }

            case '<': {
                tryDoubleKeyword(token, '=', TokenType.LESS_OR_EQUAL, TokenType.LESS);
                break;
            }

            case '>': {
                tryDoubleKeyword(token, '=', TokenType.GREATER_OR_EQUAL, TokenType.GREATER);
                break;
            }

            case '!': {
                tryDoubleKeyword(token, '=', TokenType.INEQUALITY, TokenType.NOT);
                break;
            }

            case '&': {
                tryDoubleKeyword(token, '&', TokenType.AND, TokenType.INVALID);
                break;
            }

            case '|': {
                tryDoubleKeyword(token, '|', TokenType.OR, TokenType.INVALID);
                break;
            }

            default: {
                token.setType(Keywords.singleSings.getOrDefault(sign.toString(), TokenType.INVALID));
            }
        }
    }

    private void tryDoubleKeyword(Token token, Character c, TokenType ifTrue, TokenType ifFalse) throws IOException {
        if (reader.read() == c) {
            token.setType(ifTrue);
        } else {
            reader.unRead();
            token.setType(ifFalse);
        }
    }

    private void parseNumber(Token token, Character sign) throws IOException {
        do {
            stringBuilder.append(sign);
            sign = reader.read();
        } while (Character.isDigit(sign));
        reader.unRead();

        token.setType(TokenType.NUMBER);
        token.setValue(stringBuilder.toString());
    }

    private void parseKeywordOrIdentifier(Token token, Character sign) throws IOException {
        do {
            stringBuilder.append(sign);
            sign = reader.read();
        } while (Character.isLetterOrDigit(sign) || sign == '_');
        reader.unRead();

        String s = stringBuilder.toString();

        token.setType(Keywords.keywords.getOrDefault(s, TokenType.IDENTIFIER));

        if (Keywords.keywords.containsKey(s)) {
            token.setType(Keywords.keywords.get(s));
        } else if (currencies.contains(s)) {
            token.setType(TokenType.CURRENCY);
            token.setValue(s);
        } else {
            token.setType(TokenType.IDENTIFIER);
            token.setValue(s);
        }
    }

    public Integer getLineNumber() {
        return reader.getLineNumber();
    }

}
