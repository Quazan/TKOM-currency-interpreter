package tkom.input;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

public class Reader {

    private LineNumberReader reader;
    private Integer characterPosition = -1;
    private Integer previousCharacter = 0;
    private static final Integer buffer = 4;

    public Reader(String fileName) throws FileNotFoundException {
        this.reader = new LineNumberReader(new FileReader(fileName));
    }

    public Integer getLineNumber() {
        return reader.getLineNumber();
    }

    public Integer peek() throws IOException {
        reader.mark(buffer);
        int c = reader.read();
        reader.reset();
        return c;
    }

    public Integer read() throws IOException {
        if (previousCharacter == 10) {
            characterPosition = -1;
        }

        reader.mark(buffer);
        previousCharacter = reader.read();
        characterPosition++;

        //System.out.println(previousCharacter);

        return previousCharacter;
    }

    public Integer getCharacterPosition() {
        return characterPosition;
    }

    public void unRead() throws IOException {
        reader.reset();
    }

}
