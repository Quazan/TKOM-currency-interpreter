package tkom.input;

import com.google.gson.Gson;
import tkom.currency.Rates;

import java.io.Reader;

public class JsonReader {

    public Rates getRates(Reader reader) {
        Gson gson = new Gson();

        return gson.fromJson(reader, Rates.class);
    }

}
