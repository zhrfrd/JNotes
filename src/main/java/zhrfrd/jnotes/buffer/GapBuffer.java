package zhrfrd.jnotes.buffer;

import zhrfrd.jnotes.util.Direction;

public class GapBuffer {
    private final int DEFAULT_BUFFER_SIZE = 20;   // TODO: Change back to 1024. 20 is just for quickly test.
    /** Array that holds the text (and the gap). */
    private char[] buffer;
    /** The index of the beginning of the gap. It always matches the position of the cursor. */
    private int gapStart;
    /** The index of the end of the gap. */
    private int gapEnd;
    /** The text position where the selection starts (inclusive). -1 means no selection. */
    private int highlightStart = -1;
    /** The text position where the selection ends (exclusive). -1 means no selection. */
    private int highlightEnd = -1;

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
        clearHighlight();
        int gapSize = gapEnd - gapStart;

        if (gapSize <= 0) {
            resizeGapBuffer(buffer.length * 2);
        }

        buffer[gapStart] = c;
        gapStart ++;
    }

    public void insert(String text) {
        for (char c : text.toCharArray()) {
            insert(c);
        }
    }

    /**
     * Delete a character to the left or right of the cursor based on the direction provided.
     * @param direction The direction where to delete the character:
     *                  {@code VK_LEFT}, {@code VK_RIGHT}
     */
    public void deleteChar(Direction direction) {
        clearHighlight();

        if (direction == Direction.LEFT) {
            gapStart --;
        } else if (direction == Direction.RIGHT) {
            gapEnd ++;
        }

        resizeGapBuffer(buffer.length);
    }

    /**
     * Highlight text by extending the selection in the given direction.
     * If no selection exists, starts a new selection from the current cursor position.
     * @param direction The direction to extend the selection: {@code LEFT}, {@code RIGHT}, {@code UP}, {@code DOWN}.
     */
    public void highlightChar(Direction direction) {
        if (highlightStart == -1) {
            highlightStart = gapStart;   // gapStart is not yet updated by the cursor movement. This will happen when moveCursorPreserveSelection() is called.
        }
        
        moveCursorAndPreserveHighlight(direction);
        highlightEnd = gapStart;   // Update selection end to the new cursor position.

        // TODO: Maybe remove
        if (highlightStart == highlightEnd) {
            clearHighlight();
        }
    }

    /**
     * Change the cursor position by using the arrow keys. This method also updates the gap position in the buffer.
     * <p><b>Note:</b> This method clears any existing selection. Use highlightChar() to move cursor while maintaining selection.</p>
     * @param direction The direction to move the cursor: {@code LEFT}, {@code RIGHT}, {@code UP}, {@code DOWN}.
     */
    public void moveCursor(Direction direction) {
        clearHighlight();
        moveCursorAndPreserveHighlight(direction);
    }

    /**
     * Internal method to move the cursor without clearing the selection.
     * This is used by highlightChar() to move the cursor while maintaining selection state.
     * @param direction The direction to move the cursor: {@code LEFT}, {@code RIGHT}, {@code UP}, {@code DOWN}.
     */
    private void moveCursorAndPreserveHighlight(Direction direction) {
        switch (direction) {
            case LEFT:
                if (gapStart > 0) {
                    gapStart --;
                    gapEnd --;
                    moveGap(Direction.LEFT);
                }
                break;
            case RIGHT:
                if (gapEnd < buffer.length) {
                    gapStart ++;
                    gapEnd ++;
                    moveGap(Direction.RIGHT);
                }
                break;
            case UP: {
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
                    moveGap(Direction.UP);
                }
                break;
            }
            case DOWN: {
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
                    moveGap(Direction.DOWN);
                }
                break;
            }
        }
    }

    /**
     * Move the gap in accordance to the direction input by the user.
     * <p><b>Note:</b> Moving the gap will also change the position of the character at the caret position accordingly.</p>
     * @param direction The direction to move the gap: {@code LEFT}, {@code RIGHT}, {@code UP}, {@code DOWN}.
     */
    private void moveGap(Direction direction) {
        int charsCountAfterGap = buffer.length - gapEnd;
        char[] newBuffer = new char[buffer.length];

        if (direction != Direction.DOWN) {
            for (int i = 0; i < gapStart; i++) {
                newBuffer[i] = buffer[i];
            }
        }

        if (direction == Direction.LEFT) {
            char charToMove =  buffer[gapStart];
            newBuffer[gapEnd] = charToMove;

            for (int i = 1; i < charsCountAfterGap; i ++) {
                newBuffer[gapEnd + i] = buffer[gapEnd + i];
            }
        } else if (direction == Direction.RIGHT) {
            char charToMove =  buffer[gapEnd - 1];
            newBuffer[gapStart - 1] = charToMove;

            for (int i = 0; i < charsCountAfterGap; i ++) {
                newBuffer[gapEnd + i] = buffer[gapEnd + i];
            }
        } else if (direction == Direction.UP) {
            StringBuilder sb = new StringBuilder();

            // Create a StringBuilder from the buffer without the empty spaces of the gap.
            for (char c : buffer) {
                if (c != '\u0000') {
                    sb.append(c);
                }
            }

            for (int i = 0; i < charsCountAfterGap; i ++) {
                newBuffer[gapEnd + i] = sb.charAt(gapStart + i);
            }
        } else if (direction == Direction.DOWN) {
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
     * Rebuilds the buffer with a gap at the specified position.
     * @param newGapStart the desired start index of the gap
     * @param newGapSize  the size of the gap
     */
    private void rebuildBuffer(int newGapStart, int newGapSize) {
        int textLength = getText().length();
        int newBufferLength = newGapStart + newGapSize + (textLength - newGapStart);
        char[] newBuffer = new char[newBufferLength];

        String text = getText();

        // Copy text before the gap
        for (int i = 0; i < newGapStart; i++) {
            newBuffer[i] = text.charAt(i);
        }

        // Copy text after the gap
        for (int i = newGapStart; i < textLength; i ++) {
            newBuffer[newGapStart + newGapSize + (i - newGapStart)] = text.charAt(i);
        }

        buffer = newBuffer;
        gapStart = newGapStart;
        gapEnd = newGapStart + newGapSize;
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
        int newGapSize = newBufferLength - afterGapLength - gapStart;

        rebuildBuffer(gapStart, newGapSize);
    }

    public void setCursorPosition(int position) {
        position = Math.max(0, Math.min(position, getText().length()));   // Clamp to valid range

        if (position == gapStart) {
            return;
        }

        int gapSize = gapEnd - gapStart;

        rebuildBuffer(position, gapSize);
    }

    /**
     * Reset highlight boundaries.
     */
    public void clearHighlight() {
        highlightStart = -1;
        highlightEnd = -1;
    }

    /**
     * Check if there is an active text selection.
     * @return true if there is a selection, false otherwise.
     */
    public boolean hasHighlight() {
        return highlightStart != -1;
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
     * Get the character immediately after the cursor.
     * @return The character immediately after the cursor (to the right), or '\0' (null) if it's at the end of the buffer.
     */
    public char getCharAfterCursor() {
        if (gapEnd == buffer.length) {
            return '\0';
        }

        return buffer[gapEnd];
    }

    /**
     * Get the start position of the current selection in the text.
     * @return The start position of the highlight, or -1 if no selection exists.
     */
    public int getHighlightStart() {
        return highlightStart;
    }

    /**
     * Get the end position of the current selection in the text.
     * @return The end position of the highlight, or -1 if no selection exists.
     */
    public int getHighlightEnd() {
        return highlightEnd;
    }

    public String getHighlightedText() {
        if (getHighlightStart() == getHighlightEnd()) {
            return "";
        }

        String text = getText();
        int highlightStart = getHighlightStart();
        int highlightEnd = getHighlightEnd();

        return highlightStart < highlightEnd ? text.substring(highlightStart, highlightEnd) : text.substring(highlightEnd, highlightStart);
    }
}