package tkom.input;

import java.io.IOException;
import java.io.LineNumberReader;

public class Reader {

    LineNumberReader reader;
    private int characterPosition = 1;
    private Character previousCharacter = Character.UNASSIGNED;
    private static final int buffer = 4;

    private Character castToCharacter(int c) {
        if (c == -1) {
            return Character.UNASSIGNED;
        } else {
            return (char) c;
        }
    }

    public Reader(java.io.Reader abstractReader) {
        this.reader = new LineNumberReader(abstractReader);
    }

    public int getLineNumber() {
        return reader.getLineNumber() + 1;
    }

    public Character peek() throws IOException {
        reader.mark(buffer);
        int c = reader.read();
        reader.reset();
        return castToCharacter(c);
    }

    public Character read() throws IOException {
        if (previousCharacter == '\n') {
            characterPosition = 1;
        }

        reader.mark(buffer);
        int c = reader.read();
        previousCharacter = castToCharacter(c);
        characterPosition++;

        return previousCharacter;
    }

    public int getCharacterPosition() {
        return characterPosition;
    }

    public void unRead() throws IOException {
        reader.reset();
        characterPosition--;
    }

    public void close() throws IOException {
        reader.close();
    }

}
