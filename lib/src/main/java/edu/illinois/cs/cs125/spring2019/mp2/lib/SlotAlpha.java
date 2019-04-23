package edu.illinois.cs.cs125.spring2019.mp2.lib;

import java.util.List;

/**
 * The individual squares plus a list of its nearby squares.
 */
public class SlotAlpha extends SquareAlpha {
    /**
     * List of nearby squares in form of slots.
     */
    private List<SlotAlpha> around;
    /**
     * Constructor that only setup coordinate position.
     * @param setX the X coordinate position to set.
     * @param setY the Y coordinate position to set.
     */
    public SlotAlpha(final int setX, final int setY) {
        super(setX, setY);
    }
    /**
     * Constructor that setup coordinate position and whether there is mine.
     * @param setX the X coordinate position to set.
     * @param setY the Y coordinate position to set.
     * @param setMine whether there is mine here.
     */
    public SlotAlpha(final  int setX, final int setY, final boolean setMine) {
        super(setX, setY, setMine);
    }
    /**
     * Add individual square in form of slot into the list of nearby squares.
     * @param toAdd the slot to add.
     */
    public void addAround(final SlotAlpha toAdd) {
        if (!around.contains(toAdd)) {
            around.add(toAdd);
        }
    }
    /**
     * Get the list of nearby slots.
     * @return a list of the nearby slots, maximum 8 and minimum 3
     */
    public List<SlotAlpha> getAround() {
        return around;
    }
    /**
     * Count how many nearby squares have mines in total.
     * @return the number of squares that has mine in the nearby 8 slots.
     */
    public int countAround() {
        int count = 0;
        for (SlotAlpha single: around) {
            if (single.getMine()) {
                count++;
            }
        }
        return count;
    }
}