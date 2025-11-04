package zhrfrd.jnotes.command;

import zhrfrd.jnotes.buffer.GapBuffer;

public class MoveCursorCommand implements Command {
    private final GapBuffer GAP_BUFFER;
    private final int DIRECTION;

    public MoveCursorCommand(GapBuffer gapBuffer, int direction) {
        this.GAP_BUFFER = gapBuffer;
        this.DIRECTION = direction;
    }

    @Override
    public void execute() {
        GAP_BUFFER.moveCursor(DIRECTION);
    }

    @Override
    public void undo() {}
}

