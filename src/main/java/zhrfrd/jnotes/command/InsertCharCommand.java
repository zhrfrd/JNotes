package zhrfrd.jnotes.command;

import zhrfrd.jnotes.util.Direction;
import zhrfrd.jnotes.buffer.GapBuffer;

public class InsertCharCommand implements Command {
    private final GapBuffer gapBuffer;
    private final char character;
    private int positionAtInsert;

    public InsertCharCommand(GapBuffer gapBuffer, char character) {
        this.gapBuffer = gapBuffer;
        this.character = character;
    }

    @Override
    public void execute() {
        positionAtInsert = gapBuffer.getGapStart();
        gapBuffer.insert(character);
    }

    @Override
    public boolean isUndoable() {
        return true;
    }

    @Override
    public void undo() {
        gapBuffer.setCursorPosition(positionAtInsert + 1);   // Move cursor back to where the insertion happened
        gapBuffer.deleteChar(Direction.LEFT);
    }
}