package edu.illinois.cs.cs125.spring2019.mp2.lib;

/**
 * A class that implements a Connect4-like game.
 *
 * ConnectN is a tile-based game played on a grid. Like Connect4, players try to get a run of tiles
 * of a given length (N). However, unlike Connect4 ConnectN does not check diagonal runs, although
 * you can add this feature if you like.
 *
 * This ConnectN class is also not responsible for tracking players turns. That could be done by a
 * separate class, allowing for game variations in which players can sometimes take more than one
 * turn in a row. Other variations allow more than two players. In any case, the ConnectN class
 * only monitors gameplay to determine when the game has ended, then determines the winning player.
 */
public class ConnectN {
    /**
     * Check if the passed width value is valid.
     *
     * @param toCheck - the width value to check
     * @return true if the width value is valid, false otherwise
     */
    public static boolean checkWidth(final int toCheck) {
        return (toCheck >= MIN_WIDTH && toCheck <= MAX_WIDTH);
    }

    /**
     * Check if the passed height value is valid.
     *
     * @param toCheck - the height value to check
     * @return true if the height value is valid, false otherwise
     */
    public static boolean checkHeight(final int toCheck) {
        return (toCheck >= MIN_HEIGHT && toCheck <= MAX_HEIGHT);
    }

    /**
     * Check if the passed n value is valid.
     *
     * @param width - the width value that helps in checking
     * @param height - the height value that helps in checking
     * @param toCheck the n value to check
     * @return true if the n value is valid, false otherwise
     */
    public static boolean checkN(final int width, final int height, final int toCheck) {
        if (checkWidth(width) && checkHeight(height) && toCheck >= MIN_N) {
            if (toCheck < width || toCheck < height) {
                return true;
            }
        }
        return false;
    }
    /** Minimum board width is 6. */
    public static final int MIN_WIDTH = 6;

    /** Maximum board width is 16. */
    public static final int MAX_WIDTH = 16;

    /** Minimum board height is 6. */
    public static final int MIN_HEIGHT = 6;

    /** Maximum board height is 16. */
    public static final int MAX_HEIGHT = 16;

    /** Minimum board N value is 4. */
    public static final int MIN_N = 4;

    /** Board height. */
    private int height;

    /** Board width. */
    private int width;

    /** Board N. */
    private int n;

    /** Board. */
    private Player[][] board;

    /** Whether the game has started. */
    private boolean started;

    /** Whether the game has ended. */
    private boolean ended;

    /** Numbers of movements. */
    private static int movement;

    /**
     * Get the current board width.
     *
     * @return the current board width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Attempt to set the board width.
     *
     * @param setWidth - the new width to set
     * @return true if the width was set successfully, false on error
     */
    public boolean setWidth(final int setWidth) {
        if (started) {
            return false;
        }
        if (!checkWidth(setWidth)) {
            return false;
        }
        width = setWidth;
        if (!checkN(setWidth, height, n)) {
            n = 0;
        }
        if (checkWidth(width) && checkHeight(height)) {
            board = new Player[width][height];
        }
        return true;
    }

    /**
     * Get the current board height.
     *
     * @return the current board height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Attempt to set the board height.
     *
     * @param setHeight - the new height to set
     * @return true if the height was set successfully, false on error
     */
    public boolean setHeight(final int setHeight) {
        if (started) {
            return false;
        }
        if (!checkHeight(setHeight)) {
            return false;
        }
        height = setHeight;
        if (!checkN(width, setHeight, n)) {
            n = 0;
        }
        if (checkWidth(width) && checkHeight(height)) {
            board = new Player[width][height];
        }
        return true;
    }

    /**
     * Get the current board N value.
     *
     * @return the current board N value
     */
    public int getN() {
        return n;
    }

