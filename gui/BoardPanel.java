package abalone.gui;

import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import abalone.model.AbaloneBoard;
import abalone.model.Board;
import abalone.model.Color;
import abalone.model.Player;
import abalone.model.Position;
import abalone.model.Utility;

/**
 * The BoardPanel visualizes the game board of the game Abalone. The visualized
 * balls of the board can be moved by clicking first on the ball which should be
 * moved and then on the target position.
 * <p>
 * If the move was invalid the board panel will generate an error sound, if the
 * move was valid the BoardPanel will perform the move and repaint the grid. The
 * board does also create an error dialogue if the player is not on the turn and
 * it does also create a normal dialogue if the game is over.
 */
@SuppressWarnings("serial")
public class BoardPanel extends JPanel {

    private Board abalone;
    private GUI gui;
    private int boardSize;
    private int level;
    private Thread machineMoveThread;
    private SelectablePanel firstSelectedSlot;
    private boolean playerMoveBlocked;

    /**
     * Creates a BoardPanel with the given GUI. The GUI will be used to interact
     * with it.
     * 
     * @param gui
     *            the GUI
     */
    public BoardPanel(GUI gui) {
        this.gui = gui;
        abalone = new AbaloneBoard();
        boardSize = abalone.getSize();
        setLayout(new GridLayout(boardSize + 2, boardSize * 2));
        setupGrid();
        
        // sets the background to color brown
        setBackground(new java.awt.Color(130, 70, 20));
        updateBallAmount();
        gui.getMenuPanel().setBoardPanel(this);
    }

    /**
     * Performs a machine-move. The machine move will be started in a different
     * thread than the main thread to make it possible to interact with the GUI.
     * After the performed move the board panel will repaint the game board/the
     * grid. While the thread is running the player can not move.
     */
    private void performMachineMove() {
        machineMoveThread = new Thread(new Runnable() {
            @Override
            public void run() {
                playerMoveBlocked = true;
                while (abalone.getNextPlayer() == Player.MACHINE) {
                    abalone = abalone.machineMove();
                    repaint();
                    updateBallAmount();
                    if (abalone.isGameOver()) {
                        createWinDialog();
                        return;
                    }
                }
                playerMoveBlocked = false;
            }
        });
        machineMoveThread.start();
    }

    /**
     * Stops the machine-move-thread which has been started to perform a machine
     * move. This methods will be called if a new game should be started or if
     * the user stops the program.
     */
    @SuppressWarnings("deprecation")
    public void stopMachineMoveThread() {
        if (machineMoveThread != null && machineMoveThread.isAlive()) {
            machineMoveThread.stop();
        }
    }

    /**
     * Setups the grid by deleting all (old) components, adding new JPanels and
     * repainting the board panel.
     */
    private void setupGrid() {
        firstSelectedSlot = null;
        removeAll();
        addPanels();
        repaint();
        revalidate();
    }

    /**
     * Changes the (old) ball amount in the menu panel to the current amount of
     * balls of the game.
     */
    private void updateBallAmount() {
        gui.getMenuPanel().setBallAmount(abalone.getNumberOfBalls(Color.BLACK),
                                         abalone.getNumberOfBalls(Color.WHITE));
    }

    /**
     * Creates a new game with the settings (who is the beginner, the level and
     * the board size) of the last game.
     */
    public void createNewGame() {
        stopMachineMoveThread();
        boolean machineBegan = abalone.getOpeningPlayer() == Player.MACHINE;
        abalone = new AbaloneBoard(boardSize, machineBegan);
        changeToOldLevel();
        if (machineBegan) {
            performMachineMove();
        }
        setLayout(new GridLayout(boardSize + 2, boardSize * 2 + 5));
        setupGrid();
        updateBallAmount();
        playerMoveBlocked = false;
    }

    /**
     * Creates a new game with the settings (the level and the board size) of
     * the last game but with the starting player which did not began in the
     * last game.
     */
    public void switchGame() {
        stopMachineMoveThread();
        boolean machineBegan = abalone.getOpeningPlayer() == Player.MACHINE;
        abalone = new AbaloneBoard(boardSize, !machineBegan);
        changeToOldLevel();
        if (!machineBegan) {
            performMachineMove();
        }
        setupGrid();
        playerMoveBlocked = false;
    }

    /**
     * Changes the level to the level which is used in the last game.
     */
    private void changeToOldLevel() {
        if (level > 0) {
            abalone.setLevel(level);
        }
    }

