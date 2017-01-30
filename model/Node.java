package abalone.model;

import java.util.LinkedList;
import java.util.List;

/**
 * The class node saves a board in it which was created for the
 * {@code machineMove()} logic.
 * 
 * @version 1.0
 */
public class Node {

    private Board board;
    private List<Node> children;

    /**
     * Creates a node with a board saved in it.
     * 
     * @param board
     *            the board which should be saved in the node
     */
    public Node(Board board) {
        this.board = board;
        children = new LinkedList<Node>();
    }

    /**
     * Adds a child to this node.
     * 
     * @param child
     *            the child which should be added
     */
    public void addChild(Node child) {
        children.add(child);
    }

    /**
     * Gets the children of this node.
     * 
     * @return a List of Nodes which are the children of this node
     */
    public List<Node> getChildren() {
        return children;
    }

    /**
     * Returns the board which was "saved" in the node.
     * 
     * @return the board of the node
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Checks if the node has children.
     * 
     * @return {@code true} if the node has at least one child and {@code false}
     *         if the node has no child
     */
    public boolean hasChildren() {
        return !children.isEmpty();
    }
}
