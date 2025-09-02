package zhrfrd.jnotes;

import java.awt.event.KeyEvent;

public class GapBuffer {
    private final int DEFAULT_BUFFER_SIZE = 20;   // TODO: Change back to 1024. 20 is just for quickly test.
    /** Array that holds the text (and the gap). */
    private char[] buffer;
    /** The index of the beginning of the gap */
    private int gapStart;
    /** The index of the end of the gap */
    private int gapEnd;
    private int gapSize;

    public GapBuffer() {
        buffer = new char[DEFAULT_BUFFER_SIZE];
        gapStart = 0;
        gapEnd = DEFAULT_BUFFER_SIZE;
        gapSize = gapEnd - gapStart;
    }

    /**
     * Insert a character inside the gap (cursor position) and reduce the gap size by the number of characters inserted.
     * If the size of the gap is 0, resize the gap.
     * @param c The character to be inserted.
     */
    protected void insert(char c) {
        if (gapSize <= 1) {
            resizeGapBuffer();
        }

        buffer[gapStart] = c;
        gapStart ++;
        gapSize = gapEnd - gapStart;
        System.out.println(gapSize);
    }

    /**
     * Insert a string in the gap (cursor position) and reduce the gap size by the number of size of the string inserted.
     * @param str String to be inserted.
     */
    protected void insert(String str) {
        for (char c : str.toCharArray()) {
            insert(c);
        }
    }

    protected void delete() {
    }

    protected void moveCursor(int key) {
        switch (key) {
            case KeyEvent.VK_LEFT:
                break;
            case KeyEvent.VK_RIGHT:
                break;
            case KeyEvent.VK_UP:
                break;
            case KeyEvent.VK_DOWN:
                break;
        }
    }
    /*
    BUFFER                  BUFFER SIZE         GAP SIZE
    abc____lmn              10                  4
    abcd___lmn              10                  3
    abcde__lmn              10                  2
    abcdef_lmn              10                  1
    abcdefglmn              10                  0
    BUFFER                  NEW BUFFER SIZE     GAP SIZE
    abcdefg__________lmn    20                  10
    abcdefgh_________lmn    20                  10
    abcdefghi________lmn    20                  10
    abcdefghij_______lmn    20                  10
    abcdefghijk______lmn    20                  10
    */

    /**
     * Resize the current buffer and its gap.
     * The current logic doubles the current buffer size and this causes the gap to increase in size too.
     *
     * <p><b>Note:</b> gapStart doesn't change during the resize process because it matches the cursor position
     * and the preceding text remains unchanged.</p>
     */
    protected void resizeGapBuffer() {
        int afterGapLength = buffer.length - gapEnd;
        int newBufferLength = buffer.length * 2;
        char[] newBuffer = new char[newBufferLength];
        int newGapEnd = newBufferLength - afterGapLength;

        // Copy characters before the gap to the new temporary buffer.
        for (int i = 0; i < gapStart; i ++) {
            newBuffer[i] = buffer[i];
        }

        // Copy characters after the gap to the new temporary buffer (leaving a gap behind).
        for (int i = 0; i < afterGapLength; i ++) {
            newBuffer[i + newGapEnd] = buffer[i + gapEnd];
        }

        buffer = newBuffer;
        gapEnd = newGapEnd;
    }

    protected String getText() {
        return String.valueOf(buffer);
    }
}