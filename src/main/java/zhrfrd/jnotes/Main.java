package zhrfrd.jnotes;

import zhrfrd.jnotes.ui.JAreaTextMenuBar;
import zhrfrd.jnotes.ui.JAreaText;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        System.setProperty("apple.laf.useScreenMenuBar", "true");   // macOS style menu bar

        JFrame frame = new JFrame("JNotes");
        JAreaText jAreaText = new JAreaText(frame);
        frame.add(new JScrollPane(jAreaText));
        frame.setJMenuBar(new JAreaTextMenuBar(jAreaText));
        frame.setSize(1000, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
