package zhrfrd.jnotes.command;

import zhrfrd.jnotes.buffer.GapBuffer;
import zhrfrd.jnotes.util.Direction;

public class HighlightCommand implements Command {
    private final GapBuffer GAP_BUFFER;
    private final Direction DIRECTION;

    public HighlightCommand(GapBuffer gapBuffer, int directionKeyCode) {
        GAP_BUFFER = gapBuffer;
        DIRECTION = Direction.fromKeyCode(directionKeyCode);
    }

    @Override
    public void execute() {
        switch (DIRECTION) {
            case LEFT:
                break;
            case RIGHT:
                break;
            case UP:
                break;
            case DOWN:
                break;
        }
    }

    @Override
    public void undo() {}
}
