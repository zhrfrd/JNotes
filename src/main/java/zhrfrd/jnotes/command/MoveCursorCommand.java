package zhrfrd.jnotes.command;

import zhrfrd.jnotes.buffer.GapBuffer;
import zhrfrd.jnotes.util.Direction;

public class MoveCursorCommand implements Command {
    private final GapBuffer gapBuffer;
    private final Direction direction;

    public MoveCursorCommand(GapBuffer gapBuffer, int keyCode) {
        this.gapBuffer = gapBuffer;
        direction = Direction.fromKeyCode(keyCode);
    }

    @Override
    public void execute() {
        gapBuffer.moveCursor(direction);
    }

    @Override
    public boolean isUndoable() {
        return false;
    }

    @Override
    public void undo() {}
}

