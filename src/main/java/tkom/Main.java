package tkom;

import tkom.ast.Node;
import tkom.ast.Value;
import tkom.ast.nodes.Currency;
import tkom.ast.nodes.DoubleNode;
import tkom.ast.nodes.IntNode;
import tkom.ast.nodes.Program;
import tkom.currency.Rates;
import tkom.error.InvalidTokenException;
import tkom.error.RuntimeEnvironmentException;
import tkom.error.UndefinedReferenceException;
import tkom.error.UnexpectedTokenException;
import tkom.execution.Environment;
import tkom.input.JsonReader;
import tkom.lexer.Lexer;
import tkom.parser.Parser;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        JsonReader jsonReader = new JsonReader();

        try {
            Rates rates = jsonReader.getRates(new FileReader("src/main/resources/rates.json"));
            FileReader fileReader = new FileReader("src/main/resources/program.txt");
            Lexer lexer = new Lexer(fileReader, rates.getCurrencies());
            Parser parser = new Parser(lexer);
            Program program = parser.parseProgram();
            Environment environment = new Environment(program.getFunctions(), rates);
            Value v = environment.getFunction("main").execute(environment, new ArrayList<>());
            System.out.println(v);
        } catch (IOException | InvalidTokenException | UnexpectedTokenException | UndefinedReferenceException | RuntimeEnvironmentException e) {
            e.printStackTrace();
        }
    }
}
