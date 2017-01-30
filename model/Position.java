package abalone.model;

/**
 * 
 * The class Position represents a position on a board of the game abalone.
 *
 * @version 1.0
 */
public class Position {

    private int row;
    private int diagonal;

    /**
     * Creates a position with row and diagonal coordinates
     * 
     * @param row
     *            the row of the position
     * @param diagonal
     *            the diagonal coordinates of the position
     */
    public Position(int row, int diagonal) {
        this.row = row;
        this.diagonal = diagonal;
    }

    /**
     * Getter for row coordinates.
     * 
     * @return row coordinates of this position
     */
    public int getRow() {
        return row;
    }

    /**
     * Getter for diagonal coordinates.
     * 
     * @return diagonal coordinates of this position
     */
    public int getDiag() {
        return diagonal;
    }

    /**
     * Returns a string representing this position in the format
     * "{@code row, diagonal}"
     * 
     * @return a String representing this position
     */
    @Override
    public String toString() {
        return row + ", " + diagonal;
    }
}
