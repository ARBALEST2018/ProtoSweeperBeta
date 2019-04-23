package edu.illinois.cs.cs125.spring2019.mp2.lib;

import java.util.Random;

/**
 *
 */
public class TableAlpha extends BoardAlpha {
    /**
     *
     */
    private boolean[][] detected;
    /**
     *
     */
    private boolean[][] locked;
    /**
     *
     */
    private int count;
    /**
     *
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
     *
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
     *
     */
    public TableAlpha(TableAlpha other) {
        this(other.getTotalX(), other.getTotalY(), other.getCount());
    }
    /**
     *
     */
    public static boolean check(final int width, final int height, final int mines) {
        int diff = Math.abs(width - height);
        if (2 * diff > width || 2 * diff > height) {
            return false;
        }
        if (2 * mines > 3 * width && 2 * mines > 3 * height) {
            return false;
        }
        return true;
    }
    /**
     *
     */
    public static TableAlpha create(final int width, final int height, final int number) {
        if (check(width, height, number)) {
            return new TableAlpha(width, height, number);
        }
        return null;
    }
    /**
     *
     */
    public int getWidth() {
        return getTotalX();
    }
    /**
     *
     */
    public int getHeight() {
        return getTotalY();
    }
    /**
     *
     */
    public int getCount() {
        return count;
    }
    /**
     *
     */
    public boolean getDetect(final int x, final int y) {
        return detected[x][y];
    }
    /**
     *
     */
    public boolean detect(final int x, final int y) {
        if (detected[x][y]) {
            return false;
        }
        detected[x][y] = true;
        return true;
    }
    /**
     *
     */
    public boolean boomed(final int x, final int y) {
        return seeMine(x, y);
    }
    /**
     *
     */
    public int countAround(final int x, final int y) {
        int count = 0;
        for (SlotAlpha slot: getSlot(x, y).getAround()) {
            if (slot.getMine()) {
                count++;
            }
        }
        return count;
    }
    /**
     *
     */
    public boolean changeLock(final int x, final int y) {
        locked[x][y] = !locked[x][y];
        return locked[x][y];
    }
    /**
     *
     */
    public boolean getLock(final int x, final int y) {
        return locked[x][y];
    }
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

