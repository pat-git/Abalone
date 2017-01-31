package abalone.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * The Game frame is the frame of the GUI (graphical user interface) of the game
 * Abalone.
 * 
 * @version 1.0
 */
public final class GameFrame extends JFrame {

    private JPanel menuPanel;
    private BoardPanel boardPanel;

    /**
     * The main method of the AbaloneGui. Creates a GameFrame which is a JFrame.
     * 
     * @param args
     *            the arguments/start parameters of this program which will not
     *            be used
     */
    public static void main(String[] args) {
        new GameFrame();
    }

    /**
     * Creates a new GameFrame which is a JFrame and initializes it. This
     * GameFrame visualizes the game Abalone.
     */
    private GameFrame() {
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
        
        // Add window listener to stop the machine move thread if the window is
        // closed
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent event) {
                boardPanel.stopMachineMoveThread();
            }
        });
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
