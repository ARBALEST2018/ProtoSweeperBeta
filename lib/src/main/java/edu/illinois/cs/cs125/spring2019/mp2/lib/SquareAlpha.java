package edu.illinois.cs.cs125.spring2019.mp2.lib;

/**
 * Class for individual square, contains only coordinate position and whether there is mine here.
 */
public class SquareAlpha {
    /**
     * Whether there is mine here.
     */
    private boolean isMine;
    /**
     *  X coordinate position.
     */
    private int x;
    /**
     * Y coordinate position.
     */
    private int y;
    //SlotAlpha[] around;
    /**
     * Constructor that only setup coordinate position.
     * @param setX the X coordinate position to set.
     * @param setY the Y coordinate position to set.
     */
    public SquareAlpha(final int setX, final int setY) {
        x = setX;
        y = setY;
        isMine = false;
        //around = new SlotAlpha[8];
    }
    /**
     * Constructor that setup coordinate position and whether there is mine.
     * @param setX the X coordinate position to set.
     * @param setY the Y coordinate position to set.
     * @param setMine whether there is mine here.
     */
    public SquareAlpha(final int setX, final int setY, final boolean setMine) {
        x = setX;
        y = setY;
        isMine = setMine;
        //around = new SlotAlpha[8];
    }

    /**
     * Set X coordinate position.
     * @param toSet the value to set.
     */
    public void setX(final int toSet) {
        x = toSet;
    }

    /**
     * Set Y coordinate position.
     * @param toSet the value to set.
     */
    public void setY(final int toSet) {
        y = toSet;
    }

    /**
     * Set whether there is mine here.
     * @param toSet the boolean value to set.
     */
    public void setMine(final boolean toSet) {
        isMine = toSet;
    }

    /**
     * Get X coordinate position.
     * @return the X coordinate position.
     */
    public int getX() {
        return x;
    }

    /**
     * Get Y coordinate position.
     * @return the Y coordinate position.
     */
    public int getY() {
        return y;
    }

    /**
     * Get whether there is mine here.
     * @return the boolean that indicates mine-existence.
     */
    public boolean getMine() {
        return isMine;
    }
    /*public void setSlot(final SlotAlpha toSet, final int position) {
        around[position - 1] = toSet;
    }*/
}
