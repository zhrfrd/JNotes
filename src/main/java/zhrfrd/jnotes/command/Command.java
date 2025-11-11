package zhrfrd.jnotes.command;

/**
 * Represent an action in the text editor. Each command should be able to execute and action and undo it.
 */
public interface Command {
    /**
     * This method defines the primary effect of the command.
     */
    void execute();

    /**
     * Indicates whether this command supports undoing its action.
     * @return {@code true} if the command can be undone, {@code false} otherwise.
     */
    boolean isUndoable();

    /**
     * Reverts the action previously performed by this command.
     */
    void undo();
}




