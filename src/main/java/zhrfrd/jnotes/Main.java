package zhrfrd.jnotes;

import zhrfrd.jnotes.ui.JAreaTextMenuBar;
import zhrfrd.jnotes.ui.JAreaText;
import zhrfrd.jnotes.ui.JLineNumberPanel;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        System.setProperty("apple.laf.useScreenMenuBar", "true");   // macOS style menu bar

        JFrame frame = new JFrame("JNotes");
        JAreaText jAreaText = new JAreaText(frame);
        JScrollPane scrollPane = new JScrollPane(jAreaText);
        JLineNumberPanel lineNumbers = new JLineNumberPanel(jAreaText);
        scrollPane.setRowHeaderView(lineNumbers);

        frame.add(scrollPane);
        frame.setJMenuBar(new JAreaTextMenuBar(jAreaText));
        frame.setSize(1000, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
