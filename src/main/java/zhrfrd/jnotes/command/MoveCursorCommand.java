package zhrfrd.jnotes.command;

import zhrfrd.jnotes.buffer.GapBuffer;
import zhrfrd.jnotes.util.Direction;

public class MoveCursorCommand implements Command {
    private final GapBuffer GAP_BUFFER;
    private final Direction DIRECTION;

    public MoveCursorCommand(GapBuffer gapBuffer, int keyCode) {
        GAP_BUFFER = gapBuffer;
        DIRECTION = Direction.fromKeyCode(keyCode);
    }

    @Override
    public void execute() {
        GAP_BUFFER.moveCursor(DIRECTION);
    }

    @Override
    public void undo() {}
}

