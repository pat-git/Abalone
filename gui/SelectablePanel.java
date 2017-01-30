package abalone.gui;

import java.awt.Graphics;
import abalone.model.Position;

/**
 * The class SelectablePanel is a LinePanel which is clickable/selectable. A
 * SelectablePanel also saves a position which represents the position of a slot
 * (valid position) on the game board of the game Abalone.
 * 
 * @version 1.0
 */
@SuppressWarnings("serial")
public class SelectablePanel extends LinePanel {

    private Position position;

    /**
     * Creates a SelectAblePanel with the given position.
     * 
     * @param position
     *            the position
     */
    public SelectablePanel(Position position) {
        this.position = position;
    }

    /**
     * Overrides the {@code paintComponent(Graphics graphic)} method of the
     * LinePanel. This class overrides this method to paint cross (two crossing
     * lines) which represent together with the line of the line panel the
     * possible moves of a ball in the game board of the game Abalone.
     */
    @Override
    protected void paintComponent(Graphics graphic) {
        int height = getHeight();
        int width = getWidth();
        super.paintComponent(graphic);
        graphic.drawLine(0, 0, width, height);
        graphic.drawLine(0, height, width, 0);
    }

    /**
     * Gets the position of this panel.
     * @return the position
     */
    protected Position getPosition() {
        return position;
    }
}
