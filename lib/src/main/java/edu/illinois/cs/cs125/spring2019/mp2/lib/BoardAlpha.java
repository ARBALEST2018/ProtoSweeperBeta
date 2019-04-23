package edu.illinois.cs.cs125.spring2019.mp2.lib;
/**
 * The board in form of 2d array of slots.
 */
class BoardAlpha {
    /**
     * The width of board.
     */
    private int totalX;
    /**
     * The height of board.
     */
    private int totalY;
    /**
     * Board itself as 2d array of slots.
     */
    private SlotAlpha[][] board;
    /**
     * The constructor that initialize a empty board with only width and height.
     * @param setTotalX the width of board.
     * @param setTotalY the height of board.
     */
    BoardAlpha(final int setTotalX, final int setTotalY) {
        board = new SlotAlpha[setTotalX][setTotalY];
        for (int i = 0; i < totalX; i++) {
            for (int j = 0; j < totalY; j++) {
                board[i][j] = new SlotAlpha(i, j);
            }
        }
    }
    /**
     * Copy the given other board, including the specific mine locations.
     * @param other the board to be copied.
     */
    BoardAlpha(final BoardAlpha other) {
        board = new SlotAlpha[other.getTotalX()][other.getTotalY()];
        for (int i = 0; i < totalX; i++) {
            for (int j = 0; j < totalY; j++) {
                board[i][j] = new SlotAlpha(i, j, other.getSlot(i, j).getMine());
            }
        }
    }
    /**
     * Get the width of board.
     * @return the width of board.
     */
    public int getTotalX() {
        return totalX;
    }
    /**
     * Get the height of board.
     * @return the height of board.
     */
    public int getTotalY() {
        return totalY;
    }
    /**
     * Get the specific individual slot in the board.
     * @param x the X coordinate position.
     * @param y the Y coordinate position.
     * @return the slot on this position.
     */
    public SlotAlpha getSlot(final int x, final int y) {
        if (x < 0 || x >= totalX || y < 0 || y >= totalY) {
            return null;
        }
        return board[x][y];
    }
    /**
     * Try to generate a new mine of a specific individual slot, return true and put a new mine if there
     * was no mine here, and return false if there is already a mine here.
     * @param x the X coordinate position.
     * @param y the Y coordinate position.
     * @return true if we create a new mine, and false if there is already mine.
     */
    public boolean generateMine(final int x, final int y) {
        if (board[x][y].getMine()) {
            return false;
        }
        board[x][y].setMine(true);
        return true;
    }
    /**
     * See whether there is a mine in a specific individual slot.
     * @param x the X coordinate position.
     * @param y the Y coordinate position.
     * @return true if there is mine in the slot to check, and false otherwise.
     */
    public boolean seeMine(final int x, final int y) {
        return board[x][y].getMine();
    }
}
