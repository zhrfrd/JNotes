package zhrfrd.jnotes;

import java.awt.event.KeyEvent;

public class GapBuffer {
    private final int DEFAULT_BUFFER_SIZE = 20;   // TODO: Change back to 1024. 20 is just for quickly test.
    /** Array that holds the text (and the gap). */
    private char[] buffer;
    /** The index of the beginning of the gap. It always matches the position of the cursor. */
    private int gapStart;
    /** The index of the end of the gap */
    private int gapEnd;

    public GapBuffer() {
        buffer = new char[DEFAULT_BUFFER_SIZE];
        gapStart = 0;
        gapEnd = DEFAULT_BUFFER_SIZE;
    }

    /**
     * Insert a character inside the gap (cursor position) and reduce the gap size by the number of characters inserted.
     * If the size of the gap is 0, resize the gap.
     * @param c The character to be inserted.
     */
    protected void insert(char c) {
        int gapSize = gapEnd - gapStart;
        if (gapSize <= 1) {
            resizeGapBuffer(buffer.length * 2);
        }

        buffer[gapStart] = c;
        gapStart ++;
        System.out.println(buffer);
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

    /**
     * Delete the character starting from the cursor position.
     */
    protected void delete() {
        if (gapStart > 0) {
            gapStart --;
        }
        resizeGapBuffer(buffer.length);
        System.out.println(buffer);
    }

    /**
     * Change the cursor position by using the arrow keys. This method also updates the gap position in the buffer.
     * @param key Integer value of the arrow key pressed.
     */
    protected void moveCursor(int key) {
        switch (key) {
            case KeyEvent.VK_LEFT:
                if (gapStart > 0) {
                    gapStart --;
                    gapEnd --;
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (gapEnd < buffer.length) {
                    gapStart ++;
                    gapEnd ++;
                }
                break;
            case KeyEvent.VK_UP:
                break;
            case KeyEvent.VK_DOWN:
                break;
        }

        moveTextAfterGap();
        System.out.println(buffer);
    }
    /*
        abcdefghij__________
        abcdefghi__________j
    */
    private void moveTextAfterGap() {
        int afterGapLength = buffer.length - gapEnd;
        char[] newBuffer = new char[buffer.length];
        char charToMove = buffer[gapStart];

        for (int i = 0; i < gapStart; i ++) {
            newBuffer[i] = buffer[i];
        }

        newBuffer[gapEnd] = charToMove;

        for (int i = 1; i < afterGapLength; i ++) {
            newBuffer[i + gapEnd] = buffer[i + gapEnd];
        }

        buffer = newBuffer;
    }

    /**
     * Resize the current buffer and its gap.
     * The current logic doubles the current buffer size and this causes the gap to increase in size too.
     *
     * <p><b>Note:</b> gapStart doesn't change during the resize process because it matches the cursor position
     * and the preceding text remains unchanged.</p>
     *
     * @param newBufferLength The new buffer length to assign to the buffer. The new buffer length doesn't change
     *                        during deletion. It changes only if the gap is full during insertion.
     */
    protected void resizeGapBuffer(int newBufferLength) {
        int afterGapLength = buffer.length - gapEnd;
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

    protected String getText(int start, int end) {
        return String.valueOf(buffer).substring(start, end);
    }

    protected int getGapStart() {
        return gapStart;
    }

    protected int getGapEnd() {
        return gapEnd;
    }
}