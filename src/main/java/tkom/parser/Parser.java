package tkom.parser;

import tkom.ast.Node;
import tkom.ast.Statement;
import tkom.ast.nodes.DoubleNode;
import tkom.ast.nodes.*;
import tkom.error.InvalidTokenException;
import tkom.error.UnexpectedTokenException;
import tkom.lexer.Lexer;
import tkom.utils.Token;
import tkom.utils.TokenAttributes;
import tkom.utils.TokenType;

import java.io.IOException;
import java.util.List;

public class Parser {

    private final Lexer lexer;
    private boolean buffer = false;

    private Token currentToken() {
        return lexer.getToken();
    }

    private Token advance() throws IOException, InvalidTokenException {
        if (buffer) {
            buffer = false;
            return lexer.getToken();
        }
        return lexer.nextToken();
    }

    Token getToken(TokenType expectedTokenType) throws IOException, InvalidTokenException, UnexpectedTokenException {
        if (advance().getType() != expectedTokenType) {
            throw new UnexpectedTokenException(currentToken().getLine(),
                    currentToken().getPosition(),
                    expectedTokenType,
                    currentToken().getType());
        }

        return currentToken();
    }

    boolean getOptionalToken(TokenType type) throws IOException, InvalidTokenException {
        if (buffer) {
            if (currentToken().getType() == type) {
                buffer = false;
                return true;
            } else {
                return false;
            }
        } else {
            if (advance().getType() == type) {
                return true;
            } else {
                buffer = true;
                return false;
            }
        }
    }

    boolean getOptionalTokenTypes(List<TokenType> type) throws IOException, InvalidTokenException {
        if (buffer) {
            if (type.contains(currentToken().getType())) {
                buffer = false;
                return true;
            } else {
                return false;
            }
        } else {
            if (type.contains(advance().getType())) {
                return true;
            } else {
                buffer = true;
                return false;
            }
        }
    }

    Function parseFunction() throws UnexpectedTokenException, InvalidTokenException, IOException {
        Function function = new Function();
        function.setReturnType(currentToken().getValue());
        function.setIdentifier(getToken(TokenType.IDENTIFIER).getValue());
        getToken(TokenType.ROUND_OPEN);

        while (getOptionalTokenTypes(TokenAttributes.valueTypes)) {
            function.addParameter(parseParameter());
            getOptionalToken(TokenType.COMMA);
        }

        getToken(TokenType.ROUND_CLOSE);
        getToken(TokenType.CURLY_OPEN);

        function.setStatementBlock(parseStatementBlock());

        return function;
    }

    Signature parseParameter() throws UnexpectedTokenException, InvalidTokenException, IOException {
        Signature signature = new Signature();

        signature.setReturnType(currentToken().getValue());
        signature.setIdentifier(getToken(TokenType.IDENTIFIER).getValue());

        return signature;
    }

    StatementBlock parseStatementBlock() throws UnexpectedTokenException, InvalidTokenException, IOException {
        StatementBlock block = new StatementBlock();

        while (getOptionalTokenTypes(TokenAttributes.statementTypes)) {
            block.addStatement(parseStatement());
        }

        getToken(TokenType.CURLY_CLOSE);

        return block;
    }

    StatementBlock parseSingleStatement() throws UnexpectedTokenException, InvalidTokenException, IOException {
        StatementBlock block = new StatementBlock();
        block.addStatement(parseStatement());
        return block;
    }

