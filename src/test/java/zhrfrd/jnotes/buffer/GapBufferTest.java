package zhrfrd.jnotes.buffer;

import org.junit.jupiter.api.Test;

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
        int initialBufferSize = gapBuffer.getBufferLength();
        int initialGapSize = gapBuffer.getGapEnd() - gapBuffer.getGapStart();

        Random random = new Random();

        // Insert characters until gap size reaches 0, which will trigger resize on next insert
        for (int i = 0; i < initialGapSize; i++) {
            char c = (char)('a' + random.nextInt(26));
            gapBuffer.insert(c);
        }

        // At this point, gapSize should be 0, so next insert will trigger resize
        int gapBufferSizeBeforeResize = gapBuffer.getBufferLength();
        gapBuffer.insert('z');  // This insert will trigger resize

        int newBufferSize = gapBuffer.getBufferLength();

        assertEquals(initialBufferSize * 2, newBufferSize, "Buffer size should double when gap size reaches 0 and a character is inserted.");
        assertEquals(gapBufferSizeBeforeResize, initialBufferSize, "Buffer should not have resized before gap size reached 0");
    }
}