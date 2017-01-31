package abalone.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * The class AbaloneBoard controls all actions of the board of the game it
 * implements the interface Board and Cloneable. The AbaloneBoard can also be
 * cloned.
 * <p>
 * Game information:
 * <p>
 * A human plays against the machine. The human's ground lines are always the
 * lowest rows, whereas the ground lines of the machine are the highest rows.
 * The human plays from bottom to top, the machine from top to bottom. The user
 * with the black balls opens the game. Winner is who first pushes
 * out/eliminates six of the opponent's balls.
 * 
 * <p>
 * There are some differences to traditional Abalone:
 * <ul>
 * <li>In case that one player has no option to make a valid move, he must miss
 * a turn. The other player can make a move in any case.
 * <li>A move can involve more than three (own) balls.
 * <li>Sideward moves, i.e., changing the diagonal of more than one own ball not
 * in the same row, are not allowed.
 * <li>The game may never end.
 * </ul>
 * 
 * @version 1.0
 */
public class AbaloneBoard implements Board, Cloneable {

    private int boardSize;
    private boolean machineIsBeginner = false;
    private Player lastPlayer;
    private Color humanColor;
    private Color machineColor;
    private int level;
    private Slot[][] board;
    private final int ballAmount;
    private List<Ball> playerBallList = new ArrayList<Ball>();
    private List<Ball> machineBallList = new ArrayList<Ball>();

    /**
     * Creates a AbaloneBoard with the default boardsize (=9). The board will be
     * filled with the default constellation of balls.
     */
    public AbaloneBoard() {
        this(9, false);
    }

    /**
     * Creates a AbaloneBoard with the boardSize given. The board will be filled
     * with the default constellation of balls and with the default settings
     * (level, ...).
     * 
     * @param boardSize
     *            the boardSize of the board, must be odd and greater than 5
     * @param machineIsBeginner
     *            if the machine is beginner this value must be {@code true}
     * @throws IllegalArgumentException
     *             if the boardSize is invalid
     */
    public AbaloneBoard(int boardSize, boolean machineIsBeginner) {
        if (boardSize >= MIN_SIZE && boardSize % 2 != 0) {
            this.boardSize = boardSize;
        } else {
            throw new IllegalArgumentException("Invalid boardsize");
        }
        board = new Slot[boardSize][boardSize];
        this.machineIsBeginner = machineIsBeginner;

        // Change to the right color if the beginner has been changed to machine
        if (!machineIsBeginner) {
            humanColor = Color.BLACK;
            machineColor = Color.WHITE;
        } else {
            lastPlayer = Player.HUMAN;
            humanColor = Color.WHITE;
            machineColor = Color.BLACK;
        }
        level = 2;
        initializeBoard();
        ballAmount = playerBallList.size();
    }

    /**
     * Initializes the board of the game. Fills the board with the default
     * constellation of balls.
     */
    private void initializeBoard() {
        for (int row = 0; row < boardSize; row++) {
            addEmptySlotsToRow(row);
        }
        for (int row = 0; row < 2; row++) {
            fillRowWithBalls(row, Player.HUMAN, false);
        }
        fillRowWithBalls(2, Player.HUMAN, true);
        /*
         * Fill machine balls in a different order because of the reverse order
         * of the balls in the machineBallList
         */
        fillRowWithBalls(boardSize - 3, Player.MACHINE, true);
        for (int row = boardSize - 2; row < boardSize; row++) {
            fillRowWithBalls(row, Player.MACHINE, false);
        }
        Collections.reverse(machineBallList);
    }

