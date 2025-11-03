package zhrfrd.jnotes.command;

import zhrfrd.jnotes.util.Direction;
import zhrfrd.jnotes.buffer.GapBuffer;

public class InsertCharCommand implements Command {
    private final GapBuffer GAP_BUFFER;
    private final char CHARACTER;

    public InsertCharCommand(GapBuffer gapBuffer, char character) {
        this.GAP_BUFFER = gapBuffer;
        this.CHARACTER = character;
    }

    @Override
    public void execute() {
        GAP_BUFFER.insert(CHARACTER);
    }

    @Override
    public void undo() {
        if (GAP_BUFFER.getCharBeforeCursor() == '\0') {
            return;
        }
        GAP_BUFFER.deleteChar(Direction.LEFT);
    }
}