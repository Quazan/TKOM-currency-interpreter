package tkom.input;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

public class Reader {

    LineNumberReader reader;
    private Integer characterPosition = -1;
    private Character previousCharacter = Character.UNASSIGNED;
    private static final Integer buffer = 4;

    private Character castToCharacter(int c) {
        if (c == -1) {
            return Character.UNASSIGNED;
        } else {
            return (char) c;
        }
    }

    public Reader(String fileName) throws FileNotFoundException {
        this.reader = new LineNumberReader(new FileReader(fileName));
    }

    public Integer getLineNumber() {
        return reader.getLineNumber();
    }

    public Character peek() throws IOException {
        reader.mark(buffer);
        int c = reader.read();
        reader.reset();
        return castToCharacter(c);
    }

    public Character read() throws IOException {
        if (previousCharacter == '\n') {
            characterPosition = -1;
        }

        reader.mark(buffer);
        int c = reader.read();
        previousCharacter = castToCharacter(c);
        characterPosition++;

        return previousCharacter;
    }

    public Integer getCharacterPosition() {
        return characterPosition;
    }

    public void unRead() throws IOException {
        reader.reset();
    }

}