    /**
     * Fills one row with balls using the default constellation of a abalone
     * game board.
     * <p>
     * To fill row two and third last row with the correct amount of balls
     * {@code skipOuterSlots} must be set to true.
     * 
     * @param row
     *            the row
     * @param player
     *            the player who owns the balls
     * @param skipOuterSlots
     *            skips the two slots on each side (the two lowest and highest
     *            diagonal coordinates in the row)
     * @throws IllegalArgumentException
     *             if the row is invalid
     */
    private void fillRowWithBalls(int row, Player player,
                                  boolean skipOuterSlots) {
        if (row > boardSize - 1 || row < 0) {
            throw new IllegalArgumentException("Invalid row!");
        }
        int skippedSlots = 0;
        int lastDiag = 0;
        for (int diag = 0; diag < boardSize; diag++) {
            if (board[row][diag] != null) {
                if (skipOuterSlots && skippedSlots < 2) {
                    skippedSlots++;
                } else {
                    Ball newBall;
                    if (player.equals(Player.HUMAN)) {
                        newBall = new Ball(humanColor);
                        newBall.changePosition(new Position(row, diag));
                        playerBallList.add(newBall);
                    } else {
                        newBall = new Ball(machineColor);
                        newBall.changePosition(new Position(row, diag));
                        machineBallList.add(newBall);
                    }
                    board[row][diag].setBall(newBall);
                    lastDiag = diag;
                }
            }
        }
        if (skipOuterSlots) {
            // Removes last two balls in this row and in the list
            board[row][lastDiag].removeBall();
            board[row][lastDiag - 1].removeBall();
            if (player.equals(Player.HUMAN)) {
                playerBallList.remove(playerBallList.size() - 1);
                playerBallList.remove(playerBallList.size() - 1);
            } else {
                machineBallList.remove(machineBallList.size() - 1);
                machineBallList.remove(machineBallList.size() - 1);
            }
        }
    }

    /**
     * Adds the default constellation of empty Slots to the board
     * 
     * @param row
     *            the row which should be filled
     * @throws IllegalArgumentException
     *             if row is invalid
     */
    private void addEmptySlotsToRow(int row) {
        if (row > boardSize - 1 || row < 0) {
            throw new IllegalArgumentException("Invalid row!");
        }
        int middleRowIndex = (boardSize / 2);
        if (row <= middleRowIndex) {
            int toDiag = boardSize - (middleRowIndex - row);
            int fromDiag = 0;
            fillRowWithSlots(row, fromDiag, toDiag);
        } else if (row > middleRowIndex) {
            int fromDiag = row - middleRowIndex;
            int toDiag = boardSize;
            fillRowWithSlots(row, fromDiag, toDiag);
        }
    }

