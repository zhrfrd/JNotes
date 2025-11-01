package zhrfrd.jnotes.buffer;

import org.junit.jupiter.api.Test;

import java.awt.event.KeyEvent;
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
        assertEquals(gapBufferSizeBeforeResize, initialBufferSize, "Buffer should not have resized before gap size reached 0");
    }

    @Test
    void delete_charInsideBuffer_increaseGapSize() {
        GapBuffer gapBuffer = new GapBuffer(100);
        Random random = new Random();

        for (int i = 0; i < 30; i ++) {
            char c = (char)('a' + random.nextInt(26));
            gapBuffer.insert(c);
        }

        int initialGapSize = gapBuffer.getGapEnd() - gapBuffer.getGapStart();
        gapBuffer.delete();
        gapBuffer.delete();
        int newGapSize = gapBuffer.getGapEnd() - gapBuffer.getGapStart();

        assertEquals(initialGapSize + 2, newGapSize, "The gap size must increase by 1 at each deletion");
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
            gapBuffer.moveCursor(KeyEvent.VK_LEFT);
        }

        assertEquals(0, gapBuffer.getGapStart(), "Cursor should be at position 0 (beginning)");
        assertEquals('\0', gapBuffer.getCharBeforeCursor(), "getCharBeforeCursor should return null character ('\\0') when cursor is at the beginning");
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
            gapBuffer.moveCursor(KeyEvent.VK_LEFT);
        }

        assertEquals(3, gapBuffer.getGapStart(), "Cursor should be moved 4 positions to the left");
        assertEquals('c', gapBuffer.getCharBeforeCursor(), "The character before the cursor should be 'c'");
    }
}