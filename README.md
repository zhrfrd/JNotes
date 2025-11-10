# JNotes
JNotes is a simple text editor I'm developing to explore the challenges that arise when building something seemingly simple, 
like a text editor. The inspiration for this project came from Austin Z. Henley's article,
[Challenging projects every programmer should try](https://austinhenley.com/blog/challengingprojects.html), which highlights various data structures I might use and other
interesting key concepts.

## Requirements

- Java JDK 22 or higher.
- Maven 3.6.0 or higher (for building and running tests).
- JUnit 5 5.10.0 (for unit testing).

## Clone the repository

```bash
git clone https://github.com/zhrfrd/JNotes
```

## Build the project

If you have Maven installed:
```bash
mvn clean install
```

## Running Tests
This project uses **JUnit 5** for unit testing.

### Maven
Run all JUnit tests under `src/text/java`:
```bash
mvn test
```
Run a specific test class:
```bash
mvn -Dtest=GapBufferTest test
```

### Gradle
Run all JUnit tests under `src/text/java`:
```bash
./gradlew test
```
Run a specific test class:
```bash
./gradlew test --tests "zhrfrd.jnotes.GapBufferTest"
```


## Approach and design choices
The first naive approach I've used to handle the logic for sequential characters insertion, deletion, cursor movement
and adding new lines was to use an **ArrayList**. However, it wasn't efficient for character insertion and deletion as, 
in the worst case scenario, the operation would take O(n) time complexity. 

The final approach that I've decided to use is to implement a [GapBuffer](https://en.wikipedia.org/wiki/Gap_buffer) data 
structure which reduces the time complexity of insertion and deletion to O(1). This data structure however can be quite 
"expensive" for the cursor movement.

To manage user operations such as character insertion and deletion in a structured manner, I have used 
the [Command design pattern](https://refactoring.guru/design-patterns/command) which enforces the *Single Responsibility Principle*
and *Open/Closed Principle*. 

## TODO
- [ ] Save file.
- [ ] Open file.
- [ ] Move cursor with the mouse.
  - [ ] Reset gap position.
- [x] Start a new line when pressing Enter.
- [x] Implement a custom "text area" without relying on `JTextArea` from the Swing library or similar components.
- [x] Change font from "proportional font" to "monospace font";
- [x] Implement Undo.
- [x] Implement Redo.
- [x] Add support for deleting characters:
  - [x] Delete characters before the cursor.
  - [x] Delete characters after the cursor.
- [x] Add text cursor with its behaviours.
  - [x] Move cursor left with keystrokes.
      - [x] Reset gap position.
  - [x] Move cursor right with keystrokes.
      - [x] Reset gap position.
  - [x] Move cursor up following the correct "columns" on the y-axis with keystrokes.
      - [x] Reset gap position.
  - [x] Move cursor down following the correct "columns" on the y-axis with keystrokes.
      - [x] Reset gap position.
- [x] Highlight text.
    - [x] Highlight to the left.
    - [x] Highlight to the right.
    - [x] Highlight up.
    - [x] Highlight down.
    - [x] Show highlight in the text.
- [x] Copy and paste characters allowing full integration with the system clipboard. (use `Toolkit.getDefaultToolkit().getSystemClipboard()`).
- [ ] Fix Undo and Redo for Copy and Pasting.

## Improvements
- [ ] Adjust text highlight height.
- [ ] Improve the loop that iterates through each line in `paintComponent()`. At the moment the loop runs at each timer tick and this increases with the number of new lines.
    Moreover, it runs each time you type on the editor decreasing obviously it's performance.
- [ ] There is a glitch happening when maximizing and minimizing the editor.
- [ ] When deleting text after resizing the buffer, re-shrink the buffer in order to not waste too much memory.
- [x] Don't show the gap empty spaces in the text.
    - [x] Fix `getText(int end)`.

## Notes
Currently, JNotes does not support supplementary characters.

## Resources
- [Challenging projects every programmer should try](https://austinhenley.com/blog/challengingprojects.html).
- [Gap buffer](https://en.wikipedia.org/wiki/Gap_buffer).
- [Gap buffer data structure](https://www.geeksforgeeks.org/dsa/gap-buffer-data-structure/).