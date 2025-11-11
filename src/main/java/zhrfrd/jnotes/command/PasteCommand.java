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
    private int positionAtInsert;
    private boolean executed = false;

    public PasteCommand(GapBuffer gapBuffer) {
        this.gapBuffer = gapBuffer;
    }

    @Override
    public void execute() {
        // First time execution: read clipboard and store position.
        if (!executed) {
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

            positionAtInsert = gapBuffer.getGapStart();
            executed = true;
        }

        if (pastedText == null || pastedText.isEmpty()) {
            return;
        }

        gapBuffer.setCursorPosition(positionAtInsert);   // Move cursor to the original insertion position.
        gapBuffer.insert(pastedText);   // Insert text at the stored position.
    }

    @Override
    public boolean isUndoable() {
        return true;
    }

    @Override
    public void undo() {
        if (pastedText == null || pastedText.isEmpty()) {
            return;
        }

        gapBuffer.setCursorPosition(positionAtInsert + pastedText.length());   // Move cursor to the end of pasted text

        for (int i = 0; i < pastedText.length(); i++) {
            gapBuffer.deleteChar(Direction.LEFT);
        }
    }
}
