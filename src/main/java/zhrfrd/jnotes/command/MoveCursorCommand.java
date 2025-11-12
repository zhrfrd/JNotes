package zhrfrd.jnotes.command;

import zhrfrd.jnotes.buffer.GapBuffer;
import zhrfrd.jnotes.util.Direction;

public class MoveCursorCommand implements Command {
    private final GapBuffer gapBuffer;
    private final Direction direction;
    private final boolean isModifierDown;

    public MoveCursorCommand(GapBuffer gapBuffer, int keyCode, boolean isModifierDown) {
        this.gapBuffer = gapBuffer;
        direction = Direction.fromKeyCode(keyCode);
        this.isModifierDown = isModifierDown;
    }

    @Override
    public void execute() {
        gapBuffer.moveCursor(direction, isModifierDown);
    }

    @Override
    public boolean isUndoable() {
        return false;
    }

    @Override
    public void undo() {}
}

