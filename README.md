# JNotes
JNotes is a simple text editor I'm developing to explore the challenges that arise when building something seemingly simple, like a text editor. The inspiration for this project came from Austin Z. Henley's article, [Challenging projects every programmer should try](https://austinhenley.com/blog/challengingprojects.html), which highlights various data structures I might use and other interesting key concepts.

## TODO
- [ ] At the moment each character occupies different widths. Change it so that every character occupies the same exact width.
- [ ] Add text cursor with its behaviours.
  - [x] Move cursor left with keystrokes.
    - [x] Reset gap position.
  - [x] Move cursor right with keystrokes.
    - [x] Reset gap position.
  - [x] Move cursor up following the right "columns" on the y-axis with keystrokes.
    - [ ] Reset gap position.
  - [ ] Move cursor down following the right "columns" on the y-axis with keystrokes.
    - [ ] Reset gap position.
  - [ ] Move cursor with the mouse.
    - [ ] Reset gap position.
- [ ] Add support for deleting characters:
  - [x] Delete characters before the cursor.
  - [ ] Delete characters after the cursor.
- [x] Start a new line when pressing Enter.
- [x] Implement a custom "text area" without relying on `JTextArea` from the Swing library or similar components.

## Fixes
- [ ] Improve the loop that iterates through each line in `paintComponent()`. At the moment the loop runs at each timer tick and this increases with the number of new lines.
    Moreover, it runs each time you type on the editor decreasing obviously it's performance.
- [ ] There is a glitch happening when maximizing and minimizing the editor.
- [ ] When deleting after resizing the buffer, re-shrink the buffer in order to not waste too much memory.

## Notes
Currently, JNotes does not support supplementary characters.

## Resources
- [Challenging projects every programmer should try](https://austinhenley.com/blog/challengingprojects.html).