package edu.illinois.cs.cs125.spring2019.mp2;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import edu.illinois.cs.cs125.spring2019.mp2.lib.SlotAlpha;
import edu.illinois.cs.cs125.spring2019.mp2.lib.TableAlpha;

/**
 * The main activity that renders the ConnectN game and solicits moves from users.
 * <p>
 * MP2 provides our first example of an app with <em>multiple</em> Activitys, each representing a different screen.
 * When the app launches we open the SetupActivity Activity, which solicits initial game configuration parameters
 * from the user. It then launches the GameActivity which is responsible for soliciting moves and rendering the board
 * as game play proceeds.
 * <p>
 * The code below is <em>mostly</em> working, but as usual you have a few small changes to make before you code will
 * pass all of the test cases.
 *
 * @see <a href="https://cs125.cs.illinois.edu/MP/2/">MP2 Documentation</a>
 */
public class GameActivity extends AppCompatActivity {
    /**
     *
     */
    private static final String TAG = "Proto Sweeper Alpha";
    /**
     *
     */
    private static final double SLOT_BORDER_WIDTH_MULTIPLIER = 0.1;
    /**
     *
     */
    private View gameContainer;
    /**
     *
     */
    private TextView minesLeft;
    /**
     *
     */
    private TextView victory;
    /**
     *
     */
    private int total;
    /**
     *
     */
    private int current;
    /**
     *
     */
    private GridLayout table;
    /**
     *
     */
    private View[][] slots;
    /**
     *
     */
    private GradientDrawable emptySlot;
    /**
     *
     */
    private GradientDrawable mine;
    /**
     *
     */
    private GradientDrawable locked;
    /**
     *
     */
    private GradientDrawable undetected;
    /**
     *
     */
    private TableAlpha game;
    /**
     *
     */
    private boolean win;
    /**
     *
     */
    private boolean lose;
    /**
     *
     */
    private boolean toMark = false;

    /**
     * Oncreate.
     * @param savedInstanceState the saved instance state.
     */
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Intent intent = getIntent();
        game = new TableAlpha(intent.getIntExtra("width", 0), intent.getIntExtra("height", 0), intent.getIntExtra("mines", 0));
        /*for (int i = 0; i < ; i++) {
            boolean temp = game.generateMine(random.nextInt(8), random.nextInt(8));
            if (temp) {
                total++;
            }
        }*/
        total = game.getCount();
        setTitle("Sweeper");
        gameContainer = findViewById(R.id.game_container);
        table = findViewById(R.id.table);
        minesLeft = findViewById(R.id.mines_left);

