package zhrfrd.jnotes.command;

import zhrfrd.jnotes.buffer.GapBuffer;
import zhrfrd.jnotes.util.Direction;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class PasteCommand implements Command {
    private final GapBuffer gapBuffer;
    private String pastedText = null;

    public PasteCommand(GapBuffer gapBuffer) {
        this.gapBuffer = gapBuffer;
    }

    @Override
    public void execute() {
        try {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
                pastedText = (String) clipboard.getData(DataFlavor.stringFlavor);
            }
        } catch (IllegalStateException | UnsupportedFlavorException | IOException e) {
            System.err.println("Clipboard read failed: " + e.getMessage());
            return;
        }

        if (pastedText == null || pastedText.isEmpty()) {
            return;
        }

        gapBuffer.insert(pastedText);
    }

    @Override
    public void undo() {
        if (gapBuffer.getCharBeforeCursor() == '\0' || pastedText == null || pastedText.isEmpty()) {
            return;
        }

        for (int i = 0; i < pastedText.length(); i ++) {
            gapBuffer.deleteChar(Direction.LEFT);
        }
    }
}


