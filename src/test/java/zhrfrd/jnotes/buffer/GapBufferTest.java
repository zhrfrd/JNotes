package zhrfrd.jnotes.buffer;

import org.junit.jupiter.api.Test;
import zhrfrd.jnotes.util.Direction;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class GapBufferTest {
    @Test
    void insert_charInsideBuffer_decreasesGapSize() {
        GapBuffer gapBuffer = new GapBuffer();
        int initialGapSize = gapBuffer.getGapEnd() - gapBuffer.getGapStart();;

        gapBuffer.insert('a');
        gapBuffer.insert('b');

        int newGapSize = gapBuffer.getGapEnd() - gapBuffer.getGapStart();

        assertEquals(initialGapSize - 2, newGapSize, "Gap size should decrease by 2 after inserting 2 chars.");
    }

    @Test
    void insert_charInsideBuffer_doublesTheBufferSizeWhenGapSizeIsMinorOrEqualZero() {
        GapBuffer gapBuffer = new GapBuffer(102);
        int initialBufferSize = gapBuffer.getBufferSize();
        int initialGapSize = gapBuffer.getGapEnd() - gapBuffer.getGapStart();
        Random random = new Random();

        for (int i = 0; i < initialGapSize; i ++) {
            char c = (char)('a' + random.nextInt(26));
            gapBuffer.insert(c);
        }

        int gapBufferSizeBeforeResize = gapBuffer.getBufferSize();
        gapBuffer.insert('z');  // This insert will trigger resize

        int newBufferSize = gapBuffer.getBufferSize();

        assertEquals(initialBufferSize * 2, newBufferSize, "Buffer size should double when gap size reaches 0 and a character is inserted.");
        assertEquals(gapBufferSizeBeforeResize, initialBufferSize, "Buffer should not have resized before gap size reached 0.");
    }

    @Test
    void delete_charInsideBufferToTheLeft_increaseGapSize() {
        GapBuffer gapBuffer = new GapBuffer(100);
        Random random = new Random();

        for (int i = 0; i < 30; i ++) {
            char c = (char)('a' + random.nextInt(26));
            gapBuffer.insert(c);
        }

        int initialGapSize = gapBuffer.getGapEnd() - gapBuffer.getGapStart();
        gapBuffer.deleteChar(Direction.LEFT);
        gapBuffer.deleteChar(Direction.LEFT);
        int newGapSize = gapBuffer.getGapEnd() - gapBuffer.getGapStart();

        assertEquals(initialGapSize + 2, newGapSize, "The gap size must increase by 1 at each deletion.");
    }

    @Test
    void delete_charInsideBufferToTheRight_increaseGapSize() {
        GapBuffer gapBuffer = new GapBuffer(100);
        Random random = new Random();

        for (int i = 0; i < 30; i ++) {
            char c = (char)('a' + random.nextInt(26));
            gapBuffer.insert(c);
        }

        int initialGapSize = gapBuffer.getGapEnd() - gapBuffer.getGapStart();
        gapBuffer.deleteChar(Direction.RIGHT);
        gapBuffer.deleteChar(Direction.RIGHT);
        int newGapSize = gapBuffer.getGapEnd() - gapBuffer.getGapStart();

        assertEquals(initialGapSize + 2, newGapSize, "The gap size must increase by 1 at each deletion.");
    }

    @Test
    void getCharBeforeCursor_cursorAtTheBeginning_returnsNullChar() {
        GapBuffer gapBuffer = new GapBuffer(100);
        Random random = new Random();

        for (int i = 0; i < 30; i ++) {
            char c = (char)('a' + random.nextInt(26));
            gapBuffer.insert(c);
        }

        for (int i = 0; i < 30; i ++) {
            gapBuffer.moveCursor(Direction.LEFT, false);
        }

        assertEquals(0, gapBuffer.getGapStart(), "Cursor should be at position 0 (beginning).");
        assertEquals('\0', gapBuffer.getCharBeforeCursor(), "getCharBeforeCursor should return null character ('\\0') when cursor is at the beginning.");
    }

    @Test
    void getCharBeforeCursor_cursorWithinText_returnsCharBeforeCursor() {
        GapBuffer gapBuffer = new GapBuffer(100);
        gapBuffer.insert('a');
        gapBuffer.insert('b');
        gapBuffer.insert('c');
        gapBuffer.insert('d');
        gapBuffer.insert('e');
        gapBuffer.insert('f');
        gapBuffer.insert('g');

        for (int i = 0; i < 4; i ++) {
            gapBuffer.moveCursor(Direction.LEFT, false);
        }

        assertEquals(3, gapBuffer.getGapStart(), "Cursor should be moved 4 positions to the left.");
        assertEquals('c', gapBuffer.getCharBeforeCursor(), "The character before the cursor should be 'c'.");
    }

    @Test
    void getCharAfterCursor_cursorAtTheEnd_returnsNullChar() {
        GapBuffer gapBuffer = new GapBuffer(100);
        Random random = new Random();

        for (int i = 0; i < 30; i ++) {
            char c = (char)('a' + random.nextInt(26));
            gapBuffer.insert(c);
        }

        assertEquals(gapBuffer.getBufferSize(), gapBuffer.getGapEnd(), "Cursor should be at the end of the text.");
        assertEquals('\0', gapBuffer.getCharAfterCursor(), "getCharAfterCursor should return null character ('\0') when cursor is at the end.");
    }

    @Test
    void getCharAfterCursor_cursorWithinText_returnsCharAfterCursor() {
        GapBuffer gapBuffer = new GapBuffer(100);
        gapBuffer.insert('a');
        gapBuffer.insert('b');
        gapBuffer.insert('c');
        gapBuffer.insert('d');
        gapBuffer.insert('e');
        gapBuffer.insert('f');
        gapBuffer.insert('g');

        for (int i = 0; i < 4; i ++) {
            gapBuffer.moveCursor(Direction.LEFT, false);
        }

        assertEquals(3, gapBuffer.getGapStart(), "Cursor should be moved 4 positions to the left.");
        assertEquals('d', gapBuffer.getCharAfterCursor(), "The character after the cursor should be 'd'.");
    }

    @Test
    void moveCursor_left_movesGapLeft() {
        GapBuffer gapBuffer = new GapBuffer(100);
        gapBuffer.insert('a');
        gapBuffer.insert('b');
        gapBuffer.insert('c');
        
        int initialPosition = gapBuffer.getGapStart();
        gapBuffer.moveCursor(Direction.LEFT, false);
        
        assertEquals(initialPosition - 1, gapBuffer.getGapStart(), "Gap should move left by 1 position.");
        assertEquals('b', gapBuffer.getCharBeforeCursor(), "Character after cursor should be 'b'.");
    }

    @Test
    void moveCursor_leftWhenAtBeginning_cursorStaysAtSamePosition() {
        GapBuffer gapBuffer = new GapBuffer(100);
        gapBuffer.insert('a');
        gapBuffer.insert('b');
        gapBuffer.moveCursor(Direction.LEFT, false);
        gapBuffer.moveCursor(Direction.LEFT, false);
        
        int positionAtStart = gapBuffer.getGapStart();
        gapBuffer.moveCursor(Direction.LEFT, false);
        
        assertEquals(positionAtStart, gapBuffer.getGapStart(), "Cursor should not move to the left when at beginning.");
        assertEquals('\0', gapBuffer.getCharBeforeCursor(), "Should return null character at beginning.");
    }

    @Test
    void moveCursor_right_movesGapRight() {
        GapBuffer gapBuffer = new GapBuffer(100);
        gapBuffer.insert('a');
        gapBuffer.insert('b');
        gapBuffer.insert('c');
        
        // Move cursor left first to have some characters at its right.
        gapBuffer.moveCursor(Direction.LEFT, false);
        gapBuffer.moveCursor(Direction.LEFT, false);
        
        int positionBeforeMove = gapBuffer.getGapStart();
        gapBuffer.moveCursor(Direction.RIGHT, false);
        
        assertEquals(positionBeforeMove + 1, gapBuffer.getGapStart(), "Cursor should move right by 1 position.");
        assertEquals('b', gapBuffer.getCharBeforeCursor(), "Character before cursor should be 'b'.");
    }

    @Test
    void moveCursor_rightWhenAtTheEnd_cursorStaysAtSamePosition() {
        GapBuffer gapBuffer = new GapBuffer(100);
        gapBuffer.insert('a');
        gapBuffer.insert('b');
        gapBuffer.insert('c');
        
        int initialGapStart = gapBuffer.getGapStart();
        gapBuffer.moveCursor(Direction.RIGHT, false);
        
        assertEquals(initialGapStart, gapBuffer.getGapStart(), "Cursor should not move right when at the end of the text.");
    }

    @Test
    void moveCursor_up_movesGapToPreviousLine() {
        GapBuffer gapBuffer = new GapBuffer(100);
        gapBuffer.insert('a');
        gapBuffer.insert('b');
        gapBuffer.insert('c');
        gapBuffer.insert('\n');
        gapBuffer.insert('d');
        gapBuffer.insert('e');
        gapBuffer.insert('f');
        gapBuffer.moveCursor(Direction.UP, false);

        assertEquals(gapBuffer.getCharBeforeCursor(), 'c', "The cursor should have moved up, after the letter 'c'.");
    }

    @Test
    void moveCursor_up_atFirstLine_doesNotMove() {
        GapBuffer gapBuffer = new GapBuffer(100);
        gapBuffer.insert('a');
        gapBuffer.insert('b');
        gapBuffer.insert('c');
        
        int positionAtFirstLine = gapBuffer.getGapStart();
        gapBuffer.moveCursor(Direction.UP, false);
        
        assertEquals(positionAtFirstLine, gapBuffer.getGapStart(), "Cursor should not move up when at the first line.");
    }

    @Test
    void moveCursor_upWhenPreviousLineIsLonger_movesToSameColumn() {
        GapBuffer gapBuffer = new GapBuffer(100);
        gapBuffer.insert('a');
        gapBuffer.insert('b');
        gapBuffer.insert('c');
        gapBuffer.insert('d');
        gapBuffer.insert('e');
        gapBuffer.insert('\n');
        gapBuffer.insert('f');
        gapBuffer.insert('g');
        gapBuffer.moveCursor(Direction.UP, false);

        assertEquals(gapBuffer.getCharBeforeCursor(), 'b', "The cursor should have moved up after the letter 'b'.");
    }

    @Test
    void moveCursor_upWhenPreviousLineIsShorter_movesToEndOfPreviousLine() {
        GapBuffer gapBuffer = new GapBuffer(100);
        gapBuffer.insert('a');
        gapBuffer.insert('b');
        gapBuffer.insert('\n');
        gapBuffer.insert('c');
        gapBuffer.insert('d');
        gapBuffer.insert('e');
        gapBuffer.insert('f');
        gapBuffer.moveCursor(Direction.LEFT, false);
        gapBuffer.moveCursor(Direction.UP, false);

        assertEquals(gapBuffer.getCharBeforeCursor(), 'b', "The cursor should have move up at the end of the line after letter 'c'.");
    }

    @Test
    void moveCursor_down_movesToNextLine() {
        GapBuffer gapBuffer = new GapBuffer(100);
        gapBuffer.insert('a');
        gapBuffer.insert('b');
        gapBuffer.insert('c');
        gapBuffer.insert('\n');  // New line
        gapBuffer.insert('d');
        gapBuffer.insert('e');
        gapBuffer.insert('f');
        gapBuffer.moveCursor(Direction.LEFT, false);
        gapBuffer.moveCursor(Direction.LEFT, false);
        gapBuffer.moveCursor(Direction.LEFT, false);
        gapBuffer.moveCursor(Direction.LEFT, false);
        
        int positionBeforeDown = gapBuffer.getGapStart();
        gapBuffer.moveCursor(Direction.DOWN, false);
        
        assertEquals(gapBuffer.getCharBeforeCursor(), 'f', "The cursor should have move to the next line after letter 'f'.");
    }

    @Test
    void moveCursor_down_atLastLine_doesNotMove() {
        GapBuffer gapBuffer = new GapBuffer(100);
        gapBuffer.insert('a');
        gapBuffer.insert('b');
        gapBuffer.insert('c');
        gapBuffer.insert('\n');
        gapBuffer.insert('d');
        gapBuffer.insert('e');
        
        int positionAtLastLine = gapBuffer.getGapStart();
        gapBuffer.moveCursor(Direction.DOWN, false);
        
        assertEquals(positionAtLastLine, gapBuffer.getGapStart(), "Cursor should not move down when at last line.");
    }

    @Test
    void moveCursor_downWhenNextLineIsLonger_movesToSameColumn() {
        GapBuffer gapBuffer = new GapBuffer(100);
        gapBuffer.insert('a');
        gapBuffer.insert('b');
        gapBuffer.insert('\n');
        gapBuffer.insert('c');
        gapBuffer.insert('d');
        gapBuffer.insert('e');
        gapBuffer.insert('f');
        gapBuffer.moveCursor(Direction.LEFT, false);
        gapBuffer.moveCursor(Direction.LEFT, false);
        gapBuffer.moveCursor(Direction.LEFT, false);
        gapBuffer.moveCursor(Direction.LEFT, false);
        gapBuffer.moveCursor(Direction.LEFT, false);
        gapBuffer.moveCursor(Direction.LEFT, false);
        gapBuffer.moveCursor(Direction.DOWN, false);

        assertEquals(gapBuffer.getCharBeforeCursor(), 'c', "The cursor should have moved down to the same column after letter 'a'.");
    }

    @Test
    void moveCursor_downWhenNextLineIsShorter_movesToEndOfNextLine() {
        GapBuffer gapBuffer = new GapBuffer(100);
        gapBuffer.insert('a');
        gapBuffer.insert('b');
        gapBuffer.insert('c');
        gapBuffer.insert('d');
        gapBuffer.insert('e');
        gapBuffer.insert('\n');
        gapBuffer.insert('f');
        gapBuffer.insert('g');
        gapBuffer.moveCursor(Direction.LEFT, false);
        gapBuffer.moveCursor(Direction.LEFT, false);
        gapBuffer.moveCursor(Direction.LEFT, false);
        gapBuffer.moveCursor(Direction.LEFT, false);
        gapBuffer.moveCursor(Direction.LEFT, false);
        gapBuffer.moveCursor(Direction.DOWN, false);

        assertEquals(gapBuffer.getCharBeforeCursor(), 'g', "The cursor should have moved up to the same column after letter 'g'.");
    }

    @Test
    void moveCursor_leftAndRight_preservesText() {
        GapBuffer gapBuffer = new GapBuffer(100);
        gapBuffer.insert('a');
        gapBuffer.insert('b');
        gapBuffer.insert('c');
        
        String originalText = gapBuffer.getText();
        gapBuffer.moveCursor(Direction.LEFT, false);
        gapBuffer.moveCursor(Direction.RIGHT, false);
        
        assertEquals(originalText, gapBuffer.getText(), "Text should remain unchanged after moving cursor.");
    }
}