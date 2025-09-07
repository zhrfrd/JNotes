# JNotes
JNotes is a simple text editor I'm developing to explore the challenges that arise when building something seemingly simple, like a text editor. The inspiration for this project came from Austin Z. Henley's article, [Challenging projects every programmer should try](https://austinhenley.com/blog/challengingprojects.html), which highlights various data structures I might use and other interesting key concepts.

## TODO
- [ ] Implement a custom "text area" without relying on `JTextArea` from the Swing library or similar components.
- [ ] Add text cursor with its behaviours.
  - [x] Move cursor left and right with keystrokes.
  - [ ] Move cursor up and down following the right "line" on the y-axis with keystrokes.
  - [ ] Move cursor with the mouse.
- [ ] Add support for deleting characters:
  - [x] Delete characters before the cursor.
  - [ ] Delete characters after the cursor.
- [x] Start a new line when pressing Enter.

## Fixes
- [ ] Improve the loop that iterates through each line in `paintComponent()`. At the moment the loop runs at each timer tick and this increases with the number of new lines.
    Moreover, it runs each time you type on the editor decreasing obviously it's performance.
- [ ] There is a glitch happening when maximizing and minimizing the editor.
- [ ] When deleting after resizing the buffer, re-shrink the buffer in order to not waste too much memory.

## Notes
Currently, JNotes does not support supplementary characters.

## Resources
- [Challenging projects every programmer should try](https://austinhenley.com/blog/challengingprojects.html).