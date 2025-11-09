package zhrfrd.jnotes.command;

import zhrfrd.jnotes.util.Direction;
import zhrfrd.jnotes.buffer.GapBuffer;

public class DeletePreviousCharCommand implements Command {
    private final GapBuffer gapBuffer;
    private Character deletedCharacter;

    public DeletePreviousCharCommand(GapBuffer gapBuffer) {
        this.gapBuffer = gapBuffer;
    }

    @Override
    public void execute() {
        if (gapBuffer.getCharBeforeCursor() == '\0') {
            return;
        }
        deletedCharacter = gapBuffer.getCharBeforeCursor();
        gapBuffer.deleteChar(Direction.LEFT);
    }

    @Override
    public void undo() {
        if (deletedCharacter != null) {
            gapBuffer.insert(deletedCharacter);
        }
    }
}




