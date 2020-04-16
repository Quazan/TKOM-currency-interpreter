package tkom;

import tkom.ast.nodes.Function;
import tkom.ast.nodes.Program;
import tkom.currency.Rates;
import tkom.error.InvalidTokenException;
import tkom.error.UnexpectedTokenException;
import tkom.input.JsonReader;
import tkom.lexer.Lexer;
import tkom.parser.Parser;
import tkom.utils.Token;
import tkom.utils.TokenType;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        JsonReader jsonReader = new JsonReader();
        Token token;

        try {
            Rates rates = jsonReader.getRates(new FileReader("src/main/resources/rates.json"));
            FileReader fileReader = new FileReader("src/main/resources/program.txt");
            Lexer lexer = new Lexer(fileReader, rates.getCurrencies());
            Parser parser = new Parser(lexer);

            Program program = parser.parseProgram();
            System.out.println(program);

        } catch (IOException | InvalidTokenException | UnexpectedTokenException e) {
            e.printStackTrace();
        }

    }
}
