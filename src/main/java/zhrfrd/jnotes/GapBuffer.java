package zhrfrd.jnotes;

public class GapBuffer {
    private final int DEFAULT_GAP_SIZE = 1024;
    /** Array to hold the text */
    private char[] buffer;
    private int gapStart;
    private int gapEnd;

    public GapBuffer() {
        buffer = new char[DEFAULT_GAP_SIZE];
        gapStart = 0;
        gapEnd = DEFAULT_GAP_SIZE;
    }

    /**
     * Insert a character inside the gap (cursor position) and reduce the gap size by the number of characters inserted.
     * If the size of the gap is 0, resize the gap.
     * @param c The character to be inserted.
     */
    protected void insert(char c) {
        if (gapStart == gapEnd) {
            resize();
        }

        buffer[gapStart] = c;
        gapStart ++;
    }

    /**
     * Insert a string in the gap (cursor position).
     * @param str String to be added.
     */
    protected void insert(String str) {
        for (char c : str.toCharArray()) {
            insert(c);
        }
    }

    protected void delete() {
    }

    protected void moveCursor() {

    }

    protected void resize() {

    }
}