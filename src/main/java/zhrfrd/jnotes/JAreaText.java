package zhrfrd.jnotes;

import javax.swing.*;
import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class JAreaText extends JPanel implements KeyListener, FocusListener {
    private final ArrayList<String> lines = new ArrayList<>();
    private static final int START_X = 10;
    private int caretRow = 0;
    private int caretCol = 0;
    private boolean caretVisible = true;

    public JAreaText() {
        lines.add(""); // start with one empty line
        setFocusable(true);
        addKeyListener(this);
        addFocusListener(this);

        // Timer for caret blinking
        Timer timer = new Timer(500, e -> {
            caretVisible = !caretVisible;
            repaint();
        });
        timer.start();
    }

    private void drawText(Graphics g, int lineHeight) {
        int tempLineHeight = lineHeight;
        // TODO: Needs to be more efficient. At the moment it's looping over and over at each Timer tick
        for (String line : lines) {
            g.drawString(line, START_X, tempLineHeight);
            tempLineHeight += lineHeight;
        }
    }

    private void drawCaret(Graphics g, int lineHeight) {
        String lineText = lines.get(caretRow).substring(0, caretCol);
        int lineTextLength = g.getFontMetrics().stringWidth(lineText);
        int caretX = START_X + lineTextLength;
        int caretY = caretRow * lineHeight + 5;
        g.drawLine(caretX, caretY, caretX, caretY + lineHeight - 5);
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
        String line = lines.get(caretRow);

        // Check if it's a real character and not a control character such as tabs, backspaces ...
        if (Character.isDefined(c) && !Character.isISOControl(c)) {
            lines.set(caretRow, line.substring(0, caretCol) + c + line.substring(caretCol));
            caretCol ++;
        } else if (c == '\n') {   // New line
            lines.add(caretRow + 1, line.substring(caretCol));
            lines.set(caretRow, line.substring(0, caretCol));
            caretRow ++;
            caretCol = 0;
        } else if (c == '\b') {   // Backspace
            if (caretCol > 0) {
                lines.set(caretRow, line.substring(0, caretCol - 1) + line.substring(caretCol));
                caretCol --;
            } else if (caretRow > 0) {
                caretCol = lines.get(caretRow - 1).length();
                lines.set(caretRow - 1, lines.get(caretRow - 1) + line);
                lines.remove(caretRow);
                caretRow --;
            }
        }

        repaint();
    }

    // Key pressed: handle arrows, delete, etc.
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if (caretCol > 0) {
                    caretCol --;
                } else if (caretRow > 0) {
                    caretRow --;
                    caretCol = lines.get(caretRow).length();
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (caretCol < lines.get(caretRow).length()) {
                    caretCol ++;
                } else if (caretRow < lines.size() - 1) {
                    caretRow ++;
                    caretCol = 0;
                }
                break;
            case KeyEvent.VK_UP:
                if (caretRow > 0) {
                    caretRow --;
                    caretCol = Math.min(caretCol, lines.get(caretRow).length());
                }
                break;
            case KeyEvent.VK_DOWN:
                if (caretRow < lines.size() - 1) {
                    caretRow ++;
                    caretCol = Math.min(caretCol, lines.get(caretRow).length());
                }
                break;
        }
        repaint();
    }

    @Override
    public void focusGained(FocusEvent e) {
        caretVisible = true;
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
