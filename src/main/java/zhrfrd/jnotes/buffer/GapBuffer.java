package zhrfrd.jnotes.buffer;

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

    public GapBuffer(int bufferSize) {
        buffer = new char[bufferSize];
        gapStart = 0;
        gapEnd = bufferSize;
    }

    /**
     * Insert a character inside the gap (cursor position) and reduce the gap size by the number of characters inserted.
     * If the size of the gap is 0, resize the gap.
     * @param c The character to be inserted.
     */
    public void insert(char c) {
        int gapSize = gapEnd - gapStart;

        if (gapSize <= 0) {
            resizeGapBuffer(buffer.length * 2);
        }

        buffer[gapStart] = c;
        gapStart ++;
    }

    /**
     * Insert a whole string in the gap (cursor position) and reduce the gap size by the number of size of the string inserted.
     * @param str String to be inserted.
     */
    protected void insert(String str) {
        for (char c : str.toCharArray()) {
            insert(c);
        }
    }

    /**
     * Delete the character starting from the cursor position to the left.
     */
    public void delete() {
        if (gapStart > 0) {
            gapStart --;
        }
        resizeGapBuffer(buffer.length);
    }

    /**
     * Get the character immediately before the cursor.
     * @return The character immediately before the cursor (to the left), or '\0' (null) if it's at the start of the buffer.
     */
    public char getCharBeforeCursor() {
        if (gapStart == 0) {
            return '\0';
        }

        return buffer[gapStart - 1];
    }

    /**
     * Change the cursor position by using the arrow keys. This method also updates the gap position in the buffer.
     * @param direction The {@code KeyEvent} Integer value of the direction input by the user:
     *                  {@code VK_LEFT}, {@code VK_RIGHT}, {@code VK_UP} and {@code VK_DOWN}.
     */
    public void moveCursor(int direction) {
        switch (direction) {
            case KeyEvent.VK_LEFT:
                if (gapStart > 0) {
                    gapStart --;
                    gapEnd --;
                    moveGap(KeyEvent.VK_LEFT);
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (gapEnd < buffer.length) {
                    gapStart ++;
                    gapEnd ++;
                    moveGap(KeyEvent.VK_RIGHT);
                }
                break;
            case KeyEvent.VK_UP: {
                String textUpToCursor = getText(getGapStart());   // NOTE: The cursor has not been moved yet.
                String[] linesUpToCursor = textUpToCursor.split("\n", -1);   // -1 keeps empty strings at the end.

                if (linesUpToCursor.length > 1) {
                    int currentLineIndex = textUpToCursor.lastIndexOf("\n");   // Total number of characters until the beginning of the current line.
                    int currentLineLengthBeforeGap = gapStart - currentLineIndex - 1;
                    String previousLine = linesUpToCursor[linesUpToCursor.length - 2];

                    if (previousLine.length() >= currentLineLengthBeforeGap) {
                        gapStart -= previousLine.length() + 1;
                        gapEnd -= previousLine.length() + 1;
                    } else {
                        gapStart -= currentLineLengthBeforeGap + 1;
                        gapEnd -= currentLineLengthBeforeGap + 1;
                    }
                    moveGap(KeyEvent.VK_UP);
                }
                break;
            }
            case KeyEvent.VK_DOWN: {
                String text = getText();
                String textUpToCursor = getText(getGapStart());   // NOTE: The cursor has not been moved yet.
                String[] linesUpToCursor = textUpToCursor.split("\n", -1);   // -1 keeps empty strings at the end.
                String[] lines = text.split("\n", -1);

                if (linesUpToCursor.length < lines.length) {
                    int charsCountBeforeCurrentLine = textUpToCursor.lastIndexOf("\n");   // Total number of characters until the beginning of the current line (Included backspaces).
                    int charsCountInCurrentLineBeforeGap = gapStart - charsCountBeforeCurrentLine - 1;
                    int currentLineLength = lines[linesUpToCursor.length - 1].length();
                    int charsCountInCurrentLineAfterGap = currentLineLength - charsCountInCurrentLineBeforeGap;
                    String nextLine = lines[linesUpToCursor.length];

                    if (nextLine.length() > charsCountInCurrentLineBeforeGap) {
                        gapStart += currentLineLength + 1;
                        gapEnd += currentLineLength + 1;
                    } else {
                        gapStart += charsCountInCurrentLineAfterGap + nextLine.length() + 1;
                        gapEnd += charsCountInCurrentLineAfterGap + nextLine.length() + 1;
                    }
                    moveGap(KeyEvent.VK_DOWN);
                }
                break;
            }
        }
    }

    /**
     * Move the gap in accordance to the direction input by the user.
     * <p><b>Note:</b> Moving the gap will also change the position of the character at the caret position accordingly.</p
     * @param direction The {@code KeyEvent} Integer value of the direction input by the user:
     *                  {@code VK_LEFT}, {@code VK_RIGHT}, {@code VK_UP} and {@code VK_DOWN}.
     */
    private void moveGap(int direction) {
        int charsCountAfterGap = buffer.length - gapEnd;
        char[] newBuffer = new char[buffer.length];

        if (direction != KeyEvent.VK_DOWN) {
            for (int i = 0; i < gapStart; i++) {
                newBuffer[i] = buffer[i];
            }
        }

        if (direction == KeyEvent.VK_LEFT) {
            char charToMove =  buffer[gapStart];
            newBuffer[gapEnd] = charToMove;

            for (int i = 1; i < charsCountAfterGap; i ++) {
                newBuffer[gapEnd + i] = buffer[gapEnd + i];
            }
        } else if (direction == KeyEvent.VK_RIGHT) {
            char charToMove =  buffer[gapEnd - 1];
            newBuffer[gapStart - 1] = charToMove;

            for (int i = 0; i < charsCountAfterGap; i ++) {
                newBuffer[gapEnd + i] = buffer[gapEnd + i];
            }
        } else if (direction == KeyEvent.VK_UP) {
            StringBuilder sb = new StringBuilder();   // TODO: Remove gapSize in the parameter

            // Create a StringBuilder from the buffer without the empty spaces of the gap.
            for (char c : buffer) {
                if (c != '\u0000') {
                    sb.append(c);
                }
            }

            for (int i = 0; i < charsCountAfterGap; i ++) {
                newBuffer[gapEnd + i] = sb.charAt(gapStart + i);
            }
        } else if (direction == KeyEvent.VK_DOWN) {
            StringBuilder sb = new StringBuilder();

            // Create a StringBuilder from the buffer without the empty spaces of the gap.
            for (char c : buffer) {
                if (c != '\u0000') {
                    sb.append(c);
                }
            }

            for (int i = 0; i < gapStart; i ++) {
                newBuffer[i] = sb.charAt(i);
            }
            for (int i = 0; i < charsCountAfterGap; i ++) {
                newBuffer[gapEnd + i] = sb.charAt(gapStart + i);
            }
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

    public String getText() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < gapStart; i ++) {
            sb.append(buffer[i]);
        }

        for (int i = gapEnd; i < buffer.length; i ++) {
            sb.append(buffer[i]);
        }

        return sb.toString();
    }

    /**
     * Get text up until the index indicated in the parameter.
     * @param end The index up until to extract the text.
     * @return The text in String format.
     */
    public String getText(int end) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < end; i ++) {
            sb.append(buffer[i]);
        }

        return sb.toString();
    }

    public int getGapStart() {
        return gapStart;
    }

    public int getGapEnd() {
        return gapEnd;
    }

    public int getBufferSize() {
        return buffer.length;
    }
}