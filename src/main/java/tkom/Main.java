package tkom;

import tkom.currency.Rates;
import tkom.input.JsonReader;
import tkom.lexer.Lexer;
import tkom.utils.Keywords;
import tkom.utils.Token;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        JsonReader jsonReader = new JsonReader();
        Rates rates = jsonReader.getRates("src/main/resources/rates.json");

        Lexer lexer = new Lexer("src/main/resources/test.txt", rates.getCurrencies());
        Token token;
        while ((token=lexer.nextToken()) != null) {
            if(token.getValue() == "") {
                System.out.println(token.getType());
            } else {
                System.out.println(token.getValue());
            }
        }
    }
}
