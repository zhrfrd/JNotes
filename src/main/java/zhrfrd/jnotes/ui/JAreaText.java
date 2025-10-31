package zhrfrd.jnotes.ui;

import zhrfrd.jnotes.command.CommandManager;
import zhrfrd.jnotes.command.DeleteCharCommand;
import zhrfrd.jnotes.buffer.GapBuffer;
import zhrfrd.jnotes.command.InsertCharCommand;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class JAreaText extends JPanel implements KeyListener, FocusListener {
    private static final int START_X = 10;
    /** Vertically offsets the caret slightly so it appears centered with the text. */
    private static final int CARET_Y_OFFSET = 5;
    private final GapBuffer gapBuffer;
    private final CommandManager commandManager;
    private boolean caretVisible;

    public JAreaText() {
        gapBuffer = new GapBuffer();
        commandManager = new CommandManager();
        caretVisible = true;

        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        addFocusListener(this);

        // Set a monospace font so all characters have the same width
        setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));

        // Timer for caret blinking
        Timer timer = new Timer(500, e -> {
            caretVisible = !caretVisible;
//            repaint();
        });
        timer.start();
    }

    private void drawText(Graphics g, int lineHeight) {
        g.setColor(Color.WHITE);
        String text = gapBuffer.getText();
        
        // Split text into lines and draw each line separately.
        String[] lines = text.split("\n", -1);   // -1 keeps empty strings at the end.
        
        for (int i = 0; i < lines.length; i++) {
            int y = (i + 1) * lineHeight;
            g.drawString(lines[i], START_X, y);
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
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();

        // Check if the character typed is a real character and not a control character such as tabs, backspaces etc.
        if (Character.isDefined(c) && !Character.isISOControl(c)) {
            commandManager.execute(new InsertCharCommand(gapBuffer, c));
        } else if (c == KeyEvent.VK_ENTER) {   // New line
            commandManager.execute(new InsertCharCommand(gapBuffer, '\n'));
        } else if (c == KeyEvent.VK_BACK_SPACE) {   // Backspace
            commandManager.execute(new DeleteCharCommand(gapBuffer));
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        boolean isModifierDown = e.isControlDown() || e.isMetaDown();
        caretVisible = true;

        // Handle Redo first so Cmd + Shift + Z does not get caught by Undo branch
        if (isModifierDown && (e.getKeyCode() == KeyEvent.VK_Y || (e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_Z))) {
            commandManager.redo();
            repaint();
            e.consume();
            return;
        }
        // Undo
        if (isModifierDown && e.getKeyCode() == KeyEvent.VK_Z) {
            commandManager.undo();
            repaint();
            e.consume();
            return;
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
            gapBuffer.moveCursor(e.getKeyCode());
            repaint();
            e.consume();
        }
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
    public void keyReleased(KeyEvent e) {}
}