package zhrfrd.jnotes.command;

import zhrfrd.jnotes.buffer.GapBuffer;

public class PasteCommand implements Command {
    private final GapBuffer gapBuffer;

    public PasteCommand(GapBuffer gapBuffer) {
        this.gapBuffer = gapBuffer;
    }

    @Override
    public void execute() {
    }

    @Override
    public void undo() {
    }
}


