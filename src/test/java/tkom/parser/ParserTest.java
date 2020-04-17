package tkom.parser;

import org.junit.Test;
import tkom.Main;
import tkom.ast.Node;
import tkom.ast.nodes.*;
import tkom.error.InvalidTokenException;
import tkom.error.UnexpectedTokenException;
import tkom.lexer.Lexer;
import tkom.utils.NodeType;
import tkom.utils.Token;
import tkom.utils.TokenType;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;

    private void initializeParser(String tokenInput) {
        List<String> currencies = new ArrayList<>() {{
            add("EUR");
            add("PLN");
            add("USD");
        }};
        StringReader stringReader = new StringReader(tokenInput);
        Lexer lexer = new Lexer(stringReader, currencies);
        parser = new Parser(lexer);
    }

    private Function generateFunction(String returnType, String identifier) {
        Function function = new Function();
        function.setIdentifier(identifier);
        function.setReturnType(returnType);
        return function;
    }

    private void assertFunctions(Function expectedFunction, Function actual) {
        assertEquals(expectedFunction.getReturnType(), actual.getReturnType());
        assertEquals(expectedFunction.getIdentifier(), actual.getIdentifier());
        assertEquals(expectedFunction.getParameters().size(),
                actual.getParameters().size());

        for(int i = 0; i < expectedFunction.getParameters().size(); i++) {
            assertEquals(expectedFunction.getParameters().get(i).getIdentifier(),
                    actual.getParameters().get(i).getIdentifier());
            assertEquals(expectedFunction.getParameters().get(i).getReturnType(),
                    actual.getParameters().get(i).getReturnType());
        }
    }


    @Test
    public void getExpectedToken() throws UnexpectedTokenException, InvalidTokenException, IOException {
        Token expectedToken = new Token(TokenType.INT, "int", 1, 1);
        String input = "int a;";
        initializeParser(input);

        Token actual = parser.getToken(expectedToken.getType());

        assertEquals(expectedToken.getType(), actual.getType());
    }

    @Test(expected = UnexpectedTokenException.class)
    public void getUnexpectedToken() throws UnexpectedTokenException, InvalidTokenException, IOException {
        Token expectedToken = new Token(TokenType.INT, "int", 1, 1);
        String input = "EUR euro;";
        initializeParser(input);

        parser.getToken(expectedToken.getType());

    }

    @Test()
    public void getOptionalTokenTrue() throws InvalidTokenException, IOException {
        Token expectedToken = new Token(TokenType.INT, "int", 1, 1);
        String input = "int i;";
        initializeParser(input);

        boolean actual = parser.getOptionalToken(expectedToken.getType());

        assertTrue(actual);
    }

    @Test()
    public void getOptionalTokenFalse() throws InvalidTokenException, IOException {
        Token expectedToken = new Token(TokenType.INT, "int", 1, 1);
        String input = "EUR euro;";
        initializeParser(input);

        boolean actual = parser.getOptionalToken(expectedToken.getType());

        assertFalse(actual);
    }

    @Test
    public void getOptionalTokenTypesTrue() throws IOException, InvalidTokenException {
        List<TokenType> expectedTokenTypes = new ArrayList<>() {{
          add(TokenType.INT);
        }};
        String input = "int i;";
        initializeParser(input);

        boolean actual = parser.getOptionalTokenTypes(expectedTokenTypes);

        assertTrue(actual);
    }

    @Test
    public void getOptionalTokenTypesFalse() throws IOException, InvalidTokenException {
        List<TokenType> expectedTokenTypes = new ArrayList<>() {{
            add(TokenType.INT);
        }};
        String input = "EUR euro;";
        initializeParser(input);

        boolean actual = parser.getOptionalTokenTypes(expectedTokenTypes);

        assertFalse(actual);
    }

    @Test
    public void parsePrimaryExpressionDouble() throws UnexpectedTokenException, InvalidTokenException, IOException {
        DoubleNode expectedNode = new DoubleNode();
        expectedNode.setValue(12.0);
        String input = "12.0";
        initializeParser(input);

        DoubleNode actual = (DoubleNode) parser.parsePrimaryExpression();

        assertEquals((Double) expectedNode.getValue(), (Double) actual.getValue());
    }

    @Test
    public void parsePrimaryExpressionNegativeDouble() throws UnexpectedTokenException, InvalidTokenException, IOException {
        DoubleNode expectedNode = new DoubleNode();
        expectedNode.setValue(-12.0);
        String input = "-12.0";
        initializeParser(input);

        DoubleNode actual = (DoubleNode) parser.parsePrimaryExpression();

        assertEquals((Double) expectedNode.getValue(), (Double) actual.getValue());
    }

    @Test
    public void parsePrimaryExpressionInt() throws UnexpectedTokenException, InvalidTokenException, IOException {
        IntNode expectedNode = new IntNode();
        expectedNode.setValue(12);
        String input = "12";
        initializeParser(input);

        IntNode actual = (IntNode) parser.parsePrimaryExpression();

        assertEquals((Integer) expectedNode.getValue(), (Integer) actual.getValue());
    }

    @Test
    public void parsePrimaryExpressionNegativeInt() throws UnexpectedTokenException, InvalidTokenException, IOException {
        IntNode expectedNode = new IntNode();
        expectedNode.setValue(-12);
        String input = "-12";
        initializeParser(input);

        IntNode actual = (IntNode) parser.parsePrimaryExpression();

        assertEquals((Integer) expectedNode.getValue(), (Integer) actual.getValue());
    }

    @Test
    public void parsePrimaryExpressionParenthesis() throws UnexpectedTokenException, InvalidTokenException, IOException {
        Expression expectedNode = new Expression();
        String input = "(12 + 1)";
        initializeParser(input);

        Node actual = parser.parsePrimaryExpression();

        assertEquals(expectedNode.getType(), actual.getType());
    }

    @Test
    public void parsePrimaryExpressionIdentifierVariable() throws UnexpectedTokenException, InvalidTokenException, IOException {
        Variable expectedNode = new Variable("a");
        String input = "a";
        initializeParser(input);

        Variable actual = (Variable) parser.parsePrimaryExpression();

        assertEquals(expectedNode.getType(), actual.getType());
        assertEquals(expectedNode.getIdentifier(), actual.getIdentifier());
    }

    @Test
    public void parsePrimaryExpressionIdentifierFunctionCall() throws UnexpectedTokenException, InvalidTokenException, IOException {
        FunctionCall expectedNode = new FunctionCall();
        expectedNode.setIdentifier("min");
        String input = "min(a, 1 + 2, 2 * (a / 3))";
        initializeParser(input);

        FunctionCall actual = (FunctionCall) parser.parsePrimaryExpression();

        assertEquals(expectedNode.getType(), actual.getType());
        assertEquals(expectedNode.getIdentifier(), actual.getIdentifier());
    }

    @Test(expected = UnexpectedTokenException.class)
    public void parsePrimaryExpressionUnexpectedToken() throws UnexpectedTokenException, InvalidTokenException, IOException {
        String input = "return a;";
        initializeParser(input);

        parser.parsePrimaryExpression();
    }

    @Test
    public void parseMultiplicativeExpression() throws UnexpectedTokenException, InvalidTokenException, IOException {
        List<TokenType> expectedOperators = new ArrayList<>(){{
            add(TokenType.MULTIPLY);
            add(TokenType.DIVIDE);
        }};
        String input = "a * b / 2";
        initializeParser(input);

        Expression actual = parser.parseMultiplicativeExpression();

        assertEquals(expectedOperators.get(0), actual.getOperations().get(0));
        assertEquals(expectedOperators.get(1), actual.getOperations().get(1));
    }

    @Test
    public void parseAdditiveExpression() throws UnexpectedTokenException, InvalidTokenException, IOException {
        List<TokenType> expectedOperators = new ArrayList<>(){{
            add(TokenType.PLUS);
            add(TokenType.MINUS);
        }};
        String input = "a + b - 2";
        initializeParser(input);

        Expression actual = parser.parseExpression();

        assertEquals(expectedOperators.get(0), actual.getOperations().get(0));
        assertEquals(expectedOperators.get(1), actual.getOperations().get(1));
    }

    @Test
    public void parsePrimaryNegatedCondition() throws UnexpectedTokenException, InvalidTokenException, IOException {
        Condition expectedCondition = new Condition();
        expectedCondition.setNegated(true);
        String input = "!a";
        initializeParser(input);

        Condition actual = parser.parsePrimaryCondition();

        assertEquals(expectedCondition.isNegated(), actual.isNegated());
    }

    @Test
    public void parseRelationalCondition() throws UnexpectedTokenException, InvalidTokenException, IOException {
        Condition condition = new Condition();
        condition.setOperator(TokenType.GREATER);
        String input = "a > 2";
        initializeParser(input);

        Condition actual = parser.parseRelationalCondition();

        assertEquals(condition.getOperator(), actual.getOperator());
    }

    @Test
    public void parseEqualityCondition() throws UnexpectedTokenException, InvalidTokenException, IOException {
        Condition condition = new Condition();
        condition.setOperator(TokenType.EQUALITY);
        String input = "a == 2";
        initializeParser(input);

        Condition actual = parser.parseEqualityCondition();

        assertEquals(condition.getOperator(), actual.getOperator());
    }

    @Test
    public void parseAndCondition() throws UnexpectedTokenException, InvalidTokenException, IOException {
        Condition condition = new Condition();
        condition.setOperator(TokenType.AND);
        String input = "(a > 2) && (b > 2)";
        initializeParser(input);

        Condition actual = parser.parseAndCondition();

        assertEquals(condition.getOperator(), actual.getOperator());
    }

    @Test
    public void parseOrCondition() throws UnexpectedTokenException, InvalidTokenException, IOException {
        Condition condition = new Condition();
        condition.setOperator(TokenType.OR);
        String input = "(a > 2) || (b > 2)";
        initializeParser(input);

        Condition actual = parser.parseCondition();

        assertEquals(condition.getOperator(), actual.getOperator());
    }

    @Test
    public void parseIfStatement() throws IOException, InvalidTokenException, UnexpectedTokenException {
        IfStatement expectedStatement = new IfStatement();
        String input = "if ( a > b ) {}";
        initializeParser(input);

        parser.advance();
        IfStatement actual = (IfStatement) parser.parseStatement();

        assertEquals(expectedStatement.getType(), actual.getType());
        assertEquals(0, actual.getTrueBlock().getStatements().size());
        assertNull(actual.getFalseBlock());
    }

    @Test
    public void parseIfElseStatement() throws IOException, InvalidTokenException, UnexpectedTokenException {
        IfStatement expectedStatement = new IfStatement();
        String input = "if ( a > b ) {} else {}";
        initializeParser(input);

        parser.advance();
        IfStatement actual = (IfStatement) parser.parseStatement();

        assertEquals(expectedStatement.getType(), actual.getType());
        assertEquals(0, actual.getTrueBlock().getStatements().size());
        assertEquals(0, actual.getFalseBlock().getStatements().size());
    }

    @Test
    public void parseIfElseIfStatement() throws IOException, InvalidTokenException, UnexpectedTokenException {
        IfStatement expectedStatement = new IfStatement();
        String input = "if ( a > b ) {} else if (b == a) {} else {}";
        initializeParser(input);

        parser.advance();
        IfStatement actual = (IfStatement) parser.parseStatement();

        assertEquals(expectedStatement.getType(), actual.getType());
        assertEquals(0, actual.getTrueBlock().getStatements().size());
        assertEquals(expectedStatement.getType(),
                ((IfStatement) actual.getFalseBlock().getStatements().get(0)).getType());
    }

    @Test
    public void parseIfSingleStatement() throws IOException, InvalidTokenException, UnexpectedTokenException {
        IfStatement expectedStatement = new IfStatement();
        String input = "if ( a > b ) return a; else if (b == a) return b;";
        initializeParser(input);

        parser.advance();
        IfStatement actual = (IfStatement) parser.parseStatement();

        assertEquals(expectedStatement.getType(), actual.getType());
        assertEquals(NodeType.RETURN_STATEMENT,
                ((ReturnStatement) actual.getTrueBlock().getStatements().get(0)).getType());
        assertEquals(expectedStatement.getType(),
                ((IfStatement) actual.getFalseBlock().getStatements().get(0)).getType());
    }

    @Test
    public void parseReturnStatement() throws IOException, InvalidTokenException, UnexpectedTokenException {
        ReturnStatement expectedStatement = new ReturnStatement();
        Expression expression = new Expression();
        expression.addOperation(TokenType.PLUS);
        expectedStatement.setExpression(expression);
        String input = "return a + b;";
        initializeParser(input);

        parser.advance();
        ReturnStatement actual = (ReturnStatement) parser.parseStatement();

        assertEquals(expectedStatement.getType(), actual.getType());
        assertEquals(expectedStatement.getExpression().getType(), actual.getExpression().getType());
        assertEquals(expectedStatement.getExpression().getOperations().get(0),
                actual.getExpression().getOperations().get(0));
    }

    @Test
    public void parseWhileStatement() throws IOException, InvalidTokenException, UnexpectedTokenException {
        WhileStatement expectedStatement = new WhileStatement();
        StatementBlock block = new StatementBlock();
        AssignStatement statement = new AssignStatement();
        statement.setIdentifier("a");
        block.addStatement(statement);
        expectedStatement.setWhileBlock(block);
        String input = "while( a > b) {a = a * 2;}";
        initializeParser(input);

        parser.advance();
        WhileStatement actual = (WhileStatement) parser.parseStatement();

        assertEquals(expectedStatement.getType(), actual.getType());
        assertEquals(((AssignStatement) expectedStatement.getWhileBlock().getStatements().get(0)).getType(),
                ((AssignStatement) actual.getWhileBlock().getStatements().get(0)).getType());
        assertEquals(((AssignStatement) expectedStatement.getWhileBlock().getStatements().get(0)).getIdentifier(),
                ((AssignStatement) actual.getWhileBlock().getStatements().get(0)).getIdentifier());
    }

    @Test
    public void parseWhileSingleStatement() throws IOException, InvalidTokenException, UnexpectedTokenException {
        WhileStatement expectedStatement = new WhileStatement();
        StatementBlock block = new StatementBlock();
        AssignStatement statement = new AssignStatement();
        statement.setIdentifier("a");
        block.addStatement(statement);
        expectedStatement.setWhileBlock(block);
        String input = "while( a > b) a = a * 2;";
        initializeParser(input);

        parser.advance();
        WhileStatement actual = (WhileStatement) parser.parseStatement();

        assertEquals(expectedStatement.getType(), actual.getType());
        assertEquals(((AssignStatement) expectedStatement.getWhileBlock().getStatements().get(0)).getType(),
                ((AssignStatement) actual.getWhileBlock().getStatements().get(0)).getType());
        assertEquals(((AssignStatement) expectedStatement.getWhileBlock().getStatements().get(0)).getIdentifier(),
                ((AssignStatement) actual.getWhileBlock().getStatements().get(0)).getIdentifier());
    }

    @Test
    public void parseInitStatement() throws UnexpectedTokenException, InvalidTokenException, IOException {
        InitStatement expectedStatement = new InitStatement();
        expectedStatement.setIdentifier("a");
        expectedStatement.setReturnType("int");
        String input = "int a;";
        initializeParser(input);

        parser.advance();
        InitStatement actual = (InitStatement) parser.parseStatement();

        assertEquals(expectedStatement.getType(), actual.getType());
        assertEquals(expectedStatement.getReturnType(), actual.getReturnType());
        assertEquals(expectedStatement.getIdentifier(), actual.getIdentifier());
        assertNull(actual.getAssignable());
    }

    @Test
    public void parseInitStatementWithAssignable() throws UnexpectedTokenException, InvalidTokenException, IOException {
        InitStatement expectedStatement = new InitStatement();
        Expression expression = new Expression();
        expression.addOperation(TokenType.PLUS);
        expectedStatement.setIdentifier("a");
        expectedStatement.setReturnType("int");
        expectedStatement.setAssignable(expression);
        String input = "int a = 2 + b;";
        initializeParser(input);

        parser.advance();
        InitStatement actual = (InitStatement) parser.parseStatement();

        assertEquals(expectedStatement.getType(), actual.getType());
        assertEquals(expectedStatement.getReturnType(), actual.getReturnType());
        assertEquals(expectedStatement.getIdentifier(), actual.getIdentifier());
        assertEquals(expectedStatement.getAssignable().getOperations().get(0),
                actual.getAssignable().getOperations().get(0));
    }

    @Test
    public void parseAssignStatement() throws UnexpectedTokenException, InvalidTokenException, IOException {
        AssignStatement expectedStatement = new AssignStatement();
        Expression expression = new Expression();
        expression.addOperation(TokenType.PLUS);
        expectedStatement.setIdentifier("a");
        expectedStatement.setAssignable(expression);
        String input = "a = 2 + b;";
        initializeParser(input);

        parser.advance();
        AssignStatement actual = (AssignStatement) parser.parseStatement();

        assertEquals(expectedStatement.getType(), actual.getType());
        assertEquals(expectedStatement.getIdentifier(), actual.getIdentifier());
        assertEquals(expectedStatement.getAssignable().getOperations().get(0),
                actual.getAssignable().getOperations().get(0));
    }

    @Test
    public void parseFunctionCall() throws UnexpectedTokenException, InvalidTokenException, IOException {
        FunctionCall expectedStatement = new FunctionCall();
        Expression expression = new Expression();
        expression.addOperation(TokenType.PLUS);
        expectedStatement.setIdentifier("func");
        expectedStatement.addArgument(expression);
        expectedStatement.addArgument(expression);
        String input = "func(a + b, d + c);";
        initializeParser(input);

        parser.advance();
        FunctionCall actual = (FunctionCall) parser.parseStatement();

        assertEquals(expectedStatement.getType(), actual.getType());
        assertEquals(expectedStatement.getIdentifier(), actual.getIdentifier());
        assertEquals(expectedStatement.getArguments().get(0).getOperations().get(0),
                actual.getArguments().get(0).getOperations().get(0));
        assertEquals(expectedStatement.getArguments().size(), actual.getArguments().size());
    }

    @Test
    public void parseFunctionCallWithString() throws UnexpectedTokenException, InvalidTokenException, IOException {
        FunctionCall expectedStatement = new FunctionCall();
        expectedStatement.setIdentifier("print");
        String input = "print(\"test\");";
        initializeParser(input);

        parser.advance();
        FunctionCall actual = (FunctionCall) parser.parseStatement();

        assertEquals(expectedStatement.getType(), actual.getType());
        assertEquals(expectedStatement.getIdentifier(), actual.getIdentifier());
    }

    @Test(expected = UnexpectedTokenException.class)
    public void parseInvalidStatement() throws UnexpectedTokenException, InvalidTokenException, IOException {
        String input = "5 + 5;";
        initializeParser(input);

        parser.advance();
        parser.parseStatement();
    }

    @Test
    public void parseFunctionDefinition() throws IOException, InvalidTokenException, UnexpectedTokenException {
        Function expectedFunction = new Function();
        StatementBlock block = new StatementBlock();
        block.addStatement(new ReturnStatement());
        List<Signature> parameters = new ArrayList<>(){{
            add(new Signature("int", "a"));
            add(new Signature("double", "b"));
        }};
        expectedFunction.setParameters(parameters);
        expectedFunction.setIdentifier("func");
        expectedFunction.setReturnType("int");
        expectedFunction.setStatementBlock(block);
        String input = "int func(int a, double b) {return a + b;}";
        initializeParser(input);

        parser.advance();
        Function actual = parser.parseFunction();

        assertFunctions(expectedFunction, actual);

        assertEquals(expectedFunction.getStatementBlock().getStatements().size(),
                expectedFunction.getStatementBlock().getStatements().size());
        assertEquals(((ReturnStatement) expectedFunction.getStatementBlock().getStatements().get(0)).getType(),
                ((ReturnStatement) expectedFunction.getStatementBlock().getStatements().get(0)).getType());
    }

    @Test
    public void parseProgram() throws UnexpectedTokenException, InvalidTokenException, IOException {
        Program expectedProgram = new Program();
        expectedProgram.addFunction(generateFunction("int", "main"));
        expectedProgram.addFunction(generateFunction("EUR", "transfer"));
        String input = "int main() { EUR e = transfer();" +
                "while(e < 10) e = e * 2;" +
                "if ( e > 20 ) e = e / 1.5;" +
                "else e = e * 1.5; }" +
                "EUR transfer() {" +
                "return 5;}";
        initializeParser(input);

        Program actual = parser.parseProgram();

        assertEquals(expectedProgram.getFunctions().size(), actual.getFunctions().size());
        for(int i = 0; i < expectedProgram.getFunctions().size(); i++) {
            assertFunctions(expectedProgram.getFunctions().get(0),
                    actual.getFunctions().get(0));
        }
    }
}