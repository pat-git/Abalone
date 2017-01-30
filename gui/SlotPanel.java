package abalone.gui;

import java.awt.Graphics;
import abalone.model.Color;
import abalone.model.Position;

/**
 * The class SlotPanel is a SelectablePanel which represents a slot in the game
 * Abalone.
 * 
 * @version 1.0
 */
@SuppressWarnings("serial")
public class SlotPanel extends SelectablePanel {

    private Color color;
    public boolean highlighted;

    /**
     * Creates a SlotPanel with the given color and position.
     * 
     * @param color
     *            the color of the slot, can not be null
     * @param position
     *            the position of the slot
     */
    public SlotPanel(Position position) {
        super(position);
    }

    /**
     * Overrides the {@code paintComponent(Graphics graphic)} method of the
     * SelectablePanel. This class overrides this method to paint a slot
     * containing a black ball, white ball or nothing.
     * <p>
     * If the boolean flag {@code highlighted} is set it will also paint a red
     * circle on the border of the slot to represent a highlighted slot.
     */
    @Override
    protected void paintComponent(Graphics graphic) {
        int height = getHeight();
        int width = getWidth();
        super.paintComponent(graphic);
        color = ((BoardPanel) getParent()).getSlot(getPosition().getRow(),
                                                   getPosition().getDiag());
        if (color != null) {
            graphic.setColor(getCircleColor(color));
            // paint a circle with the color of the slot
            graphic.fillOval(0, 0, width - 1, height - 1);
            graphic.setColor(java.awt.Color.BLACK);
            // paint a black border around the circle to make it look better
            graphic.drawOval(0, 0, width - 1, height - 1);
        } else {
            throw new IllegalArgumentException("Color can not be null!");
        }
        if (highlighted) {
            graphic.setColor(java.awt.Color.RED);
            // paint a red border around the circle to make it look like its
            // highlighted
            graphic.drawOval(0, 0, width - 1, height - 1);
        }
    }

    /**
     * Gets the circle color of the slot.
     * 
     * @param color
     *            the color of the ball in the AbaloneBoard
     * @return a {@code java.awt.Color} which represents the color of the ball
     *         in the game Abalone
     * @throws IllegalArgumentException
     *             if the color is invalid
     */
    private java.awt.Color getCircleColor(Color color)
            throws IllegalArgumentException {
        if (color == Color.BLACK) {
            return java.awt.Color.BLACK;
        } else if (color == Color.WHITE) {
            return java.awt.Color.WHITE;
        } else if (color == Color.NONE) {
            // define new color (brown)
            return new java.awt.Color(139, 90, 43);
        } else {
            throw new IllegalArgumentException("Invalid Color");
        }
    }

    /**
     * Gets the color of this slot panel.
     * 
     * @return the color of this slot panel
     */
    public Color getColor() {
        return color;
    }

    /**
     * Changes the state of highlighted of a slot and repaints the slot.
     * 
     * @param highlighted
     *            if {@code true} the slot will be highlighted after the next
     *            {@code repaint()} else it will not be highlighted
     */
    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
        repaint();
    }
}
