package zhrfrd.jnotes.util;

import java.awt.event.KeyEvent;

/**
 * Direction to express navigation (horizontal and vertical).
 */
public enum Direction {
    LEFT,
    RIGHT,
    UP,
    DOWN;

    /**
     * Convert a KeyEvent key code to a Direction enum value.
     * @param keyCode The KeyEvent key code (VK_LEFT, VK_RIGHT, VK_UP, VK_DOWN).
     * @return The corresponding Direction enum value.
     * @throws IllegalArgumentException if the key code is not a valid direction key.
     */
    public static Direction fromKeyCode(int keyCode) {
        return switch (keyCode) {
            case KeyEvent.VK_LEFT -> LEFT;
            case KeyEvent.VK_RIGHT -> RIGHT;
            case KeyEvent.VK_UP -> UP;
            case KeyEvent.VK_DOWN -> DOWN;
            default -> throw new IllegalStateException("Unexpected value: " + keyCode);
        };
    }
}