    Statement parseStatement() throws IOException, InvalidTokenException, UnexpectedTokenException {

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
                String identifier = currentToken().getValue();
                if (getOptionalToken(TokenType.ASSIGNMENT)) {
                    return parseAssignStatement(identifier);
                } else if (getOptionalToken(TokenType.ROUND_OPEN)) {
                    FunctionCall call = parseFunctionCall(identifier);
                    getToken(TokenType.SEMICOLON);
                    return call;
                }
                break;
            }
        }

        throw new UnexpectedTokenException(currentToken().getLine(),
                currentToken().getPosition(),
                TokenAttributes.statementTypes,
                currentToken().getType());
    }

    FunctionCall parseFunctionCall(String identifier) throws UnexpectedTokenException, InvalidTokenException, IOException {
        FunctionCall functionCall = new FunctionCall();

        functionCall.setIdentifier(identifier);

        if (getOptionalToken(TokenType.STRING)) {
            Expression expression = new Expression();
            expression.addOperand(new StringNode(currentToken().getValue()));
            functionCall.addArgument(expression);
            getToken(TokenType.ROUND_CLOSE);
            return functionCall;
        }

        while (!getOptionalToken(TokenType.ROUND_CLOSE)) {
            functionCall.addArgument(parseExpression());
            getOptionalToken(TokenType.COMMA);
        }

        return functionCall;
    }

    AssignStatement parseAssignStatement(String identifier) throws UnexpectedTokenException, InvalidTokenException, IOException {
        AssignStatement statement = new AssignStatement();

        statement.setIdentifier(identifier);
        statement.setAssignable(parseExpression());
        getToken(TokenType.SEMICOLON);

        return statement;
    }

    InitStatement parseInitStatement() throws UnexpectedTokenException, InvalidTokenException, IOException {
        InitStatement statement = new InitStatement();

        statement.setReturnType(currentToken().getValue());
        statement.setIdentifier(getToken(TokenType.IDENTIFIER).getValue());

        if (getOptionalToken(TokenType.ASSIGNMENT)) {
            statement.setAssignable(parseExpression());
        }

        getToken(TokenType.SEMICOLON);

        return statement;
    }

    ReturnStatement parseReturnStatement() throws UnexpectedTokenException, InvalidTokenException, IOException {
        ReturnStatement statement = new ReturnStatement();

        statement.setExpression(parseExpression());
        getToken(TokenType.SEMICOLON);

        return statement;
    }

    IfStatement parseIfStatement() throws UnexpectedTokenException, InvalidTokenException, IOException {
        IfStatement statement = new IfStatement();

        getToken(TokenType.ROUND_OPEN);
        statement.setCondition(parseCondition());
        getToken(TokenType.ROUND_CLOSE);

        if (getOptionalToken(TokenType.CURLY_OPEN)) {
            statement.setTrueBlock(parseStatementBlock());
        } else {
            getOptionalTokenTypes(TokenAttributes.statementTypes);
            statement.setTrueBlock(parseSingleStatement());
        }

        if (getOptionalToken(TokenType.ELSE)) {
            if (getOptionalToken(TokenType.CURLY_OPEN)) {
                statement.setFalseBlock(parseStatementBlock());
            } else {
                getOptionalTokenTypes(TokenAttributes.statementTypes);
                statement.setFalseBlock(parseSingleStatement());
            }
        }

        return statement;
    }

    WhileStatement parseWhileStatement() throws UnexpectedTokenException, InvalidTokenException, IOException {
        WhileStatement statement = new WhileStatement();

        getToken(TokenType.ROUND_OPEN);
        statement.setCondition(parseCondition());
        getToken(TokenType.ROUND_CLOSE);

        if (getOptionalToken(TokenType.CURLY_OPEN)) {
            statement.setWhileBlock(parseStatementBlock());
        } else {
            getOptionalTokenTypes(TokenAttributes.statementTypes);
            statement.setWhileBlock(parseSingleStatement());
        }

        return statement;
    }

    Condition parseCondition() throws UnexpectedTokenException, InvalidTokenException, IOException {
        Condition condition = new Condition();

        condition.addOperand(parseAndCondition());

        while (getOptionalToken(TokenType.OR)) {
            condition.setOperator(TokenType.OR);
            condition.addOperand(parseAndCondition());
        }

        return condition;
    }

    Condition parseAndCondition() throws IOException, InvalidTokenException, UnexpectedTokenException {
        Condition condition = new Condition();

        condition.addOperand(parseEqualityCondition());

        while (getOptionalToken(TokenType.AND)) {
            condition.setOperator(TokenType.AND);
            condition.addOperand(parseEqualityCondition());
        }

        return condition;
    }

    Condition parseEqualityCondition() throws IOException, InvalidTokenException, UnexpectedTokenException {
        Condition condition = new Condition();

        condition.addOperand(parseRelationalCondition());

        if (getOptionalTokenTypes(TokenAttributes.equalityOperators)) {
            condition.setOperator(currentToken().getType());
            condition.addOperand(parseRelationalCondition());
        }

        return condition;
    }

    Condition parseRelationalCondition() throws IOException, InvalidTokenException, UnexpectedTokenException {
        Condition condition = new Condition();

        condition.addOperand(parsePrimaryCondition());

        while (getOptionalTokenTypes(TokenAttributes.relationOperators)) {
            condition.setOperator(currentToken().getType());
            condition.addOperand(parsePrimaryCondition());
        }

        return condition;
    }

    Condition parsePrimaryCondition() throws IOException, InvalidTokenException, UnexpectedTokenException {
        Condition condition;
        boolean negation = false;

        if (getOptionalToken(TokenType.NOT)) {
            negation = true;
        }

        if (getOptionalToken(TokenType.ROUND_OPEN)) {
            condition = parseCondition();
            getToken(TokenType.ROUND_CLOSE);
        } else {
            condition = new Condition();
            condition.addOperand(parseExpression());
        }

        condition.setNegated(negation);
        return condition;
    }

    Expression parseExpression() throws IOException, InvalidTokenException, UnexpectedTokenException {
        Expression expression = new Expression();

        expression.addOperand(parseMultiplicativeExpression());

        while (getOptionalTokenTypes(TokenAttributes.additiveOperators)) {
            expression.addOperation(currentToken().getType());
            expression.addOperand(parseMultiplicativeExpression());
        }

        return expression;
    }

    Expression parseMultiplicativeExpression() throws IOException, InvalidTokenException, UnexpectedTokenException {
        Expression expression = new Expression();

        expression.addOperand(parsePrimaryExpression());

        while (getOptionalTokenTypes(TokenAttributes.multiplicativeOperators)) {
            expression.addOperation(currentToken().getType());
            expression.addOperand(parsePrimaryExpression());
        }

        return expression;
    }

    Node parsePrimaryExpression() throws IOException, InvalidTokenException, UnexpectedTokenException {

        switch (advance().getType()) {
            case MINUS:
            case NUMBER: {
                return parseLiteral();
            }
            case ROUND_OPEN: {
                Expression expression = parseExpression();
                getToken(TokenType.ROUND_CLOSE);
                return expression;
            }

            case IDENTIFIER: {
                java.lang.String identifier = currentToken().getValue();
                if (getOptionalToken(TokenType.ROUND_OPEN)) {
                    return parseFunctionCall(identifier);
                } else {
                    return new Variable(identifier);
                }
            }
        }

        throw new UnexpectedTokenException(currentToken().getLine(),
                currentToken().getPosition(),
                TokenAttributes.primaryExpressionTypes,
                currentToken().getType());
    }

    Node parseLiteral() throws IOException, InvalidTokenException, UnexpectedTokenException {
        Node literal;
        int sign = 1;

        if (currentToken().getType() == TokenType.MINUS) {
            sign = -1;
            getToken(TokenType.NUMBER);
        }

        if (currentToken().getValue().contains(".")) {
            literal = new DoubleNode();
            ((DoubleNode) literal).setValue(currentToken().getNumericValue().doubleValue() * sign);
        } else {
            literal = new IntNode();
            ((IntNode) literal).setValue(currentToken().getNumericValue().intValue() * sign);
        }

        return literal;
    }

    public Program parseProgram() throws IOException, InvalidTokenException, UnexpectedTokenException {
        Program program = new Program();

        while (getOptionalTokenTypes(TokenAttributes.valueTypes)) {
            program.addFunction(parseFunction());
        }

        return program;
    }

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }
}
