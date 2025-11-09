package zhrfrd.jnotes.command;

import zhrfrd.jnotes.util.Direction;
import zhrfrd.jnotes.buffer.GapBuffer;

public class DeleteNextCharCommand implements Command {
    private final GapBuffer gapBuffer;
    private Character deletedCharacter;

    public DeleteNextCharCommand(GapBuffer gapBuffer) {
        this.gapBuffer = gapBuffer;
    }

    @Override
    public void execute() {
        if (gapBuffer.getCharAfterCursor() == '\0') {
            return;
        }
        deletedCharacter = gapBuffer.getCharAfterCursor();
        gapBuffer.deleteChar(Direction.RIGHT);
    }

    @Override
    public void undo() {
        if (deletedCharacter != null) {
            gapBuffer.insert(deletedCharacter);
        }
    }
}
