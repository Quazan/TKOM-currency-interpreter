package tkom;

import tkom.currency.Rates;
import tkom.error.InvalidTokenException;
import tkom.input.JsonReader;
import tkom.lexer.Lexer;
import tkom.utils.Token;
import tkom.utils.TokenType;

import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InvalidTokenException {
        JsonReader jsonReader = new JsonReader();
        Rates rates = jsonReader.getRates(new FileReader("src/main/resources/rates.json"));

        FileReader fileReader = new FileReader("src/main/resources/program.txt");
        Lexer lexer = new Lexer(fileReader, rates.getCurrencies());
        Token token;

        while ((token = lexer.nextToken()).getType() != TokenType.END_OF_FILE) {
            System.out.println(token);
        }
    }
}
