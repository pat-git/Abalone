package abalone.model;

/**
 * The class Ball represents a ball on a board in the game abalone. The ball can
 * change his position and the ball can be printed out.
 * 
 * @version 1.0
 */
public class Ball {

    private Color color;
    private Position position;

    /**
     * Creates a Ball with the color given.
     * 
     * @param color
     *            The color of the ball
     */
    public Ball(Color color) {
        this.color = color;
    }

    /**
     * Gets the color of this ball.
     * 
     * @return the color of this ball
     */
    public Color getColor() {
        return color;
    }

    /**
     * Gets the position of this ball.
     * 
     * @return the position of this ball
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Changes the position of the ball.
     * 
     * @param position
     *            the new position of the ball
     */
    public void changePosition(Position position) {
        if (this.position == null || !this.position.equals(position)) {
            this.position = position;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Returns a String representing the color of the ball.
     * 
     * @return the String {@code "X"} if the color of the ball is black and
     *         {@code "O"} if the color is white
     */
    public String getColorToString() {
        if (color.equals(Color.BLACK)) {
            return "X";
        } else {
            return "O";
        }
    }

    /**
     * Returns a String representing the ball.
     * 
     * @return a String which contains the position and the color of the ball
     */
    public String toString() {
        return "(" + position.toString() + ", " + getColorToString() + ")";
    }
}
