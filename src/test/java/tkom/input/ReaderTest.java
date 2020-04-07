package tkom.input;

import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import tkom.utils.Token;
import tkom.utils.TokenType;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class ReaderTest {

    Reader reader;
    private String testFilePath = "src/test/resources/readerTest.txt";
    private String emptyTestFilePath = "src/test/resources/emptyTest.txt";

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

        openFile(testFilePath);
        int line = reader.getLineNumber();

        assertEquals(expectedLine, line);
    }

    @Test
    public void getLineNumberOnNewLine() throws IOException {
        final int expectedLine = 1;

        openFile(testFilePath);

        skipLine();

        int line = reader.getLineNumber();

        assertEquals(expectedLine, line);
    }

    @Test
    public void peek() throws IOException {
        final Character expectedCharacter = '1';

        openFile(testFilePath);

        Character peeked = reader.peek();
        Character read = reader.read();

        assertEquals(expectedCharacter, peeked);
        assertEquals(expectedCharacter, read);
    }

    @Test
    public void read() throws IOException {
        final Character expectedCharacter = '1';

        openFile(testFilePath);

        Character read = reader.read();

        assertEquals(expectedCharacter, read);
    }

    @Test
    public void readOnEmptyFile() throws IOException {
        final Character expectedCharacter = Character.UNASSIGNED;

        openFile(emptyTestFilePath);

        Character read = reader.read();

        assertEquals(expectedCharacter, read);
    }

    @Test
    public void getCharacterPositionWithoutReading() {
        final int expectedPosition = -1;

        openFile(testFilePath);

        int position = reader.getCharacterPosition();

        assertEquals(expectedPosition, position);
    }

    @Test
    public void getCharacterPositionAfterReading() throws IOException {
        final int expectedPosition = 0;

        openFile(testFilePath);

        reader.read();
        int position = reader.getCharacterPosition();

        assertEquals(expectedPosition, position);
    }

    @Test
    public void getCharacterPositionOnNewLine() throws IOException {
        final int expectedPosition = 0;

        openFile(testFilePath);

        skipLine();

        int position = reader.getCharacterPosition();

        assertEquals(expectedPosition, position);
    }

    @Test(expected = IOException.class)
    public void unReadBeforeReading() throws IOException {
        openFile(testFilePath);

        reader.unRead();
    }

    @Test
    public void unRead() throws IOException {
        final Character expectedChar = '1';

        openFile(testFilePath);

        reader.read();
        reader.unRead();
        Character c = reader.read();

        assertEquals(expectedChar, c);
    }
}