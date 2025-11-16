package zhrfrd.jnotes.ui;

import javax.swing.*;
import java.awt.*;

public class JLineNumberPanel extends JComponent {
    private final JAreaText jAreaText;
    private static final int PADDING = 5;   // Space between numbers and text.
    private static final Color BG_COLOR = new Color(25, 25, 25);     // darker than editor
    private static final Color NUMBER_COLOR = new Color(120, 120, 120);

    public JLineNumberPanel(JAreaText jAreaText) {
        this.jAreaText = jAreaText;
        setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(50, jAreaText.getHeight());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(BG_COLOR);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(NUMBER_COLOR);


        String text = jAreaText.getTextContent();
        String[] lines = text.split("\n", -1);

        int lineHeight = g.getFontMetrics().getHeight();
        int y = lineHeight;

        for (int i = 0; i < lines.length; i++) {
            String number = String.valueOf(i + 1);
            int x = getWidth() - g.getFontMetrics().stringWidth(number) - PADDING;

            g.drawString(number, x, y);
            y += lineHeight;
        }
    }
}
