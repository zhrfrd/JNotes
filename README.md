# JNotes
JNotes is a simple text editor I'm developing to explore the challenges that arise when building something seemingly simple, 
like a text editor. The inspiration for this project came from Austin Z. Henley's article,
[Challenging projects every programmer should try](https://austinhenley.com/blog/challengingprojects.html), which highlights various data structures I might use and other
interesting key concepts.

## Approach
The first naive approach I've used to handle the logic for sequential characters insertion, deletion, cursor movement
and adding new lines was to use an **ArrayList**. However, it wasn't efficient for character insertion and deletion as, 
in the worst case scenario, the operation would take O(n) time complexity. 

The final approach that I've decided to use is to implement a [GapBuffer](https://en.wikipedia.org/wiki/Gap_buffer) data 
structure which reduces the time complexity of insertion and deletion to O(1). This data structure however can be quite 
"expensive" for the cursor movement.

## TODO
- [ ] Add text cursor with its behaviours.
  - [x] Move cursor left with keystrokes.
    - [x] Reset gap position.
  - [x] Move cursor right with keystrokes.
    - [x] Reset gap position.
  - [x] Move cursor up following the correct "columns" on the y-axis with keystrokes.
    - [x] Reset gap position.
  - [x] Move cursor down following the correct "columns" on the y-axis with keystrokes.
    - [x] Reset gap position.
  - [ ] Move cursor with the mouse.
    - [ ] Reset gap position.
- [ ] Add support for deleting characters:
  - [x] Delete characters before the cursor.
  - [ ] Delete characters after the cursor.
- [ ] Copy and paste characters.
- [ ] Highlight text.
- [ ] Save file.
- [ ] Open file.
- [x] Start a new line when pressing Enter.
- [x] Implement a custom "text area" without relying on `JTextArea` from the Swing library or similar components.
- [x] Change font from "proportional font" to "monospace font";

## Improvements
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