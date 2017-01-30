package abalone.model;

/**
 * The Utility class provides two static functions that return the highest and
 * lowest diagonal coordinates of the board. This class can not be instantiated.
 * 
 * @version 1.0
 */
public final class Utility {

    private Utility() {
    }

    /**
     * Calculates the lowest diagonal coordinates in one row on the board.
     * 
     * @param row
     *            the row
     * @param size
     *            the size of the board
     * @return the lowest diagonal coordinates
     */
    public static int minDiag(int row, int size) {
        return Math.max(0, row - size / 2);
    }

    /**
     * Calculates the highest diagonal coordinates in one row on the board.
     * 
     * @param row
     *            the row
     * @param size
     *            the size of the board
     * @return the highest diagonal coordinates
     */
    public static int maxDiag(int row, int size) {
        return Math.min(row + size / 2, size - 1);
    }
}
