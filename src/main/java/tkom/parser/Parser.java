package tkom.parser;

import tkom.ast.Node;
import tkom.ast.Statement;
import tkom.ast.nodes.*;
import tkom.ast.nodes.Double;
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
        function.setReturnType(Type.valueOf(currentToken().getType().toString()));
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
                String identifier = currentToken().getValue();
                if (advance().getType() == TokenType.ASSIGNMENT) {
                    return parseAssignStatement(identifier);
                } else if (currentToken().getType() == TokenType.ROUND_OPEN) {
                    return parseFunctionCall(identifier);
                }
                break;
            }
        }

        throw new UnexpectedTokenException();
    }

    private FunctionCall parseFunctionCall(String identifier) throws UnexpectedTokenException, InvalidTokenException, IOException {
        FunctionCall functionCall = new FunctionCall();

        functionCall.setName(identifier);

        while (!getOptionalToken(TokenType.ROUND_CLOSE)) {
            functionCall.addArgument(parseExpression());
        }

        getToken(TokenType.SEMICOLON);

        return functionCall;
    }

    private AssignStatement parseAssignStatement(String identifier) throws UnexpectedTokenException, InvalidTokenException, IOException {
        AssignStatement statement = new AssignStatement();

        statement.setIdentifier(identifier);
        statement.setAssignable(parseExpression());
        getToken(TokenType.SEMICOLON);

        return statement;
    }

    private InitStatement parseInitStatement() throws UnexpectedTokenException, InvalidTokenException, IOException {
        InitStatement statement = new InitStatement();

        statement.setReturnType(Type.valueOf(currentToken().getType().toString()));
        statement.setName(getToken(TokenType.IDENTIFIER).getValue());

        getToken(TokenType.ASSIGNMENT);

        statement.setAssignable(parseExpression());

        return statement;
    }

    private ReturnStatement parseReturnStatement() throws UnexpectedTokenException, InvalidTokenException, IOException {
        ReturnStatement statement = new ReturnStatement();

        statement.setExpression(parseExpression());
        getToken(TokenType.SEMICOLON);

        return statement;
    }

    //TODO
    private Expression parseExpression() throws IOException, InvalidTokenException, UnexpectedTokenException {
        Expression expression = new Expression();

        expression.addOperand(parseMultiplicativeExpression());

        while (isValidAdditiveOperator(advance().getType())){
            expression.addOperation(currentToken().getType());
            expression.addOperand(parseMultiplicativeExpression());
        }

        return expression;
    }

    private boolean isValidAdditiveOperator(TokenType type) {
        return TokenAttributes.additiveOperators.contains(type);
    }

    private boolean isValidMultiplicativeOperator(TokenType type) {
        return TokenAttributes.multiplicativeOperators.contains(type);
    }

    private Node parseMultiplicativeExpression() throws IOException, InvalidTokenException, UnexpectedTokenException {
        Expression expression = new Expression();

        expression.addOperand(parsePrimaryExpression());

        while (isValidMultiplicativeOperator(advance().getType())){
            expression.addOperation(currentToken().getType());
            expression.addOperand(parsePrimaryExpression());
        }

        return expression;
    }

    private Node parsePrimaryExpression() throws IOException, InvalidTokenException, UnexpectedTokenException {

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

            //TODO tutaj może się wyjebać
            case IDENTIFIER: {
                String identifier = currentToken().getValue();
                if(advance().getType() == TokenType.CURLY_OPEN) {
                    return parseFunctionCall(identifier);
                } else {
                    return new Variable(identifier);
                }
            }
        }

        throw new UnexpectedTokenException();
    }

    private Node parseLiteral() throws IOException, InvalidTokenException, UnexpectedTokenException {
        Node literal;
        int sign = 1;

        if(currentToken().getType() == TokenType.MINUS) {
            sign = -1;
            getToken(TokenType.NUMBER);
        }

        if(currentToken().getValue().contains(".")) {
            literal = new Double();
            ((Double) literal).setValue(currentToken().getNumericValue().doubleValue() * sign);
        }  else {
            literal = new Int();
            ((Int) literal).setValue(currentToken().getNumericValue().intValue() * sign);
        }

        return literal;
    }

    private WhileStatement parseWhileStatement() throws UnexpectedTokenException, InvalidTokenException, IOException {
        WhileStatement statement = new WhileStatement();

        getToken(TokenType.ROUND_OPEN);
        statement.setCondition(parseCondition());
        getToken(TokenType.ROUND_CLOSE);

        if (getOptionalToken(TokenType.CURLY_OPEN)) {
            statement.setWhileBlock(parseStatementBlock());
        } else {
            statement.setWhileBlock(parseSingleStatement());
        }

        return statement;
    }

    private IfStatement parseIfStatement() throws UnexpectedTokenException, InvalidTokenException, IOException {
        IfStatement statement = new IfStatement();

        getToken(TokenType.ROUND_OPEN);
        statement.setCondition(parseCondition());

        if (getOptionalToken(TokenType.CURLY_OPEN)) {
            statement.setTrueBlock(parseStatementBlock());
        } else {
            statement.setTrueBlock(parseSingleStatement());
        }

        if (getOptionalToken(TokenType.ELSE)) {
            if (getOptionalToken(TokenType.CURLY_OPEN)) {
                statement.setFalseBlock(parseStatementBlock());
            } else {
                statement.setFalseBlock(parseSingleStatement());
            }
        }

        return statement;
    }

    private StatementBlock parseSingleStatement() throws UnexpectedTokenException, InvalidTokenException, IOException {
        StatementBlock block = new StatementBlock();
        block.addStatement(parseStatement());
        return block;
    }

    private boolean getOptionalToken(TokenType type) throws IOException, InvalidTokenException {
        return advance().getType() == type;
    }

    //TODO może wyjebywać przez tego getOptional
    private Condition parseCondition() throws UnexpectedTokenException, InvalidTokenException, IOException {
        Condition condition = new Condition();

        condition.addOperand(parseAndCondition());

        while (getOptionalToken(TokenType.OR)) {
            condition.setOperator(TokenType.OR);
            condition.addOperand(parseAndCondition());
        }

        return condition;
    }

    private Node parseAndCondition() throws IOException, InvalidTokenException, UnexpectedTokenException {
        Condition condition = new Condition();

        condition.addOperand(parseEqualityCondition());

        while (getOptionalToken(TokenType.AND)) {
            condition.setOperator(TokenType.AND);
            condition.addOperand(parseEqualityCondition());
        }

        return condition;
    }

    private Node parseEqualityCondition() throws IOException, InvalidTokenException, UnexpectedTokenException {
        Condition condition = new Condition();

        condition.addOperand(parseRelationalCondition());

        while (isValidEqualityOperator(advance().getType())) {
            condition.setOperator(currentToken().getType());
            condition.addOperand(parseRelationalCondition());
        }

        return condition;
    }

    private Node parseRelationalCondition() throws IOException, InvalidTokenException, UnexpectedTokenException {
        Condition condition = new Condition();

        condition.addOperand(parsePrimaryCondition());

        while (isValidRelationalOperator(advance().getType())) {
            condition.setOperator(currentToken().getType());
            condition.addOperand(parsePrimaryCondition());
        }

        return condition;
    }

    private Node parsePrimaryCondition() throws IOException, InvalidTokenException, UnexpectedTokenException {
        Condition condition;
        boolean negation = false;

        if(getOptionalToken(TokenType.NOT)) {
            negation = true;
        }

        if(advance().getType() == TokenType.ROUND_OPEN) {
            condition = parseCondition();
            condition.setNegated(negation);
            getToken(TokenType.ROUND_CLOSE);
        } else {
            condition = new Condition();
            condition.setNegated(negation);
            condition.addOperand(parseExpression());
        }
        return condition;
    }

    private boolean isValidRelationalOperator(TokenType type) {
        return TokenAttributes.relationOperators.contains(type);
    }

    private boolean isValidEqualityOperator(TokenType type) {
        return TokenAttributes.equalityOperators.contains(type);
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
