package zhrfrd.jnotes.ui;

import zhrfrd.jnotes.command.*;
import zhrfrd.jnotes.buffer.GapBuffer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class JAreaText extends JPanel implements KeyListener, FocusListener {
    private static final String TITLE = "JNotes";
    private static final int START_X = 10;
    /** Vertically offsets the caret slightly, so it appears centered with the text. */
    private static final int CARET_Y_OFFSET = 5;
    private static final Color HIGHLIGHT_COLOR = new Color(50, 100, 200);
    private static final Color TEXT_COLOR = Color.WHITE;
    private final CommandManager commandManager;
    private final GapBuffer gapBuffer;
    private boolean caretVisible;
    JFrame frame;

    public JAreaText(JFrame frame) {
        this.frame = frame;
        gapBuffer = new GapBuffer();
        commandManager = new CommandManager();
        caretVisible = true;

        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        addFocusListener(this);

        // Set a monospace font so all characters have the same width.
        setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));

        // Timer for caret blinking
        Timer timer = new Timer(500, e -> {
            caretVisible = !caretVisible;
            repaint();
        });
        timer.start();
    }

    private void drawText(Graphics g, int lineHeight) {
        String text = getTextContent();
        FontMetrics fontMetrics = g.getFontMetrics();

        // Split text into lines and draw each line separately.
        String[] lines = text.split("\n", -1);   // -1 keeps empty strings at the end.

        // Draw highlight background if there's a selection
        if (gapBuffer.hasHighlight()) {
            drawHighlight(g, fontMetrics, lineHeight, text, lines);
        }

        // Draw the text with appropriate colors for selected/non-selected portions
        drawTextWithHighlight(g, lineHeight, lines);
    }

    /**
     * Renders the background highlight for the currently selected text region.
     * <p><b>Note: </b>It does not render any visible text itself. text rendering is handled separately by {@link #drawTextWithHighlight(Graphics, int, String[])}.</p>
     * @param g The {@link Graphics} context used to perform drawing.
     * @param fontMetrics The {@link FontMetrics} object used to measure text width.
     * @param lineHeight The height, in pixels, of a single line of text.
     * @param text The full text content currently stored in the editor buffer.
     * @param lines The text split and organized by lines.
     */
    private void drawHighlight(Graphics g, FontMetrics fontMetrics, int lineHeight, String text, String[] lines) {
        int highlightStart = Math.min(gapBuffer.getHighlightStart(), gapBuffer.getHighlightEnd());
        int highlightEnd = Math.max(gapBuffer.getHighlightStart(), gapBuffer.getHighlightEnd());

        if (highlightStart == highlightEnd) {
            return; // No visible highlight.
        }

        int charIndex = 0;
        for (int lineIndex = 0; lineIndex < lines.length; lineIndex ++) {
            String line = lines[lineIndex];
            int lineStart = charIndex;
            int lineEnd = charIndex + line.length();

            // Check if this line overlaps with the highlight.
            if (highlightEnd > lineStart && highlightStart < lineEnd) {
                int lineHighlightStart = Math.max(0, highlightStart - lineStart);
                int lineHighlightEnd = Math.min(line.length(), highlightEnd - lineStart);

                // Find pixel positions.
                int xStart = START_X + fontMetrics.stringWidth(line.substring(0, lineHighlightStart));
                int width = fontMetrics.stringWidth(line.substring(lineHighlightStart, lineHighlightEnd));
                int y = lineIndex * lineHeight;

                g.setColor(HIGHLIGHT_COLOR);
                g.fillRect(xStart, y, width, lineHeight);
            }

            charIndex += line.length() + 1; //   +1 for newline
        }
    }

    /**
     * Draws all visible text lines within the editor, including any highlighted text, on top of the
     * previously rendered highlight background.
     * @param g The {@link Graphics} context used to perform drawing.
     * @param lineHeight The height, in pixels, of a single line of text.
     * @param lines The text split and organized by lines.
     */
    private void drawTextWithHighlight(Graphics g, int lineHeight, String[] lines) {
        g.setColor(TEXT_COLOR);

        for (int lineIndex = 0; lineIndex < lines.length; lineIndex++) {
            String line = lines[lineIndex];
            int y = (lineIndex + 1) * lineHeight;
            g.drawString(line, START_X, y);
        }
    }

    private void drawCaret(Graphics g, int lineHeight) {
        Graphics2D g2 = (Graphics2D)g;

        String textUpToCursor = gapBuffer.getText(gapBuffer.getGapStart());   // Whole text until the caret.
        String[] lines = textUpToCursor.split("\n", -1);
        int currentLineIndex = lines.length - 1;   // Line index where the cursor is currently on.
        String lineTextUpToCursor = lines[currentLineIndex];   // Line text until the caret.

        int lineTextLength = g.getFontMetrics().stringWidth(lineTextUpToCursor);
        int caretX = START_X + lineTextLength;
        int caretY = (currentLineIndex * lineHeight) + CARET_Y_OFFSET;

        g2.setStroke(new BasicStroke(2));
        g2.drawLine(caretX, caretY, caretX, caretY + lineHeight - 2);
    }

    /**
     * Update the buffer content and replace the {@link JAreaText} content with the given {@link String}.
     * @param content The new {@link String} to replace the current {@link JAreaText} and buffer.
     */
    public void setTextContent(String content) {
        gapBuffer.loadText(content);
        repaint();
    }

    public void setFrameTitle(String title) {
        frame.setTitle(TITLE + " | " + title);
    }

    /**
     * Returns the full text currently stored in the editor {@link GapBuffer}.
     * @return The complete text content of the {@link JAreaText} as a {@link String}.
     */
    public String getTextContent() {
        return gapBuffer.getText();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int lineHeight = g.getFontMetrics().getHeight();
        drawText(g, lineHeight);

        if (caretVisible && isFocusOwner()) {
            drawCaret(g, lineHeight);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        boolean isModifierDown = e.isControlDown() || e.isMetaDown();
        caretVisible = true;

        if (isModifierDown && (e.getKeyCode() == KeyEvent.VK_Y || (e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_Z))) {
            commandManager.redo();
        } else if (isModifierDown && e.getKeyCode() == KeyEvent.VK_Z) {
            commandManager.undo();
        } else if (isModifierDown && e.getKeyCode() == KeyEvent.VK_C) {
            commandManager.execute(new CopyCommand(gapBuffer));
        } else if (isModifierDown && e.isShiftDown() && (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN)) {
            commandManager.execute(new HighlightCommand(gapBuffer, e.getKeyCode(), true));
        } else if ((isModifierDown || e.getKeyCode() == KeyEvent.VK_HOME) && (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT)) {
            commandManager.execute(new MoveCursorCommand(gapBuffer, e.getKeyCode(), true));
        } else if (isModifierDown && e.getKeyCode() == KeyEvent.VK_V) {
            commandManager.execute(new PasteCommand(gapBuffer));
        } else if (e.isShiftDown() && (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN)) {
            commandManager.execute(new HighlightCommand(gapBuffer, e.getKeyCode(), false));
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
            commandManager.execute(new MoveCursorCommand(gapBuffer, e.getKeyCode(), false));
        } else if (e.getKeyCode() == KeyEvent.VK_DELETE || (isModifierDown && e.getKeyCode() == KeyEvent.VK_BACK_SPACE)) {
            commandManager.execute(new DeleteNextCharCommand(gapBuffer));
        } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            commandManager.execute(new DeletePreviousCharCommand(gapBuffer));
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            commandManager.execute(new InsertCharCommand(gapBuffer, '\n'));
        } else {
            char c = e.getKeyChar();
            if (Character.isDefined(c) && !Character.isISOControl(c)) {
                commandManager.execute(new InsertCharCommand(gapBuffer, c));
            }
        }

        repaint();
        e.consume();
    }

    @Override
    public void focusGained(FocusEvent e) {
        caretVisible = true;
        setCursor(new Cursor(Cursor.TEXT_CURSOR));
        repaint();
    }

    @Override
    public void focusLost(FocusEvent e) {
        caretVisible = false;
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}