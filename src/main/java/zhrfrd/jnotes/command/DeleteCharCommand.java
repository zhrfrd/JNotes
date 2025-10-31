package zhrfrd.jnotes.command;

import zhrfrd.jnotes.buffer.GapBuffer;

public class DeleteCharCommand implements Command {
    private final GapBuffer gapBuffer;
    private Character deletedCharacter;

    public DeleteCharCommand(GapBuffer gapBuffer) {
        this.gapBuffer = gapBuffer;
    }

    @Override
    public void execute() {
        if (gapBuffer.getCharBeforeCursor() == null) {
            return;
        }
        deletedCharacter = gapBuffer.getCharBeforeCursor();
        gapBuffer.delete();
    }

    @Override
    public void undo() {
        if (deletedCharacter != null) {
            gapBuffer.insert(deletedCharacter);
        }
    }
}




