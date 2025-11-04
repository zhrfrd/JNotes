package zhrfrd.jnotes.command;

import zhrfrd.jnotes.util.Direction;
import zhrfrd.jnotes.buffer.GapBuffer;

public class DeleteNextCharCommand implements Command {
    private final GapBuffer GAP_BUFFER;
    private Character deletedCharacter;

    public DeleteNextCharCommand(GapBuffer gapBuffer) {
        GAP_BUFFER = gapBuffer;
    }

    @Override
    public void execute() {
        if (GAP_BUFFER.getCharAfterCursor() == '\0') {
            return;
        }
        deletedCharacter = GAP_BUFFER.getCharAfterCursor();
        GAP_BUFFER.deleteChar(Direction.RIGHT);
    }

    @Override
    public void undo() {
        if (deletedCharacter != null) {
            GAP_BUFFER.insert(deletedCharacter);
        }
    }
}
