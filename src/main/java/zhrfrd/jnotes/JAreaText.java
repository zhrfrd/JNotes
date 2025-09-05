package zhrfrd.jnotes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class JAreaText extends JPanel implements KeyListener, FocusListener {
    private static final int START_X = 10;
    private GapBuffer gapBuffer;
    private int caretRow;
    private int caretCol;
    private boolean caretVisible;

    public JAreaText() {
        gapBuffer = new GapBuffer();
        caretRow = 0;
        caretCol = 0;
        caretVisible = true;

        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        addFocusListener(this);

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
        g.drawString(text, START_X, lineHeight);
    }

    private void drawCaret(Graphics g, int lineHeight) {
        Graphics2D g2 = (Graphics2D)g;
        int lineTextLength = g.getFontMetrics().stringWidth(gapBuffer.getText(0, gapBuffer.getGapStart()));
        int caretX = START_X + lineTextLength;
        int caretY = caretRow * lineHeight + 5;
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
            gapBuffer.insert(c);
        } else if (c == KeyEvent.VK_ENTER) {   // New line
            caretRow ++;
        } else if (c == KeyEvent.VK_BACK_SPACE) {   // Backspace
            gapBuffer.delete();
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        caretVisible = true;

        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
            gapBuffer.moveCursor(e.getKeyCode());
            repaint();
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