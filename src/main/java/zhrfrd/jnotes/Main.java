package zhrfrd.jnotes;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Simple JPanel Text Editor");
        JAreaText editor = new JAreaText();
        frame.add(new JScrollPane(editor));
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}
