package zhrfrd.jnotes.command;

import zhrfrd.jnotes.buffer.GapBuffer;
import zhrfrd.jnotes.util.Direction;

public class HighlightCommand implements Command {
    private final GapBuffer gapBuffer;
    private final Direction direction;
    private final boolean isModifierDown;

    public HighlightCommand(GapBuffer gapBuffer, int directionKeyCode, boolean isModifierDown) {
        this.gapBuffer = gapBuffer;
        this.direction = Direction.fromKeyCode(directionKeyCode);
        this.isModifierDown = isModifierDown;
    }

    @Override
    public void execute() {
        gapBuffer.highlightChar(direction, isModifierDown);
    }

    @Override
    public boolean isUndoable() {
        return false;
    }

    @Override
    public void undo() {}
}
