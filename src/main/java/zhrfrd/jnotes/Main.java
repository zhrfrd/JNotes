package zhrfrd.jnotes;

import zhrfrd.jnotes.ui.JAreaTextMenuBar;
import zhrfrd.jnotes.ui.JAreaText;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("JNotes");
        JAreaText editor = new JAreaText();
        frame.add(new JScrollPane(editor));
        frame.setJMenuBar(new JAreaTextMenuBar(editor));
        frame.setSize(1000, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
