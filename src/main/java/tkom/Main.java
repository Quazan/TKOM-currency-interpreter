package tkom;

import tkom.ast.nodes.Function;
import tkom.ast.nodes.Program;
import tkom.currency.Rates;
import tkom.error.InvalidTokenException;
import tkom.input.JsonReader;
import tkom.lexer.Lexer;
import tkom.utils.Token;
import tkom.utils.TokenType;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        JsonReader jsonReader = new JsonReader();
        Token token;

        Program program = new Program();
        program.addFunction(new Function());

        try {
            Rates rates = jsonReader.getRates(new FileReader("src/main/resources/rates.json"));
            FileReader fileReader = new FileReader("src/main/resources/program.txt");
            Lexer lexer = new Lexer(fileReader, rates.getCurrencies());

            while ((token = lexer.nextToken()).getType() != TokenType.END_OF_FILE) {
                System.out.println(token);
            }
        } catch (IOException | InvalidTokenException e) {
            e.getMessage();
        }

    }
}
