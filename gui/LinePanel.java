package abalone.gui;

import java.awt.Graphics;

/**
 * The LinePanel is a PlaceholderPanel which visually contains a horizontal
 * line.
 * 
 * @version 1.0
 */
@SuppressWarnings("serial")
public class LinePanel extends PlaceholderPanel {

    /**
     * Create a LinePanel.
     */
    public LinePanel() {
    }

    /**
     * Overrides the {@code paintComponent(Graphics g)} method of the JComponent
     * class. This class overrides this method to paint horizontal line.
     */
    @Override
    protected void paintComponent(Graphics graphic) {
        int height = getHeight();
        int width = getWidth();
        super.paintComponent(graphic);
        graphic.drawLine(0, height / 2, width, height / 2);
    }
}
