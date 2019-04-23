package edu.illinois.cs.cs125.spring2019.mp2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import edu.illinois.cs.cs125.spring2019.mp2.lib.TableAlpha;

/**
 * The initial activity that configures the game.
 * <p>
 * MP2 provides our first example of an app with <em>multiple</em> Activitys, each representing a different screen.
 * When the app launches we open the SetupActivity Activity, which solicits initial game configuration parameters
 * from the user. It then launches the GameActivity which is responsible for soliciting moves and rendering the board
 * as game play proceeds.
 *
 * @see <a href="https://cs125.cs.illinois.edu/MP/2/">MP2 Documentation</a>
 */
public class SetupActivity extends AppCompatActivity {
    /**
     *
     */
    private static final String TAG = "ProtoSweeperAlpha:Setup";

    /**
     * Oncreate.
     * @param savedInstanceState the saved instance state.
     */
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setup);
        setTitle("Set up Sweeper");

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) { }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) { }

            @Override
            public void afterTextChanged(final Editable s) {
                Log.i(TAG, "Text changed");
                Button startButton = findViewById(R.id.start_button);
                TextView errorNotice = findViewById(R.id.invalid_setup_label);
                if (validSetup()) {
                    startButton.setEnabled(true);
                    errorNotice.setVisibility((View.GONE));
                } else {
                    startButton.setEnabled(false);
                    errorNotice.setVisibility(View.VISIBLE);
                }
            }
        };

        for (int id : new int[]{R.id.edit_width, R.id.edit_height, R.id.edit_mines}) {
            ((EditText) findViewById(id)).addTextChangedListener(textWatcher);
        }

        findViewById(R.id.start_button).setOnClickListener(v -> startGame());

    }
    /**
     * To get the text in editable text view.
     */
    String getTextIn(final int editor) {
        return ((EditText) findViewById(editor)).getText().toString();
    }
    /**
     * To get integer in editable text view.
     */
    private int getNumberIn(final int editor) {
        return Integer.parseInt(getTextIn(editor));
    }
    /**
     * To test whether the setup is valid.
     * @return true if the setup is valid.
     */
    boolean validSetup() {
        Log.i(TAG, "Testing setup validity");
        try {
            int width = getNumberIn(R.id.edit_width);
            int height = getNumberIn(R.id.edit_height);
            int mines = getNumberIn(R.id.edit_mines);

            return TableAlpha.create(width, height, mines) != null;
        } catch (Exception e) {
            return false;
        }
    }
    /**
     * To start game.
     */
    void startGame() {
        Intent intent = new Intent(this, GameActivity.class);

        intent.putExtra("width", getNumberIn(R.id.edit_width));
        intent.putExtra("height", getNumberIn(R.id.edit_height));
        intent.putExtra("mines", getNumberIn(R.id.edit_mines));

        startActivity(intent);

        finish();

    }

}