package abalone.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Callable;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/**
 * The MenuPanel is a JPanel, it is the menu for the game Abalone. The MenuPanel
 * has a button to start a new game, to switch the starting player and to quit
 * the game. In the MenuPanel you can also configure the level of the KI and the
 * board size of the game. The MenuPanel also gives the player the information
 * how many balls on which side are left.
 * 
 * @version 1.0
 */
@SuppressWarnings("serial")
public class MenuPanel extends JPanel {
    
    private JLabel blackBalls;
    private JLabel whiteBalls;
    private BoardPanel boardPanel;

    /**
     * Creates the menu panel of the gui of the game abalone.
     * 
     * @param layout
     *            the layout of the panel
     */
    public MenuPanel(LayoutManager layout) {
        super(layout);
        blackBalls = new JLabel();
        whiteBalls = new JLabel();
        // Increase the font size of both panels to 30.
        whiteBalls.setFont(whiteBalls.getFont().deriveFont(30.0f));
        blackBalls.setFont(blackBalls.getFont().deriveFont(30.0f));
        // Change the color of the font of both panels.
        whiteBalls.setForeground(Color.WHITE);
        blackBalls.setForeground(Color.BLACK);
        JPanel buttonPanel = new JPanel();
        addDropDownMenus(buttonPanel);
        addButtons(buttonPanel);
        add(buttonPanel, BorderLayout.CENTER);
        add(blackBalls, BorderLayout.WEST);
        add(whiteBalls, BorderLayout.EAST);
        setBackground(Color.GRAY);
    }

    private void addDropDownMenus(JPanel buttonPanel) {
        JLabel labelSize = new JLabel("Size: ");
        // options for the board size drop-down-menu.
        Integer[] options = new Integer[] { 7, 9, 11, 13 };
        JComboBox<Integer> dropDownSize = new JComboBox<Integer>(options);
        dropDownSize.setSelectedItem(9);
        // options for the level drop-down-menu.
        options = new Integer[] { 1, 2, 3 };
        JLabel labelLevel = new JLabel("Level: ");
        JComboBox<Integer> dropDownLevel = new JComboBox<Integer>(options);
        dropDownLevel.setSelectedItem(2);
        buttonPanel.setOpaque(false);
        buttonPanel.add(labelSize);
        buttonPanel.add(dropDownSize);
        dropDownSize.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                boardPanel.changeBoardSize((int)
                                           dropDownSize.getSelectedItem());
            }
        });
        buttonPanel.add(labelLevel);
        buttonPanel.add(dropDownLevel);
        dropDownLevel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                boardPanel.changeLevel((int) dropDownLevel.getSelectedItem());
            }
        });
    }

    private void addButtons(JPanel buttonPanel) {
        JButton buttonNew = new JButton("New");
        // Add listener which will listen for a click on the button.
        buttonNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                boardPanel.createNewGame();
            }
        });
        JButton buttonSwitch = new JButton("Switch");
        buttonSwitch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                boardPanel.switchGame();
            }
        });
        JButton buttonQuit = new JButton("Quit");
        buttonQuit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                boardPanel.quit();
            }
        });
        // Add shortcuts to the buttons to make the menu more comfortable.
        addShortcut(buttonNew, 'N', new Callable<Void>() {
            public Void call() {
                boardPanel.createNewGame();
                return null;
            }
        });
        addShortcut(buttonSwitch, 'S', new Callable<Void>() {
            public Void call() {
                boardPanel.switchGame();
                return null;
            }
        });
        addShortcut(buttonQuit, 'Q', new Callable<Void>() {
            public Void call() {
                boardPanel.quit();
                return null;
            }
        });
        buttonPanel.add(buttonNew);
        buttonPanel.add(buttonSwitch);
        buttonPanel.add(buttonQuit);
    }

    /**
     * Adds a shortcut to a {@code JButton}.
     * 
     * @param button
     *            the button
     * @param key
     *            the key on the keyboard which should be pressed in combination
     *            with the control key
     * @param function
     *            the function which will be executed
     */
    private void addShortcut(JButton button, char key,
                             Callable<Void> function) {
        button.setMnemonic(key);
        String actionName = null;
        Action buttonAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                try {
                    function.call();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        };
        switch (key) {
        case 'S':
            actionName = "Switch";
            break;
        case 'N':
            actionName = "New";
            break;
        case 'Q':
            actionName = "Quit";
            break;
        default:
            new IllegalArgumentException("Key not allowed");
            break;
        }
        buttonAction.putValue(Action.ACCELERATOR_KEY, 
                              KeyStroke.getKeyStroke("control " + key));
        button.getActionMap().put(actionName, buttonAction);
        button.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                      (KeyStroke) buttonAction.getValue(Action.ACCELERATOR_KEY),
                                                        actionName);
    }

    /**
     * Changes the text of both labels to the amount of balls on the field.
     * 
     * @param blackBallAmount
     *            the current amount of black balls on the field
     * @param whiteBallAmount
     *            the current amount of white balls on the field
     */
    public void setBallAmount(int blackBallAmount, int whiteBallAmount) {
        blackBalls.setText(Integer.toString(blackBallAmount));
        whiteBalls.setText(Integer.toString(whiteBallAmount));
    }

    /**
     * Sets the boardpanel reference of this menupanel. This menupanel will
     * interact with the reference of the boardpanel
     * 
     * @param boardPanel
     *            the boardpanel
     */
    public void setBoardPanel(BoardPanel boardPanel) {
        this.boardPanel = boardPanel;
    }
}
