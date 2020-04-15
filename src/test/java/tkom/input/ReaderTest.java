package tkom.input;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;

public class ReaderTest {

    Reader reader;

    private void initialiseReader(String input) {
        StringReader stringReader = new StringReader(input);
        reader = new Reader(stringReader);
    }

    private void skipLine() throws IOException {
        while (reader.read() != '\n') ;
        reader.read();
    }

    @Test(expected = FileNotFoundException.class)
    public void openNonExistingFile() throws FileNotFoundException {
        reader = new Reader(new FileReader("nonExistingFile"));
    }

    @Test
    public void getLineNumberWithoutReading() {
        final int expectedLine = 1;
        initialiseReader("1234");

        int line = reader.getLineNumber();

        assertEquals(expectedLine, line);
    }

    @Test
    public void getLineNumberOnNewLine() throws IOException {
        final int expectedLine = 2;
        initialiseReader("1234\r\nabcde");
        skipLine();

        int line = reader.getLineNumber();

        assertEquals(expectedLine, line);
    }

    @Test
    public void peek() throws IOException {
        final Character expectedCharacter = '1';
        initialiseReader("1234");

        Character peeked = reader.peek();
        Character read = reader.read();

        assertEquals(expectedCharacter, peeked);
        assertEquals(expectedCharacter, read);
    }

    @Test
    public void read() throws IOException {
        final Character expectedCharacter = '1';
        initialiseReader("123\r\nabcde");

        Character read = reader.read();

        assertEquals(expectedCharacter, read);
    }

    @Test
    public void readOnEmptyFile() throws IOException {
        final Character expectedCharacter = Character.UNASSIGNED;
        initialiseReader("");

        Character read = reader.read();

        assertEquals(expectedCharacter, read);
    }

    @Test
    public void getCharacterPositionWithoutReading() {
        final int expectedPosition = 0;
        initialiseReader("abcde");

        int position = reader.getCharacterPosition();

        assertEquals(expectedPosition, position);
    }

    @Test
    public void getCharacterPositionAfterReading() throws IOException {
        final int expectedPosition = 1;
        initialiseReader("abcde");

        reader.read();
        int position = reader.getCharacterPosition();

        assertEquals(expectedPosition, position);
    }

    @Test
    public void getCharacterPositionOnNewLine() throws IOException {
        final int expectedPosition = 1;
        initialiseReader("1234\r\nabcde");
        skipLine();

        int position = reader.getCharacterPosition();

        assertEquals(expectedPosition, position);
    }

    @Test(expected = IOException.class)
    public void unReadBeforeReading() throws IOException {
        initialiseReader("1234");

        reader.unRead();
    }

    @Test
    public void unRead() throws IOException {
        final Character expectedChar = '1';
        initialiseReader("123");

        reader.read();
        reader.unRead();
        Character c = reader.read();

        assertEquals(expectedChar, c);
    }
}