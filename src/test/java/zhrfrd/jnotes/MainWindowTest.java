package zhrfrd.jnotes;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

class MainWindowTest {
    @Test
    @DisplayName("Check if MainWindow layout initialize correctly")
    void testInitializeLayout() {
        MainWindow mainWindow = new MainWindow();

        assertTrue(mainWindow.isVisible(), "MainWindow should be visible.");
        assertEquals(1, mainWindow.getContentPane().getComponentCount(), "Should have 1 main panel.");
        assertEquals(JFrame.EXIT_ON_CLOSE, mainWindow.getDefaultCloseOperation(), "Default close operation should be EXIT_ON_CLOSE.");
    }
}