    /**
     * Attempt to set the current board N value.
     *
     * @param newN - the new N
     * @return true if N was set successfully, false otherwise
     */
    public boolean setN(final int newN) {
        if (started) {
            return false;
        }
        if (!checkN(getWidth(), getHeight(), newN)) {
            return false;
        }
        n = newN;
        return true;
    }
    /**
     * Creates a new ConnectN board with a given width, height, and N value.
     *
     * @param setWidth - the width for the new ConnectN board
     * @param setHeight - the height for the new ConnectN board
     * @param setN - the N value for the new ConnectN board
     */
    public ConnectN(final int setWidth, final int setHeight, final int setN) {
        if (checkWidth(setWidth)) {
            width = setWidth;
        }
        if (checkHeight(setHeight)) {
            height = setHeight;
        }
        if (checkN(width, height, setN)) {
            n = setN;
        }
        board = new Player[width][height];
        started = false;
        ended = false;
    }

    /**
     * Create a new ConnectN board with uninitialized width, height, and N value.
     */
    public ConnectN() {
        started = false;
        ended = false;
    }

    /**
     * Create a new ConnectN board with given width and height, but uninitialized N value.
     *
     * @param setWidth - the width for the new ConnectN board
     * @param setHeight - the height for the new ConnectN board
     */
    public ConnectN(final int setWidth, final int setHeight) {
        if (checkWidth(setWidth)) {
            width = setWidth;
        }
        if (checkHeight(setHeight)) {
            height = setHeight;
        }
        board = new Player[width][height];
        started = false;
        ended = false;
    }

    /**
     * Create a new ConnectN board with dimensions and N value copied from another board.
     *
     * @param otherBoard - the ConnectN board to copy width, height, and N from
     */
    public ConnectN(final ConnectN otherBoard) {
        width = otherBoard.getWidth();
        height = otherBoard.getHeight();
        n = otherBoard.getN();
        board = new Player[width][height];
        started = false;
        ended = false;
    }



    /**
     * Set the board at a specific position.
     *
     * @param player - the player attempting the move
     * @param setX - the X coordinate that they are trying to place a tile at
     * @param setY - the Y coordinate that they are trying to place a tile at
     * @return true if the move succeeds, false on error
     */
    public boolean setBoardAt(final Player player, final int setX, final int setY) {
        if (ended) {
            return false;
        }
        if (setX < 0 || setX >= width || setY < 0 || setY >= height) {
            return false;
        }
        boolean supported = true;
        for (int i = 0; i < setY; i++) {
            if (getBoardAt(setX, i) == null) {
                supported = false;
            }
        }
        if (supported && getBoardAt(setX, setY) == null) {
            started = true;
            board[setX][setY] = player;
            return true;
        }
        return false;
    }

    /**
     * Drop a tile in a particular column.
     *
     * @param player - the player attempting the move
     * @param setX - the X coordinate for the stack that they are trying to drop a tile in
     * @return true if the move succeeds, false on error
     * */
    public boolean setBoardAt(final Player player, final int setX) {
        if (ended) {
            return false;
        }
        if (setX < 0 || setX >= width) {
            return false;
        }
        for (int i = 0; i < height; i++) {
            if (getBoardAt(setX, i) == null) {
                setBoardAt(player, setX, i);
                return true;
            }
        }
        return false;
    }

    /**
     * Get the player at a specific board position.
     *
     * @param getX - the X coordinate to get the player at
     * @param getY - the Y coordinate to get the player at
     * @return the player whose tile is at that position, or null if nobody has played at that
     * position
     * */
    public Player getBoardAt(final int getX, final int getY) {
        if (getX >= 0 && getX < width && getY >= 0 && getY < height) {
            return board[getX][getY];
        }
        return null;
    }

