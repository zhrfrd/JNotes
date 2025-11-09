package zhrfrd.jnotes.command;

import zhrfrd.jnotes.buffer.GapBuffer;
import zhrfrd.jnotes.util.Direction;

public class HighlightCommand implements Command {
    private final GapBuffer gapBuffer;
    private final Direction direction;

    public HighlightCommand(GapBuffer gapBuffer, int directionKeyCode) {
        this.gapBuffer = gapBuffer;
        this.direction = Direction.fromKeyCode(directionKeyCode);
    }

    @Override
    public void execute() {
        gapBuffer.highlightChar(direction);
    }

    @Override
    public void undo() {}
}
