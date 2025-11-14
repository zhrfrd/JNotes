package zhrfrd.jnotes.ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class JAreaTextMenuBar extends JMenuBar {
    private final JAreaText jAreaText;
    private Path currentFile = null;   // Stores last saved/opened file.

    public JAreaTextMenuBar(JAreaText jAreaText) {
        this.jAreaText = jAreaText;
        JMenu fileMenu = new JMenu("File");

        // Open File
        fileMenu.add(new JMenuItem(new AbstractAction("Open…") {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFile();
            }
        }));

        // Save
        fileMenu.add(new JMenuItem(new AbstractAction("Save") {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFile(false);
            }
        }));

        // Save As
        fileMenu.add(new JMenuItem(new AbstractAction("Save As…") {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFile(true);
            }
        }));

        add(fileMenu);
    }

    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();

        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile().toPath();

            try {
                String content = Files.readString(currentFile);
                jAreaText.setTextContent(content);
                setFrameTitle();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Failed to open file:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Saves the current {@link JAreaText} content to a file.
     * @param choosePath if {@code true}, always prompt the user to select a file path,
     *                   if {@code false}, save to the existing file path if available.
     */
    private void saveFile(boolean choosePath) {
        if (currentFile == null || choosePath) {
            JFileChooser chooser = new JFileChooser();

            if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                currentFile = chooser.getSelectedFile().toPath();
                setFrameTitle();
            } else {   // User cancelled action.
                return;
            }
        }

        try {
            Files.writeString(currentFile, jAreaText.getTextContent());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Failed to save file:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setFrameTitle() {
        int lastIndexSlash = currentFile.toString().lastIndexOf('/');
        String title = currentFile.toString().substring(lastIndexSlash + 1);
        jAreaText.setFrameTitle(title);
    }
}
