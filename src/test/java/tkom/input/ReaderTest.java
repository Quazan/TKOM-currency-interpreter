package tkom.input;

import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.Assert.*;

public class ReaderTest {

    Reader reader;

    private void openFile(String path) {
        try {
            reader = new Reader(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void skipLine() throws IOException {
        while(reader.read() != '\n');
        reader.read();
    }

    @Test(expected = FileNotFoundException.class)
    public void openNonExistingFile() throws FileNotFoundException {
        reader = new Reader("notActualFile");
    }

    @Test
    public void getLineNumberWithoutReading() {
        final int expectedLine = 0;

        openFile("src/main/resources/readerTest.txt");
        int line = reader.getLineNumber();

        assertEquals(expectedLine, line);
    }

    @Test
    public void getLineNumberOnNewLine() throws IOException {
        final int expectedLine = 1;

        openFile("src/main/resources/readerTest.txt");

        skipLine();

        int line = reader.getLineNumber();

        assertEquals(expectedLine, line);
    }

    @Test
    public void peek() throws IOException {
        final Character expectedCharacter = '1';

        openFile("src/main/resources/readerTest.txt");

        Character peeked = reader.peek();
        Character read = reader.read();

        assertEquals(expectedCharacter, peeked);
        assertEquals(expectedCharacter, read);
    }

    @Test
    public void read() throws IOException {
        final Character expectedCharacter = '1';

        openFile("src/main/resources/readerTest.txt");

        Character read = reader.read();

        assertEquals(expectedCharacter, read);
    }

    @Test
    public void readOnEmptyFile() throws IOException {
        final Character expectedCharacter = Character.UNASSIGNED;

        openFile("src/main/resources/emptyTest.txt");

        Character read = reader.read();

        assertEquals(expectedCharacter, read);
    }

    @Test
    public void getCharacterPositionWithoutReading() {
        final int expectedPosition = -1;

        openFile("src/main/resources/readerTest.txt");

        int position = reader.getCharacterPosition();

        assertEquals(expectedPosition, position);
    }

    @Test
    public void getCharacterPositionAfterReading() throws IOException {
        final int expectedPosition = 0;

        openFile("src/main/resources/readerTest.txt");

        reader.read();
        int position = reader.getCharacterPosition();

        assertEquals(expectedPosition, position);
    }

    @Test
    public void getCharacterPositionOnNewLine() throws IOException {
        final int expectedPosition = 0;

        openFile("src/main/resources/readerTest.txt");

        skipLine();

        int position = reader.getCharacterPosition();

        assertEquals(expectedPosition, position);
    }

    @Test(expected = IOException.class)
    public void unReadBeforeReading() throws IOException {
        openFile("src/main/resources/readerTest.txt");

        reader.unRead();
    }

    @Test
    public void unRead() throws IOException {
        final Character expectedChar = '1';

        openFile("src/main/resources/readerTest.txt");

        reader.read();
        reader.unRead();
        Character c = reader.read();

        assertEquals(expectedChar, c);
    }
}