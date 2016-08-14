package com.kevinlee.reactiontimetester;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    boolean pressedEarly = false;
    private long currentTime;
    private long elapsedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayHighScore();
    }

    /**
     * Starts the test with the press of the red button. Between a random time between 2000 - 5000
     * milliseconds, the red button will turn green, and then the user must press it. If the user
     * presses the button too early, then a message will display signaling that the user pressed
     * too early, and a button will appear to try again.
     *
     * @param v the red buttton
     */
    public void startTest(View v) {
        final ImageView redButton = (ImageView) findViewById(R.id.redButton);
        final ImageView greenButton = (ImageView) findViewById(R.id.greenButton);

        redButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setFinalMessage("You pressed too early! Please try again.");
                setPressedEarly();
                v.setClickable(false);
                Button restart_button = (Button) findViewById(R.id.restart_button);
                restart_button.setVisibility(View.VISIBLE);
            }
        });

        Handler handler = new Handler();
        Random r = new Random();
        int millis = r.nextInt(3000) + 2000;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!pressedEarly) {
                    currentTime = System.currentTimeMillis();
                    redButton.setVisibility(View.INVISIBLE);
                    greenButton.setVisibility(View.VISIBLE);
                }
            }
        }, millis);
    }

    /**
     * On a successful click of the green button, the method triggers, giving the reaction time.
     *
     * @param v the green button
     */
    public void reactionClick(View v) {
        elapsedTime = System.currentTimeMillis() - currentTime;
        Button restart_button = (Button) findViewById(R.id.restart_button);


        String message = "Good job! Your reaction time was " + String.valueOf(elapsedTime) + " milliseconds.";
        setFinalMessage(message);

        v.setClickable(false);
        restart_button.setVisibility(View.VISIBLE);

        int highScore = getHighScore();
        if (elapsedTime < highScore) {
            setHighScore((int) elapsedTime);
            displayHighScore();
        }

    }

    /**
     * Sets the message to the bottom of the screen.
     *
     * @param message to be set
     */
    private void setFinalMessage(String message) {
        TextView results = (TextView) findViewById(R.id.results);
        results.setText(message);
        results.setVisibility(View.VISIBLE);
    }

    /**
     * Resets the program entirely to its initial layout so that when the user presses the red
     * button again, startTest will begin.
     *
     * @param v the restart button
     */
    public void restart(View v) {
        ImageView red = (ImageView) findViewById(R.id.redButton);
        ImageView green = (ImageView) findViewById(R.id.greenButton);
        pressedEarly = false;

        v.setVisibility(View.INVISIBLE);
        setFinalMessage("");

        red.setVisibility(View.VISIBLE);
        red.setClickable(true);
        red.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startTest(v);
            }
        });

        green.setVisibility(View.INVISIBLE);
        green.setClickable(true);
    }

    /**
     * Method to set the boolean if the red button was pressed early.
     */
    private void setPressedEarly() {
        pressedEarly = true;
    }

    /**
     * Displays the high score.
     */
    private void displayHighScore() {
        TextView highScore = (TextView) findViewById(R.id.high_score_text_view);
        highScore.setText(getString(R.string.high_score, getHighScore()));
    }

    /**
     * Gets the high score.
     *
     * @return int high score
     */
    private int getHighScore() {
        SharedPreferences prefs = this.getSharedPreferences("high_scores", Context.MODE_PRIVATE);
        return prefs.getInt("highScore", 50000);
    }

    /**
     * sets the high score.
     *
     * @param score the high score
     */
    private void setHighScore(int score) {
        SharedPreferences prefs = this.getSharedPreferences("high_scores", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("highScore", score);
        editor.apply();
    }

}
