package zhrfrd.jnotes;

public class DeleteCharCommand implements Command {
    private final GapBuffer gapBuffer;

    public DeleteCharCommand(GapBuffer gapBuffer) {
        this.gapBuffer = gapBuffer;
    }

    @Override
    public void execute() {
        Character c = gapBuffer.getCharBeforeCursor();

        if (c == null) {
            return;
        }

        gapBuffer.delete();
    }
}



