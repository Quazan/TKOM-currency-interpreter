package tkom.parser;

import tkom.ast.NodeType;
import tkom.ast.Statement;
import tkom.ast.nodes.*;
import tkom.error.InvalidTokenException;
import tkom.error.UnexpectedTokenException;
import tkom.lexer.Lexer;
import tkom.utils.Token;
import tkom.utils.TokenAttributes;
import tkom.utils.TokenType;
import tkom.utils.Type;

import java.io.IOException;

public class Parser {

    private final Lexer lexer;

    private Token advance() throws IOException, InvalidTokenException {
        return lexer.nextToken();
    }

    private Token currentToken() {
        return lexer.getToken();
    }

    public Program parseProgram() throws IOException, InvalidTokenException, UnexpectedTokenException {
        Program program = new Program();

        while (isValueType(advance().getType())) {
            program.addFunction(parseFunction());
        }

        return program;
    }

    private boolean isValueType(TokenType type) {
        return TokenAttributes.valueTypes.contains(type);
    }

    private Token getToken(TokenType expectedTokenType) throws IOException, InvalidTokenException, UnexpectedTokenException {
        Token token = advance();
        if (token.getType() != expectedTokenType) {
            throw new UnexpectedTokenException();
        }

        return token;
    }

    private Function parseFunction() throws UnexpectedTokenException, InvalidTokenException, IOException {
        Function function = new Function();

        function.setName(getToken(TokenType.IDENTIFIER).getValue());
        getToken(TokenType.ROUND_OPEN);

        while (isValueType(advance().getType())) {
            function.addParameter(parseParameter());
        }

        getToken(TokenType.ROUND_CLOSE);
        getToken(TokenType.CURLY_OPEN);

        function.setStatementBlock(parseStatementBlock());

        return function;
    }

    private StatementBlock parseStatementBlock() throws UnexpectedTokenException, InvalidTokenException, IOException {
        StatementBlock block = new StatementBlock();



        while (isValidStatement(advance().getType())) {
            block.addStatement(parseStatement());
        }

        getToken(TokenType.CURLY_CLOSE);

        return block;
    }

    private Statement parseStatement() throws IOException, InvalidTokenException, UnexpectedTokenException {

        switch (currentToken().getType()) {
            case IF: {
                return parseIfStatement();
            }
            case WHILE: {
                return parseWhileStatement();
            }
            case RETURN: {
                return parseReturnStatement();
            }
            case INT:
            case DOUBLE:
            case CURRENCY: {
                return parseInitStatement();
            }

            case IDENTIFIER: {
                if (advance().getType() == TokenType.ASSIGNMENT) {
                    return parseAssignStatement();
                } else if (currentToken().getType() == TokenType.ROUND_OPEN) {
                    return parseFunctionCall();
                }
                break;
            }

            default:
                throw new UnexpectedTokenException();
        }
    }

    private FunctionCall parseFunctionCall() {
    }

    private AssignStatement parseAssignStatement() {
    }

    private InitStatement parseInitStatement() {
    }

    private ReturnStatement parseReturnStatement() {
    }

    private WhileStatement parseWhileStatement() {
    }

    private IfStatement parseIfStatement() throws UnexpectedTokenException, InvalidTokenException, IOException {
        IfStatement statement = new IfStatement();

        getToken(TokenType.ROUND_OPEN);
        statement.setCondition(parseCondition());

        if(getOptionalToken(TokenType.CURLY_OPEN)) {
            statement.setTrueBlock(parseStatementBlock());
        } else {
            statement.setTrueBlock(parseSingleStatement());
        }


    }

    private StatementBlock parseSingleStatement() throws UnexpectedTokenException, InvalidTokenException, IOException {
        StatementBlock block = new StatementBlock();
        block.addStatement(parseStatement());
        return block;
    }

    private boolean getOptionalToken(TokenType type) throws IOException, InvalidTokenException {
        return advance().getType() == type;
    }

    private Condition parseCondition() {
        Condition condition = new Condition();

        return condition;
    }

    private boolean isValidStatement(TokenType type) {
        return TokenAttributes.statementTypes.contains(type);
    }

    private Signature parseParameter() throws UnexpectedTokenException, InvalidTokenException, IOException {
        Signature signature = new Signature();

        signature.setReturnType(Type.valueOf(currentToken().getType().toString()));
        signature.setName(getToken(TokenType.IDENTIFIER).getValue());

        return signature;
    }


    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }
}
