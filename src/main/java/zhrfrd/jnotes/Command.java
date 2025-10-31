package zhrfrd.jnotes;

/**
 * Represent an action in the text editor. Each command should be able to execute and action and undo it.
 */
public interface Command {
    /**
     * This method defines the primary effect of the command.
     */
    void execute();

    /**
     * Reverts the action previously performed by this command.
     */
    void undo();
}




