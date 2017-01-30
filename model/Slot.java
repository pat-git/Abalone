package abalone.model;

/**
 * This class represents a slot in the game Abalone. A Slot can be created with
 * a ball or with no ball. The ball thats in the slot can also be changed or be
 * removed.
 * 
 * @version 1.0
 */
public class Slot {

    private Ball ball;

    /**
     * Creates a slot with a ball an with a position on the board of the game
     * abalone.
     * 
     * @param ball
     *            the ball which is in this slot, if there is no ball in the
     *            slot this param should be null
     * @param position
     *            the position of the slot on the board
     */
    public Slot(Ball ball, Position position) {
        this.ball = ball;
        ball.changePosition(position);
    }

    /**
     * Creates a empty slot with a position on the board of the game abalone.
     */
    public Slot() {
    }

    /**
     * Returns a String representing the content of this slot.
     * 
     * @return a String representing the content of this slot (the color of the
     *         ball), if there is no ball in the slot it returns '.'
     */
    @Override
    public String toString() {
        if (ball == null) {
            return ".";
        } else {
            return ball.getColorToString();
        }
    }

    /**
     * Changes the ball of this slot to the ball given.
     * 
     * @param ball
     *            the ball which is now in the slot
     * @return the ball which has been overwritten
     */
    public Ball setBall(Ball ball) {
        Ball oldBall = ball;
        this.ball = ball;
        return oldBall;
    }

    /**
     * Removes the current ball of this slot.
     * 
     * @return the ball which has been removed
     */
    public Ball removeBall() {
        Ball oldBall = ball;
        ball = null;
        return oldBall;
    }

    /**
     * Returns the ball of this slot.
     * 
     * @return {@code null} if there's no ball or the ball if there's a ball in
     *         the slot
     */
    public Ball getBall() {
        return ball;
    }

    /**
     * Returns the color from the ball of this slot.
     * 
     * @return the color of the ball if there's a ball in the slot, if not it
     *         will return {@code Color.NONE}
     */
    public Color getColor() {
        if (ball != null) {
            return ball.getColor();
        } else {
            return Color.NONE;
        }
    }
}