        findViewById(R.id.new_game_button).setOnClickListener(v -> {
            game = new TableAlpha(game);
            setupTable();
            updateDisplay();
        });
        findViewById(R.id.back_setup_button).setOnClickListener(v -> {
            Intent setupIntent = new Intent(this, SetupActivity.class);
            startActivity(setupIntent);
            finish();
        });
        final ViewTreeObserver viewTreeObserver = gameContainer.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                gameContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                onReadyForSizing();
            }
        });




    }
    /**
     *
     */
    void onReadyForSizing() {
        Log.i(TAG, String.format("Layout is %d by %d", gameContainer.getWidth(), gameContainer.getHeight()));
        int bottomSpace = findViewById(R.id.game_control_container).getBottom() - findViewById(R.id.label_container).getTop();
        double horizontalConstraint = gameContainer.getWidth() / (double) game.getWidth();
        double verticalConstraint = (gameContainer.getHeight() - bottomSpace) / (double) game.getHeight();
        int slotSize = (int) Math.floor(Math.min(horizontalConstraint, verticalConstraint));
        Log.i(TAG, String.format("Slotss are %d pixels", slotSize));
        int slotBorderWidth = (int) (slotSize * SLOT_BORDER_WIDTH_MULTIPLIER);

        emptySlot = new GradientDrawable();
        emptySlot.setShape(GradientDrawable.RECTANGLE);
        emptySlot.setSize(slotSize, slotSize);
        emptySlot.setColor(Color.YELLOW);
        emptySlot.setStroke(slotBorderWidth, Color.BLACK);

        locked = new GradientDrawable();
        locked.setShape(GradientDrawable.RECTANGLE);
        locked.setSize(slotSize, slotSize);
        locked.setColor(Color.RED);
        locked.setStroke(slotBorderWidth, Color.BLACK);

        undetected = new GradientDrawable();
        undetected.setShape(GradientDrawable.RECTANGLE);
        undetected.setSize(slotSize, slotSize);
        undetected.setColor(Color.WHITE);
        undetected.setStroke(slotBorderWidth, Color.BLACK);

        mine = new GradientDrawable();
        mine.setShape(GradientDrawable.RECTANGLE);
        mine.setSize(slotSize, slotSize);
        mine.setColor(Color.BLACK);
        mine.setStroke(slotBorderWidth, Color.BLACK);


        setupTable();
        updateDisplay();

    }
    /**
     *
     */
    void setupTable() {
        Log.i(TAG, "setupTable");

        table.removeAllViews();
        findViewById(R.id.failure).setVisibility(View.GONE);
        findViewById(R.id.victory).setVisibility(View.GONE);

        table.setColumnCount(game.getWidth());
        table.setRowCount(game.getHeight());

        slots = new View[game.getWidth()][game.getHeight()];
        ((TextView) findViewById(R.id.mines_left)).setText(game.getCount());

        for (int x = 0; x < game.getWidth(); x++) {
            for (int y = 0; y < game.getHeight(); y++) {

                GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
                layoutParams.columnSpec = GridLayout.spec(x);
                layoutParams.rowSpec = GridLayout.spec(y);

                ImageView cell = new ImageView(this);
                cell.setImageDrawable(undetected);
                cell.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                table.addView(cell, layoutParams);

                final int gameX = x;
                final int gameY = game.getHeight() - y - 1;
                findViewById(R.id.to_mark).setOnClickListener(v -> {
                    toMark = true;
                });
                cell.setOnClickListener(v -> {
                    if (toMark) {
                        slotLongClicked(gameX, gameY);
                    } else {
                        slotClicked(gameX, gameY);
                    }
                });
                slots[gameX][gameY] = cell;
            }
        }


    }
    /**
     * To lock the slot clicked.
     * @param x the X coordinate position.
     * @param y the Y coordinate position.
     */
    void slotLongClicked(final int x, final int y) {
        boolean b = game.changeLock(x, y);
        updateDisplay();
    }

    /**
     * To open the slot clicked if not locked.
     * @param x the X coordinate position.
     * @param y the Y coordinate position.
     */
    void slotClicked(final int x, final int y) {
        if (!game.getLock(x, y) && !game.getDetect(x, y)) {
            int count = game.getSlot(x, y).countAround();
            game.detect(x, y);
            if (count == 0) {
                for (SlotAlpha single: game.getSlot(x, y).getAround()) {
                    slotClicked(single.getX(), single.getY());
                    updateDisplay();
                    winOrLose();
                }
            }
            updateDisplay();
            winOrLose();
        }
    }
    /**
     * To update display.
     */
    protected void updateDisplay() {
        boolean boomed = false;
        int lock = 0;
        for (int x = 0; x < game.getWidth(); x++) {
            for (int y = 0; y < game.getHeight(); y++) {
                SlotAlpha slot = game.getSlot(x, y);
                if (game.getLock(x, y)) {
                    ((ImageView) slots[x][y]).setImageDrawable(locked);
                    lock++;
                } else {
                    if (game.getDetect(x, y)) {
                        if (game.boomed(x, y)) {
                            boomed = true;
                            ((ImageView) slots[x][y]).setImageDrawable(mine);
                        } else {
                            if (game.getSlot(x, y).countAround() == 0) {
                                ((ImageView) slots[x][y]).setImageDrawable(emptySlot);
                            } else {
                                slots[x][y] = new TextView(this);
                                TextView textView = (TextView) slots[x][y];
                                textView.setText(game.getSlot(x, y).countAround());
                            /*int size = textView.getAutoSizeMaxTextSize();
                            textView.setAutoSizeTextTypeWithDefaults(size);*/
                                table.addView(textView, x, y);
                            }
                        }
                    }
                }
            }
        }
        if (lock > game.getCount()) {
            ((TextView) findViewById(R.id.mines_left)).setText(lock - game.getCount());
            ((TextView) findViewById(R.id.mines_left)).setTextColor(Color.RED);
        } else {
            ((TextView) findViewById(R.id.mines_left)).setText(game.getCount() - lock);
        }
        if (boomed) {
            lose = true;
        } else {
            int count = 0;
            for (int i = 0; i < game.getWidth(); i++) {
                for (int j = 0; j < game.getHeight(); j++) {
                    if (game.getDetect(i, j)) {
                        count++;
                    }
                }
            }
            if (count + game.getCount() == game.getWidth() * game.getHeight()) {
                win = true;
            }
        }
    }

    /**
     * To examine whether the game has ended.
     * @return true if game ended, either win or lose.
     */
    protected boolean winOrLose() {
        if (lose) {
            findViewById(R.id.failure).setVisibility(View.VISIBLE);
            return true;
        }
        if (win) {
            findViewById(R.id.victory).setVisibility(View.VISIBLE);
            return true;
        }
        return false;
    }
}