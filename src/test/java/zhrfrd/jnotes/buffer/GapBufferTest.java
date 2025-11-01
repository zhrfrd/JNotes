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
    void insert_charInsideBufferWhenGapSizeIsMinorOrEqualZero_doublesTheBufferSize() {
        GapBuffer gapBuffer = new GapBuffer();
        int initialBufferSize = gapBuffer.getBufferLength();

        Random random = new Random();

        for (int i = 20; i >= 0; i --) {
            char c = (char)('a' + random.nextInt(26));
            gapBuffer.insert(c);
        }

        int newBufferSize = gapBuffer.getBufferLength();

        assertEquals(initialBufferSize * 2, newBufferSize, "Buffer size should double its size each time the gap size reaches 0.");
    }
}