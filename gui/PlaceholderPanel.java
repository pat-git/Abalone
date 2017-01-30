package abalone.gui;

import javax.swing.JPanel;

/**
 * The PlaceholderPanel is a JPanel which contains nothing and is not opaque.
 * 
 * @version 1.0
 */
@SuppressWarnings("serial")
public class PlaceholderPanel extends JPanel {

    /**
     * Creates a PlaceholerPanel and sets it to be not opaque.
     */
    public PlaceholderPanel() {
        setOpaque(false);
    }
}
