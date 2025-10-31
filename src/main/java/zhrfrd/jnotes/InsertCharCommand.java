package zhrfrd.jnotes;

public class InsertCharCommand implements Command {
    private final GapBuffer gapBuffer;
    private final char character;

    public InsertCharCommand(GapBuffer gapBuffer, char character) {
        this.gapBuffer = gapBuffer;
        this.character = character;
    }

    @Override
    public void execute() {
        gapBuffer.insert(character);
    }

    @Override
    public void undo() {
        if (gapBuffer.getCharBeforeCursor() == null) {
            return;
        }
        gapBuffer.delete();
    }
}