    /**
     * Return a copy of the board.
     *
     * @return a copy of the current board
     * */
    public Player[][] getBoard() {
        if (width == 0 || height == 0) {
            return null;
        }
        Player[][] copy = new Player[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (board[i][j] == null) {
                    copy[i][j] = null;
                } else {
                    copy[i][j] = new Player(board[i][j]);
                }
            }
        }
        return copy;
    }

    /**
     * Return the winner of the game, or null if the game has not ended.
     *
     * @return the winner of the game, or null if the game has not ended
     */
    public Player getWinner() {
        Player[] temp = new Player[getN()];
        boolean possible;
        if (!checkWidth(getWidth()) || !checkHeight(getHeight()) || !checkN(getWidth(), getHeight(), getN())) {
            return null;
        }
        if (getN() < getWidth()) {
            for (int i = 0; i < getWidth() - getN(); i++) {
                for (int j = 0; j < getHeight(); j++) {
                    possible = true;
                    for (int k = 0; k < getN(); k++) {
                        if (getBoardAt(i + k, j) == null) {
                            possible = false;
                        } else {
                            temp[k] = getBoardAt(i + k, j);
                        }
                    }
                    if (possible) {
                        boolean allEqual = true;
                        for (int k = 0; k < getN() - 1; k++) {
                            if (!temp[k].equals(temp[k + 1])) {
                                allEqual = false;
                            }
                        }
                        if (allEqual) {
                            ended = true;
                            temp[0].addScore();
                            return temp[0];
                        }
                    }
                }
            }
        }
        if (getN() < getHeight()) {
            for (int i = 0; i < getWidth(); i++) {
                for (int j = 0; j < getHeight() - getN(); j++) {
                    possible = true;
                    for (int k = 0; k < getN(); k++) {
                        if (getBoardAt(i, j + k) == null) {
                            possible = false;
                        } else {
                            temp[k] = getBoardAt(i, j + k);
                        }
                    }
                    if (possible) {
                        boolean allEqual = true;
                        for (int k = 0; k < getN() - 1; k++) {
                            if (!temp[k].equals(temp[k + 1])) {
                                allEqual = false;
                            }
                        }
                        if (allEqual) {
                            ended = true;
                            temp[0].addScore();
                            return temp[0];
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Class method to create a new ConnectN board.
     *
     * @param width - the width of the new ConnectN instance to create
     * @param height - the height of the new ConnectN instance to create
     * @param n - the n value of the new ConnectN instance to create
     * @return the new ConnectN instance, or null if the parameters are invalid
     */
    public static ConnectN create(final int width, final int height, final int n) {
        if (checkWidth(width) && checkHeight(height)) {
            if (checkN(width, height, n)) {
                return new ConnectN(width, height, n);
            }
        }
        return null;
    }

    /**
     * Creates multiple new ConnectN instances.
     *
     * @param number - the number of new ConnectN instances to create
     * @param width - the width of the new ConnectN instance to create
     * @param height - the height of the new ConnectN instance to create
     * @param n - the n value of the new ConnectN instance to create
     * @return an array of new ConnectN instances, or null if the parameters are invalid
     * */
    public static ConnectN[] createMany(final int number, final int width, final int height, final int n) {
        ConnectN[] boards;
        if (number == 0) {
            return null;
        }
        if (checkWidth(width) && checkHeight(height)) {
            if (checkN(width, height, n)) {
                boards = new ConnectN[number];
                for (int i = 0; i < number; i++) {
                    boards[i] = new ConnectN(width, height, n);
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
        return boards;
    }

    /**
     * Compare any number of ConnectN boards.
     *
     * @param firstBoard - the first ConnectN board to compare
     * @param secondBoard - the second ConnectN board to compare
     * @return true if the boards are the same, false otherwise
     * */
    public static boolean compareBoards(final ConnectN firstBoard, final ConnectN secondBoard) {
        if (firstBoard == null || secondBoard == null) {
            return false;
        }
        if (firstBoard.getWidth() != secondBoard.getWidth() || firstBoard.getHeight() != secondBoard.getHeight()) {
            return false;
        }
        if (firstBoard.getN() != secondBoard.getN()) {
            return false;
        }
        for (int i = 0; i < firstBoard.getWidth(); i++) {
            for (int j = 0; j < firstBoard.getHeight(); j++) {
                if (firstBoard.getBoardAt(i, j) == null) {
                    if (!(secondBoard.getBoardAt(i, j) == null)) {
                        return false;
                    }
                } else {
                    if (secondBoard.getBoardAt(i, j) == null) {
                        return false;
                    }
                    if (!(firstBoard.getBoardAt(i, j).equals(secondBoard.getBoardAt(i, j)))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Compare any number of ConnectN boards.
     *
     * @param boards - the array of ConnectN boards to compare
     * @return true if all passed boards are the same, false otherwise
     * */
    public static boolean compareBoards(final ConnectN... boards) {
        for (int i = 0; i < boards.length - 1; i++) {
            if (!compareBoards(boards[i], boards[i + 1])) {
                return false;
            }
        }
        return true;
    }


}
