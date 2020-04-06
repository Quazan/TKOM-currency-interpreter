package tkom.input;

import com.google.gson.Gson;
import tkom.currency.Rates;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class JsonReader {

   public Rates getRates(String path) {

       Gson gson = new Gson();
       Rates rates = null;
       try (Reader reader = new FileReader(path)) {
           rates = gson.fromJson(reader, Rates.class);

       } catch (IOException e) {
           e.printStackTrace();
       }

       return rates;
   }

}
