package zhrfrd.jnotes.command;

import java.util.ArrayDeque;
import java.util.Deque;

public class CommandManager {
    // Deque (via ArrayDeque) works exactly like a LIFO Stack.
    // but without the synchronization overhead of Stack, which would only slow down this single-threaded program.
    private final Deque<Command> undoStack = new ArrayDeque<>();
    private final Deque<Command> redoStack = new ArrayDeque<>();

    public void execute(Command command) {
        command.execute();
        undoStack.push(command);
        redoStack.clear();
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop();
            command.undo();
            redoStack.push(command);
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.execute();
            undoStack.push(command);
        }
    }
}




