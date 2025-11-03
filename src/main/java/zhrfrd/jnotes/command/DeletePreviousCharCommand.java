package zhrfrd.jnotes.command;

import zhrfrd.jnotes.util.Direction;
import zhrfrd.jnotes.buffer.GapBuffer;

public class DeletePreviousCharCommand implements Command {
    private final GapBuffer GAP_BUFFER;
    private Character deletedCharacter;

    public DeletePreviousCharCommand(GapBuffer gapBuffer) {
        this.GAP_BUFFER = gapBuffer;
    }

    @Override
    public void execute() {
        if (GAP_BUFFER.getCharBeforeCursor() == '\0') {
            return;
        }
        deletedCharacter = GAP_BUFFER.getCharBeforeCursor();
        GAP_BUFFER.deleteChar(Direction.LEFT);
    }

    @Override
    public void undo() {
        if (deletedCharacter != null) {
            GAP_BUFFER.insert(deletedCharacter);
        }
    }
}




