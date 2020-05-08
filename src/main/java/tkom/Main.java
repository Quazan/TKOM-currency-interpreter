package tkom;

import tkom.ast.nodes.Program;
import tkom.currency.Rates;
import tkom.error.InvalidTokenException;
import tkom.error.RuntimeEnvironmentException;
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

        if(args.length != 2) {
            System.out.println("Invalid number of arguments");
            System.exit(0);
        }

        String programFile = args[0];
        String ratesFile = args[1];


        try {
            Rates rates = jsonReader.getRates(new FileReader(ratesFile));
            FileReader fileReader = new FileReader(programFile);
            Program program = parseProgram(fileReader, rates);
            runProgram(program, rates);
        } catch (IOException | InvalidTokenException | UnexpectedTokenException |
                RuntimeEnvironmentException e) {
            System.out.println(e.getMessage());
        }
    }

    public static Program parseProgram(FileReader reader, Rates rates) throws UnexpectedTokenException, InvalidTokenException, IOException {
        Lexer lexer = new Lexer(reader, rates.getCurrencies());
        Parser parser = new Parser(lexer);
        return parser.parseProgram();
    }

    public static void runProgram(Program program, Rates rates) throws RuntimeEnvironmentException {
        Environment environment = new Environment(program.getFunctions(), rates);
        environment.getFunction("main").execute(environment, new ArrayList<>());
    }
}