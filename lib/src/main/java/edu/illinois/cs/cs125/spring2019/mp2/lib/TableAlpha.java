package edu.illinois.cs.cs125.spring2019.mp2.lib;

import java.util.Random;

/**
 * The board of slots plus booleans that tells whether slots are detected, whether slots are locked,
 * and the number of mines in total. This will be the class whose instance object is directly
 * manipulated by the GameActivity class.
 */
public class TableAlpha extends BoardAlpha {
    /**
     * Whether each slot is detected.
     */
    private boolean[][] detected;
    /**
     * Whether each slot is locked.
     */
    private boolean[][] locked;
    /**
     * The total number of mines in this board.
     */
    private int count;
    /**
     * Constructor that setup only width and height with no mine in side.
     * @param width the width of table.
     * @param height the height of table.
     */
    public TableAlpha(final int width, final int height) {
        super(width, height);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                for (int k = -1; k < 2; k++) {
                    for (int l = -1; l < 2; l++) {
                        if (k != 0 || l != 0) {
                            if (getSlot(i + k, j + l) != null) {
                                getSlot(i, j).addAround(getSlot(i + k, j + l));
                            }
                        }
                    }
                }
            }
        }
        detected = new boolean[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                detected[i][j] = false;
                locked[i][j] = false;
            }
        }
        count = 0;
    }
    /**
     * Constructor that setup width and height and randomly setup mines according to the given number..
     * @param width the width of table.
     * @param height the height of table.
     * @param number the number of mines.
     */
    public TableAlpha(final int width, final int height, final int number) {
        this(width, height);
        Random random = new Random();
        for (int i = 0; i < number; i++) {
            boolean temp = dropMine(random.nextInt(getTotalX()), random.nextInt(getTotalY()));
            if (!temp) {
                i--;
            }
        }
    }
    /**
     * Contructor that copy a given other board, but mine locations are randomly generated.
     * @param other the table to copy
     */
    public TableAlpha(final TableAlpha other) {
        this(other.getTotalX(), other.getTotalY(), other.getCount());
    }
    /**
     * Check whether the setup values are valid.
     * @param width the width value.
     * @param height the height value.
     * @param mines the number of mines.
     * @return true if this whole set of values are valid, and false otherwise.
     */
    public static boolean check(final int width, final int height, final int mines) {
        int diff = Math.abs(width - height);
        if (2 * diff > width || 2 * diff > height) {
            return false;
        }
        if (2 * mines > (2 + 1) * width && 2 * mines > (2 + 1) * height) {
            return false;
        }
        return true;
    }
    /**
     * Create a new table with given width, height, and mine number, if they are actually valid.
     * @param width the width value.
     * @param height the height value.
     * @param number the number of mines.
     * @return the table if values are valid, and null otherwise.
     */
    public static TableAlpha create(final int width, final int height, final int number) {
        if (check(width, height, number)) {
            return new TableAlpha(width, height, number);
        }
        return null;
    }
    /**
     * Get the width of the table.
     * @return the width of table
     */
    public int getWidth() {
        return getTotalX();
    }
    /**
     * Get the height of the table.
     * @return the height of the table.
     */
    public int getHeight() {
        return getTotalY();
    }
    /**
     * Get the number of mines in the table.
     * @return the number of mines.
     */
    public int getCount() {
        return count;
    }
    /**
     * See whether a specific individual slot in table is detected.
     * @param x the X coordinate position.
     * @param y the Y coordinate position.
     * @return true if this slot is already detected.
     */
    public boolean getDetect(final int x, final int y) {
        return detected[x][y];
    }
    /**
     * Try to detect a specific individual slot, return true and detect it if it wasn't detected.
     * @param x the X coordinate position.
     * @param y the Y coordinate position.
     * @return true if detection is valid, and false if it is already detected.
     */
    public boolean detect(final int x, final int y) {
        if (detected[x][y]) {
            return false;
        }
        detected[x][y] = true;
        return true;
    }
    /**
     * Open a specific individual slot, and return true if you get boomed.
     * @param x the X coordinate position.
     * @param y the Y coordinate position.
     * @return true you are boomed, false if you are safe.
     */
    public boolean boomed(final int x, final int y) {
        return seeMine(x, y);
    }
    /**
     * Get the number of mines around a specific individual slot.
     * @param x the X coordinate position.
     * @param y the Y coordinate position.
     * @return the number of mines nearby.
     */
    public int countAround(final int x, final int y) {
        int around = 0;
        for (SlotAlpha slot: getSlot(x, y).getAround()) {
            if (slot.getMine()) {
                around++;
            }
        }
        return around;
    }
    /**
     * Change the lock status, which will be to lock or unlock a undetected slot.
     * @param x the X coordinate position.
     * @param y the Y coordinate position.
     * @return whether this slot is locked after this change.
     */
    public boolean changeLock(final int x, final int y) {
        locked[x][y] = !locked[x][y];
        return locked[x][y];
    }
    /**
     * Get the lock status of a specific individual slot.
     * @param x the X coordinate position.
     * @param y the Y coordinate position.
     * @return the current lock status of this slot.
     */
    public boolean getLock(final int x, final int y) {
        return locked[x][y];
    }

    /**
     * Try to generate a new Mine and update the total number of mines in table.
     * @param x the X coordinate position.
     * @param y the Y coordinate position.
     * @return true if we create a new mine, and false if there is already mine here.
     */
    public boolean dropMine(final int x, final int y) {
        Random random = new Random();
        boolean temp = generateMine(random.nextInt(getTotalX()), random.nextInt(getTotalY()));
        if (temp) {
            count++;
            return true;
        }
        return false;
    }
}

