package zhrfrd.jnotes.command;

import zhrfrd.jnotes.buffer.GapBuffer;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;

public class CopyCommand implements Command {
    private final GapBuffer gapBuffer;

    public CopyCommand(GapBuffer gapBuffer) {
        this.gapBuffer = gapBuffer;
    }

    @Override
    public void execute() {
        String text = gapBuffer.getHighlightedText();

        if (text.isEmpty()) {
            return;
        }

        // Copy text to system clipboard
        StringSelection selection = new StringSelection(text);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
    }

    @Override
    public boolean isUndoable() {
        return false;
    }

    @Override
    public void undo() {}
}


