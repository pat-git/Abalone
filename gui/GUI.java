package abalone.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * The GUI (graphical user interface) of the game Abalone.
 * 
 * @version 1.0
 */
@SuppressWarnings("serial")
public class GUI extends JFrame {

    private JPanel menuPanel;
    private JPanel boardPanel;

    /**
     * The main method of the AbaloneGui. Creates a GUI which is a JFrame.
     * 
     * @param args
     *            the arguments/start parameters of this program which will not
     *            be used
     */
    public static void main(String[] args) {
        new GUI();
    }

    /**
     * Creates a new GUI which is a JFrame and initializes it. This GUI
     * visualizes the game Abalone.
     */
    private GUI() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        setTitle("Abalone");
        setSize(getScreenSize().width / 2, getScreenSize().height / 2);
        setLocation(getScreenSize().width / 4, getScreenSize().height / 4);
        setLayout(new BorderLayout());
        menuPanel = new MenuPanel(new BorderLayout());
        getContentPane().add(menuPanel, BorderLayout.SOUTH);
        boardPanel = new BoardPanel(this);
        getContentPane().add(boardPanel, BorderLayout.CENTER);
    }

    /**
     * Returns the screensize (amount of pixels) of the main monitor.
     * 
     * @return the screensize as a {@code Dimension}
     */
    private Dimension getScreenSize() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }

    /**
     * Gets the panel which contains the menu of abalone.
     * 
     * @return the menu-panel
     */
    public MenuPanel getMenuPanel() {
        return (MenuPanel) menuPanel;
    }
}
