package tkom.parser;

import tkom.ast.Expression;
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

    Token advance() throws IOException, InvalidTokenException {
        if (buffer) {
            buffer = false;
            return lexer.getToken();
        }
        return lexer.nextToken();
    }

    Token getNextToken(TokenType expectedTokenType) throws IOException, InvalidTokenException, UnexpectedTokenException {
        if (advance().getType() != expectedTokenType) {
            throw new UnexpectedTokenException(
                    currentToken().getLine(),
                    currentToken().getPosition(),
                    expectedTokenType,
                    currentToken().getType()
            );
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

    boolean getOptionalToken(List<TokenType> type) throws IOException, InvalidTokenException {
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
        Function function = new Function(
                currentToken().getValue(),
                getNextToken(TokenType.IDENTIFIER).getValue()
        );

        getNextToken(TokenType.ROUND_OPEN);

        while (getOptionalToken(TokenAttributes.valueTypes)) {
            function.addParameter(parseParameter());
            getOptionalToken(TokenType.COMMA);
        }

        getNextToken(TokenType.ROUND_CLOSE);
        getNextToken(TokenType.CURLY_OPEN);

        function.setStatementBlock(parseStatementBlock());

        return function;
    }

    Signature parseParameter() throws UnexpectedTokenException, InvalidTokenException, IOException {
        return new Signature(
                currentToken().getValue(),
                getNextToken(TokenType.IDENTIFIER).getValue()
        );
    }

    StatementBlock parseStatementBlock() throws UnexpectedTokenException, InvalidTokenException, IOException {
        StatementBlock block = new StatementBlock();

        while (getOptionalToken(TokenAttributes.statementTypes)) {
            block.addStatement(parseStatement());
        }

        getNextToken(TokenType.CURLY_CLOSE);

        return block;
    }

    StatementBlock parseSingleStatement() throws UnexpectedTokenException, InvalidTokenException, IOException {
        StatementBlock block = new StatementBlock();
        block.addStatement(parseStatement());
        return block;
    }

    Statement parseStatement() throws IOException, InvalidTokenException, UnexpectedTokenException {

        switch (currentToken().getType()) {
            case IDENTIFIER:
                return parseAssignOrFunctionCall();

            case IF:
                return parseIfStatement();

            case WHILE:
                return parseWhileStatement();

            case RETURN:
                return parseReturnStatement();

            case INT:
            case DOUBLE:
            case CURRENCY:
                return parseInitStatement();

            default:
                throw new UnexpectedTokenException(currentToken().getLine(),
                        currentToken().getPosition(),
                        TokenAttributes.statementTypes,
                        currentToken().getType()
                );
        }
    }

    Statement parseAssignOrFunctionCall() throws UnexpectedTokenException, InvalidTokenException, IOException {
        String identifier = currentToken().getValue();

        if (getOptionalToken(TokenType.ASSIGNMENT)) {
            return parseAssignStatement(identifier);
        } else if (getOptionalToken(TokenType.ROUND_OPEN)) {
            FunctionCall call = parseFunctionCall(identifier);
            getNextToken(TokenType.SEMICOLON);
            return call;
        }

        throw new UnexpectedTokenException(
                currentToken().getLine(),
                currentToken().getPosition(),
                TokenAttributes.statementTypes,
                currentToken().getType()
        );
    }

    FunctionCall parseFunctionCall(String identifier) throws UnexpectedTokenException, InvalidTokenException, IOException {
        FunctionCall functionCall = new FunctionCall(identifier);

        if (getOptionalToken(TokenType.STRING)) {
            return parsePrintMethod(functionCall);
        }

        while (!getOptionalToken(TokenType.ROUND_CLOSE)) {
            functionCall.addArgument(parseExpression());
            getOptionalToken(TokenType.COMMA);
        }

        return functionCall;
    }

    FunctionCall parsePrintMethod(FunctionCall functionCall) throws IOException, InvalidTokenException, UnexpectedTokenException {
        ExpressionNode expressionNode = new ExpressionNode();
        expressionNode.addOperand(new StringNode(currentToken().getValue()));
        functionCall.addArgument(expressionNode);
        getNextToken(TokenType.ROUND_CLOSE);

        return functionCall;
    }

    AssignStatement parseAssignStatement(String identifier) throws UnexpectedTokenException, InvalidTokenException, IOException {
        AssignStatement statement = new AssignStatement(
                identifier,
                parseExpression()
        );

        getNextToken(TokenType.SEMICOLON);

        return statement;
    }

    InitStatement parseInitStatement() throws UnexpectedTokenException, InvalidTokenException, IOException {
        InitStatement statement = new InitStatement(
                currentToken().getValue(),
                getNextToken(TokenType.IDENTIFIER).getValue()
        );

        if (getOptionalToken(TokenType.ASSIGNMENT)) {
            statement.setAssignable(parseExpression());
        }

        getNextToken(TokenType.SEMICOLON);

        return statement;
    }

    ReturnStatement parseReturnStatement() throws UnexpectedTokenException, InvalidTokenException, IOException {
        ReturnStatement statement = new ReturnStatement(parseExpression());

        getNextToken(TokenType.SEMICOLON);

        return statement;
    }

    IfStatement parseIfStatement() throws UnexpectedTokenException, InvalidTokenException, IOException {
        IfStatement statement = new IfStatement();

        getNextToken(TokenType.ROUND_OPEN);
        statement.setCondition(parseCondition());
        getNextToken(TokenType.ROUND_CLOSE);

        statement.setTrueBlock(parseSingleOrBlock());

        if (getOptionalToken(TokenType.ELSE)) {
            statement.setFalseBlock(parseSingleOrBlock());
        }

        return statement;
    }

    StatementBlock parseSingleOrBlock() throws IOException, InvalidTokenException, UnexpectedTokenException {
        if (getOptionalToken(TokenType.CURLY_OPEN)) {
            return parseStatementBlock();
        } else {
            getOptionalToken(TokenAttributes.statementTypes);
            return parseSingleStatement();
        }
    }

    WhileStatement parseWhileStatement() throws UnexpectedTokenException, InvalidTokenException, IOException {
        WhileStatement statement = new WhileStatement();

        getNextToken(TokenType.ROUND_OPEN);
        statement.setCondition(parseCondition());
        getNextToken(TokenType.ROUND_CLOSE);

        statement.setWhileBlock(parseSingleOrBlock());

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

        if (getOptionalToken(TokenAttributes.equalityOperators)) {
            condition.setOperator(currentToken().getType());
            condition.addOperand(parseRelationalCondition());
        }

        return condition;
    }

    Condition parseRelationalCondition() throws IOException, InvalidTokenException, UnexpectedTokenException {
        Condition condition = new Condition();

        condition.addOperand(parsePrimaryCondition());

        while (getOptionalToken(TokenAttributes.relationOperators)) {
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
            getNextToken(TokenType.ROUND_CLOSE);
        } else {
            condition = new Condition();
            condition.addOperand(parseExpression());
        }

        condition.setNegated(negation);
        return condition;
    }

    ExpressionNode parseExpression() throws IOException, InvalidTokenException, UnexpectedTokenException {
        ExpressionNode expressionNode = new ExpressionNode();

        expressionNode.addOperand(parseMultiplicativeExpression());

        while (getOptionalToken(TokenAttributes.additiveOperators)) {
            expressionNode.addOperation(currentToken().getType());
            expressionNode.addOperand(parseMultiplicativeExpression());
        }

        return expressionNode;
    }

    ExpressionNode parseMultiplicativeExpression() throws IOException, InvalidTokenException, UnexpectedTokenException {
        ExpressionNode expressionNode = new ExpressionNode();

        expressionNode.addOperand(parsePrimaryExpression());

        while (getOptionalToken(TokenAttributes.multiplicativeOperators)) {
            expressionNode.addOperation(currentToken().getType());
            expressionNode.addOperand(parsePrimaryExpression());
        }

        return expressionNode;
    }

    Expression parsePrimaryExpression() throws IOException, InvalidTokenException, UnexpectedTokenException {

        switch (advance().getType()) {
            case MINUS:
            case NUMBER: {
                return parseLiteral();
            }
            case ROUND_OPEN: {
                ExpressionNode expressionNode = parseExpression();
                getNextToken(TokenType.ROUND_CLOSE);
                return expressionNode;
            }

            case IDENTIFIER: {
                String identifier = currentToken().getValue();
                if (getOptionalToken(TokenType.ROUND_OPEN)) {
                    return parseFunctionCall(identifier);
                } else {
                    return new Variable(identifier);
                }
            }
        }

        throw new UnexpectedTokenException(
                currentToken().getLine(),
                currentToken().getPosition(),
                TokenAttributes.primaryExpressionTypes,
                currentToken().getType()
        );
    }

    Expression parseLiteral() throws IOException, InvalidTokenException, UnexpectedTokenException {
        Expression literal;
        int sign = getSignValue();

        if (currentToken().getValue().contains(".")) {
            literal = new DoubleNode(currentToken().getNumericValue().doubleValue() * sign);
        } else {
            literal = new IntNode(currentToken().getNumericValue().intValue() * sign);
        }

        return literal;
    }

    int getSignValue() throws IOException, InvalidTokenException, UnexpectedTokenException {
        if (currentToken().getType() == TokenType.MINUS) {
            getNextToken(TokenType.NUMBER);
            return -1;
        }

        return 1;
    }

    public Program parseProgram() throws IOException, InvalidTokenException, UnexpectedTokenException {
        Program program = new Program();

        while (getOptionalToken(TokenAttributes.valueTypes)) {
            program.addFunction(parseFunction());
        }

        return program;
    }

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }
}