    /**
     * Adds all Panels to the board panel.
     */
    private void addPanels() {
        for (int row = boardSize; row >= -1; row--) {
            int minDiag = Utility.minDiag(row, boardSize);
            int maxDiag = Utility.maxDiag(row, boardSize);
            int amount;
            if (minDiag > 0) {
                amount = minDiag;
            } else {
                amount = boardSize - maxDiag - 1;
            }
            // Add placeholder in front of the panel with the lowest diagonal
            // coordinates in the row.
            for (int diag = 0; diag <= amount; diag++) {
                add(new PlaceholderPanel());
            }
            JPanel edgePanel = new SelectablePanel(new Position(row, 
                                                                minDiag - 1));
            addMouseListener(edgePanel);
            add(edgePanel);
            add(new LinePanel());
            
            // Add all panels which have a valid position on the board.
            for (int diag = minDiag; diag <= maxDiag; diag++) {
                JPanel panelToAdd;
                if (row < boardSize && row >= 0) {
                    panelToAdd = new SlotPanel(new Position(row, diag));
                } else {
                    panelToAdd = new SelectablePanel(new Position(row, diag));
                }
                addMouseListener(panelToAdd);
                add(panelToAdd);
                add(new LinePanel());
            }
            JPanel edgePanel2 = new SelectablePanel(new Position(row, 
                                                                 maxDiag+ 1));
            addMouseListener(edgePanel2);
            add(edgePanel2);
            
            // Add placeholder after the panel with the lowest diagonal
            // coordinates in the row.
            for (int diag = 0; diag <= amount; diag++) {
                add(new PlaceholderPanel());
            }
        }
    }

    /**
     * Adds a customized mouse listener ({@code MouseAdaper}) to the given
     * panel.
     * 
     * @param panel
     *            the panel
     */
    private void addMouseListener(JPanel panel) {
        panel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                SelectablePanel selectedPanel = (SelectablePanel) 
                                                event.getComponent();
                if (firstSelectedSlot != null) {
                    performMove(firstSelectedSlot.getPosition(), 
                                selectedPanel.getPosition());
                    ((SlotPanel) firstSelectedSlot).setHighlighted(false);
                    firstSelectedSlot = null;
                } else {
                    if (selectedPanel instanceof SlotPanel
                        && ((SlotPanel) selectedPanel).getColor() 
                           == abalone.getHumanColor()) {
                        firstSelectedSlot = selectedPanel;
                        ((SlotPanel) selectedPanel).setHighlighted(true);
                    } else {
                        playErrorSound();
                    }
                }
            }
        });
    }

    /**
     * Plays the beep (error sound) of the system.
     */
    private void playErrorSound() {
        Toolkit.getDefaultToolkit().beep();
    }

    /**
     * Create a error message Dialog with the given message.
     * 
     * @param message
     *            the error message of the dialog
     */
    private void createErrorDialog(String message) {
        playErrorSound();
        JOptionPane.showMessageDialog(gui, message, "Error",
                                      JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Create a message Dialog with the given message.
     * 
     * @param message
     *            the message of the dialog
     */
    private void createDialog(String message) {
        JOptionPane.showMessageDialog(gui, message, "",
                                      JOptionPane.INFORMATION_MESSAGE);
    }

    private void createWinDialog() {
        if (abalone.getWinner() == Player.HUMAN) {
            createDialog("Congratulations you won the game!");
        } else {
            createDialog("Sorry, the machine won the game!");
        }
    }

    /**
     * Stops the machine-move-thread and the program.
     */
    public void quit() {
        System.exit(0);
    }

    /**
     * Changes the board size to the given board size. The board size must be
     * odd and higher than {@code 5}.
     * 
     * @param newBoardSize
     *            the new board size
     */
    public void changeBoardSize(int newBoardSize) {
        this.boardSize = newBoardSize;
        createNewGame();
    }

    /**
     * Changes the level of the KI of the machine player of the game Abalone to
     * the given level. The level must be greater than {@code 0}. A level higher
     * than {@code 3} is not recommended.
     * 
     * @param newLevel
     *            the new level
     */
    public void changeLevel(int newLevel) {
        level = newLevel;
        abalone.setLevel(newLevel);
    }

    /**
     * Performs a move of one ball in the game Abalone to another valid
     * position.
     * 
     * @param positionFrom
     *            the starting position
     * @param positionTo
     *            to target position
     * @return {@code true} if the move was has been performed and was valid or
     *         {@code false} if the move has not been performed and was invalid.
     */
    private boolean performMove(Position positionFrom, Position positionTo) {
        int rowFrom = positionFrom.getRow();
        int diagFrom = positionFrom.getDiag();
        int rowTo = positionTo.getRow();
        int diagTo = positionTo.getDiag();
        if (!abalone.isGameOver()) {
            if (!playerMoveBlocked) {
                Board result = abalone.move(rowFrom, diagFrom, rowTo, diagTo);
                if (result != null) {
                    abalone = result;
                    repaint();
                    updateBallAmount();
                    if (abalone.isGameOver()) {
                        playerMoveBlocked = true;
                        createWinDialog();
                    } else {
                        performMachineMove();
                    }
                    return true;
                } else {
                    playErrorSound();
                }
            } else {
                createErrorDialog("Its not your turn");

            }
        } else {
            createErrorDialog("The game is over, press new or switch to create"
                              + "a new game");
        }
        return false;
    }

    /**
     * Gets the color of one slot of the game board of the game Abalone.
     * 
     * @param row
     *            the row coordinates of the slot
     * @param diag
     *            the diagonal coordinates of the slot
     * @return the color of the slot
     */
    public Color getSlot(int row, int diag) {
        return abalone.getSlot(row, diag);
    }
}