    /**
     * Fills a row with Slots starting from {@code fromDiag} to {@code toDiag}
     * 
     * @param row
     *            the row which should be filled
     * @param fromDiag
     *            the diag coordinates from which is will start to fill
     * @param toDiag
     *            the last diag coordinate which will be filled with slots
     */
    private void fillRowWithSlots(int row, int fromDiag, int toDiag) {
        int counter = fromDiag;
        while (counter < toDiag) {
            if (board[row][counter] == null) {
                board[row][counter] = new Slot();
            }
            counter++;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Player getOpeningPlayer() {
        if (machineIsBeginner) {
            return Player.MACHINE;
        } else {
            return Player.HUMAN;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Color getHumanColor() {
        return humanColor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Player getNextPlayer() {
        if (lastPlayer == null) {
            return getOpeningPlayer();
        } else if (lastPlayer == Player.HUMAN) {
            if (!canMove(Player.MACHINE)) {
                return Player.HUMAN;
            } else {
                return Player.MACHINE;
            }
        } else {
            if (!canMove(Player.HUMAN)) {
                return Player.MACHINE;
            } else {
                return Player.HUMAN;
            }
        }
    }

    private void setNextPlayer() {
        lastPlayer = this.getNextPlayer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValidPosition(int row, int diag) { 
        // The board[row][diag] is only null if there's no slot at this position
        // that means that the given position is out of the game board and that
        // means that the given position is invalid
        return isInsideArray(row, diag) && board[row][diag] != null;
    }

    /**
     * Checks if the given position is in the board Array.
     * 
     * @param row
     *            the row coordinates of the position
     * @param diag
     *            the diagonal coordinates of the position
     * @return {@code true} if the position is in the array and {@code false} if
     *         the position is not in the array
     */
    private boolean isInsideArray(int row, int diag) {
        return row >= 0 && diag >= 0 && row < boardSize && diag < boardSize;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValidTarget(int row, int diag) {
        return isValidPosition(row, diag) || isOnEdge(row, diag);
    }

    /**
     * Checks if the given position is on the edge of the board.
     * 
     * @param row
     *            the row coordinates of the position
     * @param diag
     *            the diagonal coordinates of the position
     * @return {@code true} if the position is on the edge, {@code false}
     *         otherwise
     */
    private boolean isOnEdge(int row, int diag) {
        return calculateDistanceToEdge(row, diag) == -1;
    }

    /**
     * Calculates the distance of one position to the edge.
     * 
     * @param row
     *            the row coordinates of the position
     * @param diag
     *            the diagonal coordinates of the position
     * @return the distance of the position to the edge
     */
    private int calculateDistanceToEdge(int row, int diag) {
        double otherDiag = row - diag + (boardSize / 2);
        double minRow = Math.min(row, boardSize - row - 1);
        double minDiag = Math.min(diag, boardSize - diag - 1);
        double minOtherDiag = Math.min(otherDiag, boardSize - otherDiag - 1);
        return (int) Math.min(minRow, Math.min(minDiag, minOtherDiag));
    }

    /**
     * Checks if the player can perform a valid move. Moving one ball directly
     * to the edge is also a valid move
     * 
     * @param player
     *            the player
     * @return {@code true} if the player can make a valid move and
     *         {@code false} if he can not make a valid move
     */
    private boolean canMove(Player player) {
        List<Ball> ballList = null;
        if (player == Player.HUMAN) {
            ballList = playerBallList;
        } else {
            ballList = machineBallList;
        }
        for (int i = 0; i < ballList.size(); i++) {
            if (ballList.get(i) != null) {
                int row = ballList.get(i).getPosition().getRow();
                int diag = ballList.get(i).getPosition().getDiag();
                
                // Directly returning true because of better performance
                if (checkMove(row, diag, row + 1, diag)) {
                    return true;
                } else if (checkMove(row, diag, row, diag + 1)) {
                    return true;
                } else if (checkMove(row, diag, row + 1, diag + 1)) {
                    return true;
                } else if (checkMove(row, diag, row - 1, diag)) {
                    return true;
                } else if (checkMove(row, diag, row, diag - 1)) {
                    return true;
                } else if (checkMove(row, diag, row - 1, diag - 1)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if the move with the given positions is valid.
     * 
     * @param rowFrom
     *            the row coordinates of the first ball
     * @param diagFrom
     *            the diagonal coordinates of the first ball
     * @param rowTo
     *            the row coordinates of the target position
     * @param diagTo
     *            the row coordinates of the target position
     * @return {@code true} if the move is valid, {@code false} if the move is
     *         invalid
     */
    private boolean checkMove(int rowFrom, int diagFrom, int rowTo,
                                                         int diagTo) {
        if (board != null && isValidPosition(rowFrom, diagFrom)
                          && isValidTarget(rowTo, diagTo)) {
            // calculate distances to detect a invalid move
            int distanceRow = Math.abs(rowFrom - rowTo);
            int distanceRowDiag = Math.abs((rowFrom - rowTo) 
                                           - (diagFrom - diagTo));
            int distanceDiag = Math.abs(diagFrom - diagTo);
            if (distanceRow + distanceDiag < 3 && distanceRow + distanceDiag > 0
                                               && distanceRowDiag < 2) {
                if (isOnEdge(rowTo, diagTo)) {
                    return true;
                } else if (board[rowTo][diagTo].getBall() == null) {
                    return true;
                } else {
                    // because there are more balls in this direction the move
                    // must be checked if its legal
                    return sumito(rowFrom, diagFrom, rowTo, diagTo) != null;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Board move(int rowFrom, int diagFrom, int rowTo, int diagTo) {
        if (board != null && isValidPosition(rowFrom, diagFrom)
                          && isValidTarget(rowTo, diagTo)) {
            // calculate distances to detect a invalid move
            int distanceRow = Math.abs(rowFrom - rowTo);
            int distanceRowDiag = Math.abs((rowFrom - rowTo) 
                                           - (diagFrom - diagTo));
            int distanceDiag = Math.abs(diagFrom - diagTo);
            if (distanceRow + distanceDiag < 3 && distanceRow + distanceDiag > 0
                                               && distanceRowDiag < 2) {
                AbaloneBoard result = (AbaloneBoard) this.clone();
                if (isOnEdge(rowTo, diagTo)) {
                    // ball should be moved to the outside -> just delete it
                    Ball removedBall = result.board[rowFrom][diagFrom]
                                                                  .removeBall();
                    if (getNextPlayer() == Player.HUMAN) {
                        result.playerBallList.remove(removedBall);
                    } else {
                        result.machineBallList.remove(removedBall);
                    }
                } else if (board[rowTo][diagTo].getBall() == null) {
                    // next slot is empty -> fill the slot with the ball
                    result.board[rowTo][diagTo].setBall(
                                     result.board[rowFrom][diagFrom].getBall());
                    result.board[rowFrom][diagFrom].removeBall();
                    result.board[rowTo][diagTo].getBall().changePosition(
                                                   new Position(rowTo, diagTo));
                } else {
                    // because there are more balls in this direction the move
                    // must be checked if its legal
                    int[] lastCoords = sumito(rowFrom, diagFrom, rowTo, diagTo);
                    if (lastCoords != null) {
                        return moveLineOfBalls(rowFrom, diagFrom, 
                                               rowTo - rowFrom,
                                               diagTo - diagFrom, 
                                               lastCoords[0], lastCoords[1]);
                    } else {
                        return null;
                    }
                }
                result.setNextPlayer();
                return result;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Checks if the amount of balls in one line (=all balls which are located
     * in same direction where the current ball will move without any empty slot
     * in between) on the board of the current player is higher or equal than
     * the amount of the other balls in one line on the board. If the amount is
     * higher or equal the method performs a move of all balls which are in the
     * same line.
     * 
     * @param rowFrom
     *            the row coordinates of the ball before the move
     * @param diagFrom
     *            the diagonal coordinates of the ball before the move
     * @param rowTo
     *            the row coordinates of the ball after the move
     * @param diagTo
     *            the diagonal coordinates of the ball after the move
     * @return a duplicate of the board with the performed move of all balls or
     *         {@code null} if the move was illegal
     */
    private int[] sumito(int rowFrom, int diagFrom, int rowTo, int diagTo) {
        Ball ballToMove = board[rowFrom][diagFrom].getBall();
        int vectorRow = rowTo - rowFrom;
        int vectorDiag = diagTo - diagFrom;
        int nextRow = rowFrom;
        int nextDiag = diagFrom;
        int friendlyBallCounter = 0;
        int enemyBallCounter = 0;
        int[] lastCoords = new int[2];
        boolean illegalMove = false;
        // lookup all balls in one direction and count enemy balls and balls of
        // the player who is performing this move (friendly balls)
        while (isInsideArray(nextRow, nextDiag) && !illegalMove) {
            Slot nextSlot = board[nextRow][nextDiag];
            if (nextSlot != null) {
                if (nextSlot.getBall() != null) {
                    if (nextSlot.getBall().getColor() 
                        == ballToMove.getColor()) {
                        if (enemyBallCounter > 0) {
                            illegalMove = true;
                        } else {
                            friendlyBallCounter++;
                        }
                    } else {
                        enemyBallCounter++;
                    }
                } else {
                    break;
                }
            } else {
                break;
            }
            nextRow += vectorRow;
            nextDiag += vectorDiag;
        }
        if (!illegalMove && friendlyBallCounter > enemyBallCounter) {
            lastCoords[0] = nextRow;
            lastCoords[1] = nextDiag;
            return lastCoords;
        } else {
            return null;
        }
    }

    /**
     * Moves all balls in one "line" (=all balls which are located in same
     * direction where the current ball will move without any empty slot in
     * between).
     * 
     * @param rowFrom
     *            the row coordinates of the ball before the move
     * @param diagFrom
     *            the diagonal coordinates of the ball before the move
     * @param vectorRow
     *            the distance of the row coordinates from one ball to the next
     *            ball
     * @param vectorDiag
     *            the distance of the diagonal coordinates from one ball to the
     *            next ball
     * @param lastRow
     *            the row coordinates of the last ball in the line
     * @param lastDiag
     *            the diagonal coordinates of the last ball in the line
     * @return a duplicate of the board with the performed move of all balls
     * @throws IllegalArgumentException
     *             if the coordinates are invalid
     */
    private Board moveLineOfBalls(int rowFrom, int diagFrom, int vectorRow,
                                  int vectorDiag, int lastRow, int lastDiag) {
        AbaloneBoard result = (AbaloneBoard) this.clone();
        
        // The ball that has been overwritten by the balls which are moving in
        // the direction of the ball which executed the move
        int currentRow = lastRow;
        int currentDiag = lastDiag;
        int beforeRow = currentRow - vectorRow;
        int beforeDiag = currentDiag - vectorDiag;
        if (!isInsideArray(beforeRow, beforeDiag) 
             || isOnEdge(beforeRow, beforeDiag)) {
            throw new IllegalArgumentException("Invalid coordinates!");
        }
        if (!isInsideArray(currentRow, currentDiag) 
             || isOnEdge(currentRow, currentDiag)) {
            // Delete Ball, ball will be deleted on the board because it gets
            // replaced by the one which is pushing it outside
            Slot slotBefore = result.board[beforeRow][beforeDiag];
            if (result.getHumanColor() == slotBefore.getColor()) {
                result.playerBallList.remove(slotBefore.getBall());
            } else {
                result.machineBallList.remove(slotBefore.getBall());
            }
            currentRow = beforeRow;
            currentDiag = beforeDiag;
            beforeRow -= vectorRow;
            beforeDiag -= vectorDiag;
        }
        while (currentRow != rowFrom || currentDiag != diagFrom) {
            Slot slotBefore = result.board[beforeRow][beforeDiag];
            Slot currentSlot = result.board[currentRow][currentDiag];
            currentSlot.setBall(slotBefore.getBall());
            slotBefore.removeBall();
            currentSlot.getBall().changePosition(new Position(currentRow,
                                                              currentDiag));
            currentRow = beforeRow;
            currentDiag = beforeDiag;
            beforeRow -= vectorRow;
            beforeDiag -= vectorDiag;
        }
        result.setNextPlayer();
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Board machineMove() {
        return calculateBestMove(setupTree()).getBoard();
    }

    /**
     * Calculates the best move for the machine. The higher the value of the
     * board of the node is the better the move for the machine.
     * 
     * @param root
     *            the root node of the tree
     * @return the node with the best board
     */
    private Node calculateBestMove(Node root) {
        Node bestNode = root.getChildren().get(0);
        double bestValue = calculateGameSituation(bestNode);
        // Pick the best node of all children
        for (Node node : root.getChildren()) {
            double newValue = calculateGameSituation(node);
            if (newValue > bestValue) {
                bestNode = node;
                bestValue = newValue;
            }
        }
        return bestNode;
    }

    /**
     * Calculates the game situation of a node with a board. If the node has no
     * children the return value is directly calculate through the method
     * {@code calculateGameBoardSituation(AbaloneBoard board)}. If the node has
     * children it will pick the min or max value of all children depending on
     * which player made the last move.
     * 
     * @param node
     *            the node with a board
     * @return the value of the node (the value of the board which is saved in
     *         the node)
     */
    private double calculateGameSituation(Node node) {
        if (node.hasChildren()) {
            Player player = node.getBoard().getNextPlayer();
            double minMaxOfChildren = calculateGameSituation(
                                                     node.getChildren().get(0));
            if (player != Player.HUMAN) {
                for (Node child : node.getChildren()) {
                    double currentValue = calculateGameSituation(child);
                    if (currentValue < minMaxOfChildren) {
                        minMaxOfChildren = currentValue;
                    }
                }
            } else {
                for (Node child : node.getChildren()) {
                    double currentValue = calculateGameSituation(child);
                    if (currentValue > minMaxOfChildren) {
                        minMaxOfChildren = currentValue;
                    }
                }
            }

            return calculateGameBoardSituation((AbaloneBoard) node.getBoard())
                   + minMaxOfChildren;
        } else {
            return calculateGameBoardSituation((AbaloneBoard) node.getBoard());
        }
    }

    /**
     * Calculates the current situation of the given board and returns a value.
     * The value depends on how much balls the players (more balls are better)
     * have, how good the balls are placed on the field (Near to the edge is
     * worse) and if one player is winning in a few rounds (this value dominates
     * all others).
     * 
     * @param board
     *            the board from which the value is calculated
     * @return the value of the board
     */
    private double calculateGameBoardSituation(AbaloneBoard board) {
        double distVal = board.calculateBallDistancesToEdgeValue();
        double winVal = 0;
        double ballAmountVal = board.machineBallList.size() 
                               - 1.5 * board.playerBallList.size();
        if (board.isGameOver()) {
            if (Player.HUMAN == board.getWinner()) {
                winVal = -1.5 * 50000000.0 / (double) level;
            } else {
                winVal = 50000000.0 / (double) level;
            }
        }
        return boardSize * ballAmountVal + distVal + winVal;
    }

    /**
     * Calculates all distances of all balls that are on the board and returns
     * the distance value that is later used in the
     * {@code calculateGameBoardSituation(AbaloneBoard board)} method.
     * 
     * @return the distance value
     */
    private double calculateBallDistancesToEdgeValue() {
        int[] playerBallDistances = new int[(boardSize / 2) + 1];
        int[] machineBallDistances = new int[(boardSize / 2) + 1];
        for (Ball ball : playerBallList) {
            int index = calculateDistanceToEdge(ball.getPosition().getRow(),
                                                ball.getPosition().getDiag());
            playerBallDistances[index]++;
        }
        for (Ball ball : machineBallList) {
            int index = calculateDistanceToEdge(ball.getPosition().getRow(),
                                                ball.getPosition().getDiag());
            machineBallDistances[index]++;
        }
        double playerResult = 0;
        double machineResult = 0;
        for (int i = 0; i < playerBallDistances.length; i++) {
            playerResult += playerBallDistances[i] * i;
            machineResult += machineBallDistances[i] * i;
        }
        return machineResult - 1.5 * playerResult;
    }

    private Node setupTree() {
        Node root = new Node(this);
        setChildren(root, level);
        return root;
    }

    /**
     * Sets the children of a node by getting all possible boards of this board.
     * 
     * @param node
     *            the root node
     * @param level
     *            the level of the game
     */
    private void setChildren(Node node, int level) {
        List<Board> possibleBoards = getAllPossibleBoards((AbaloneBoard) node
                                  .getBoard(), node.getBoard().getNextPlayer());
        for (Board board : possibleBoards) {
            Node child = new Node(board);
            node.addChild(child);
        }
        level--;
        if (level > 0) {
            for (Node child : node.getChildren()) {
                setChildren(child, level);
            }
        }
    }

    /**
     * Gets all possible boards of a board by performing all legal moves of all
     * balls.
     * 
     * @param board
     *            the board
     * @param currentPlayer
     *            the player who is on the line
     * @return all possible boards in a list of boards
     */
    private List<Board> getAllPossibleBoards(AbaloneBoard board,
                                             Player currentPlayer) {
        List<Board> result = new LinkedList<Board>();
        List<Ball> balls;
        if (currentPlayer == Player.HUMAN) {
            balls = board.playerBallList;
        } else {
            balls = board.machineBallList;
        }
        for (int i = 0; i < balls.size(); i++) {
            if (balls.get(i) != null) {
                int row = balls.get(i).getPosition().getRow();
                int diag = balls.get(i).getPosition().getDiag();
                Board newBoard = board.move(row, diag, row + 1, diag);
                if (newBoard != null) {
                    result.add(newBoard);
                }
                newBoard = board.move(row, diag, row, diag + 1);
                if (newBoard != null) {
                    result.add(newBoard);
                }
                newBoard = board.move(row, diag, row + 1, diag + 1);
                if (newBoard != null) {
                    result.add(newBoard);
                }
                newBoard = board.move(row, diag, row - 1, diag);
                if (newBoard != null) {
                    result.add(newBoard);
                }
                newBoard = board.move(row, diag, row, diag - 1);
                if (newBoard != null) {
                    result.add(newBoard);
                }
                newBoard = board.move(row, diag, row - 1, diag - 1);
                if (newBoard != null) {
                    result.add(newBoard);
                }
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isGameOver() {
        return !(ELIM > ballAmount - playerBallList.size() 
               && ELIM > ballAmount - machineBallList.size());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Player getWinner() {
        if (isGameOver()) {
            if (ELIM <= ballAmount - playerBallList.size()) {
                return Player.MACHINE;
            } else {
                return Player.HUMAN;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNumberOfBalls(Color color) {
        if (humanColor == color) {
            return playerBallList.size();
        } else {
            return machineBallList.size();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Color getSlot(int row, int diag) {
        if (board[row][diag] == null) {
            return null;
        } else if (board[row][diag].getBall() != null) {
            return board[row][diag].getColor();
        } else {
            return Color.NONE;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSize() {
        return boardSize;
    }

    /**
     * Creates a String to represent the board of the game abalone. {@code X}
     * represents black Balls, {@code O} represents white Balls and {@code .}
     * represents a empty Slot. Between all slots is also a whitespace.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("");
        for (int row = boardSize - 1; row >= 0; row--) {
            int middleRowIndex = (boardSize / 2);
            if (row <= middleRowIndex) {
                int toDiag = boardSize - (middleRowIndex - row);
                int fromDiag = 0;
                for (int i = 0; i < middleRowIndex - row; i++) {
                    builder.append(" ");
                }
                while (fromDiag < toDiag) {
                    builder.append(board[row][fromDiag] + " ");
                    fromDiag++;
                }
            } else if (row > middleRowIndex) {
                int fromDiag = row - middleRowIndex;
                int toDiag = boardSize;
                for (int i = 0; i < row - middleRowIndex; i++) {
                    builder.append(" ");
                }
                while (fromDiag < toDiag) {
                    builder.append(board[row][fromDiag] + " ");
                    fromDiag++;
                }
            }
            // Delete last whitespace.
            builder.deleteCharAt(builder.length() - 1);
            builder.append("\n");
        }
        return builder.toString();
    }

    /**
     * Clones the Object Board of this instance (Deepclone).
     * 
     * @return a duplicate of this board
     */
    @Override
    public Board clone() {
        AbaloneBoard clone = new AbaloneBoard(boardSize, machineIsBeginner);
        clone.lastPlayer = this.lastPlayer;
        clone.humanColor = this.humanColor;
        clone.machineColor = this.machineColor;
        clone.level = this.level;
        clone.machineIsBeginner = this.machineIsBeginner;
        clone.playerBallList = new ArrayList<Ball>();
        clone.machineBallList = new ArrayList<Ball>();
        
        // Copy all balls of the last AbaloneBoard
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] != null) {
                    clone.board[i][j] = new Slot();
                }
            }
        }
        for (Ball ball : this.playerBallList) {
            int row = ball.getPosition().getRow();
            int diag = ball.getPosition().getDiag();
            Position clonedPos = new Position(row, diag);
            clone.board[row][diag].setBall(new Ball(ball.getColor()));
            clone.board[row][diag].getBall().changePosition(clonedPos);
            clone.playerBallList.add(clone.board[row][diag].getBall());
        }
        for (Ball ball : this.machineBallList) {
            int row = ball.getPosition().getRow();
            int diag = ball.getPosition().getDiag();
            Position clonedPos = new Position(row, diag);
            clone.board[row][diag].setBall(new Ball(ball.getColor()));
            clone.board[row][diag].getBall().changePosition(clonedPos);
            clone.machineBallList.add(clone.board[row][diag].getBall());
        }
        return clone;
    }
}
