package zhrfrd.jnotes;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    static final int SCREEN_WIDTH = 1000;
    static final int SCREEN_HEIGHT = SCREEN_WIDTH / 3 * 2; // ASPECT RATIO 16:9

    public MainWindow() {
        initializeLayout();
    }

    private void initializeLayout() {
        JPanel panelMain = new JPanel();
        panelMain.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        add(panelMain);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);
        setTitle("JNotes");
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
    }
}
