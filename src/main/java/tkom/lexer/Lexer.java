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

    private boolean isWhiteSpace(int c) {
        return c == ' ' || c == '\n';
    }

    private boolean isEOF(Character c) {
        return c == Character.UNASSIGNED;
    }

    public Lexer(String fileName, List<String> currencies) throws FileNotFoundException {
        this.reader = new Reader(fileName);
        this.currencies = currencies;
    }

    public Token nextToken() throws IOException {
        skipWhiteSpaces();

        stringBuilder = new StringBuilder();

        Token token = new Token();
        token.setPosition(reader.getCharacterPosition());
        token.setLine(reader.getLineNumber());

        Character sign = reader.read();

        if (isEOF(sign)) {
            return null;
        }

        if (Character.isLetter(sign)) {
            do {
                stringBuilder.append(sign);
                sign = reader.read();
            } while (Character.isLetterOrDigit(sign));
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

        } else if (Character.isDigit(sign)) {
            do {
                stringBuilder.append(sign);
                sign = reader.read();
            } while (Character.isDigit(sign));
            reader.unRead();

            token.setType(TokenType.NUMBER);
            token.setValue(stringBuilder.toString());
        } else {
            switch (sign) {
                case '=': {
                    if (reader.read() == '=') {
                        token.setType(TokenType.EQUALITY);
                    } else {
                        reader.unRead();
                        token.setType(TokenType.ASSIGNMENT);
                    }
                    break;
                }

                case '<': {
                    if (reader.read() == '=') {
                        token.setType(TokenType.LESS_OR_EQUAL);
                    } else {
                        reader.unRead();
                        token.setType(TokenType.LESS);
                    }
                    break;
                }

                case '>': {
                    if (reader.read() == '=') {
                        token.setType(TokenType.GREATER_OR_EQUAL);
                    } else {
                        reader.unRead();
                        token.setType(TokenType.GREATER);
                    }
                    break;
                }

                case '!': {
                    if (reader.read() == '=') {
                        token.setType(TokenType.INEQUALITY);
                    } else {
                        reader.unRead();
                        token.setType(TokenType.NOT);
                    }
                    break;
                }

                case '&': {
                    if (reader.read() == '&') {
                        token.setType(TokenType.AND);
                    } else {
                        reader.unRead();
                        token.setType(TokenType.INVALID);
                    }
                    break;
                }

                case '|': {
                    if (reader.read() == '|') {
                        token.setType(TokenType.OR);
                    } else {
                        reader.unRead();
                        token.setType(TokenType.INVALID);
                    }
                    break;
                }

                default: {
                    token.setType(Keywords.singleSings.getOrDefault(sign.toString(), TokenType.INVALID));
                }
            }
        }

        return token;
    }

    public Integer getLineNumber() {
        return reader.getLineNumber();
    }

}
