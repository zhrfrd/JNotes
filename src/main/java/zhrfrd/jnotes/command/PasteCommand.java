package zhrfrd.jnotes.command;

import zhrfrd.jnotes.buffer.GapBuffer;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class PasteCommand implements Command {
    private final GapBuffer gapBuffer;

    public PasteCommand(GapBuffer gapBuffer) {
        this.gapBuffer = gapBuffer;
    }

    @Override
    public void execute() {
        String text = null;

        try {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
                text = (String) clipboard.getData(DataFlavor.stringFlavor);
            }
        } catch (IllegalStateException | UnsupportedFlavorException | IOException e) {
            System.err.println("Clipboard read failed: " + e.getMessage());
            return;
        }

        if (text == null || text.isEmpty()) {
            return;
        }

        gapBuffer.insert(text);
    }

    @Override
    public void undo() {
    }
}


