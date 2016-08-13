package com.kevinlee.reactiontimetester;

import android.media.Image;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private long currentTime;
    private long elapsedTime;
    boolean pressedEarly = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        Button restart_button = (Button) findViewById(R.id.restart_button);
        elapsedTime = System.currentTimeMillis() - currentTime;

        String message = "Good job! Your reaction time was " + String.valueOf(elapsedTime) + " milliseconds.";
        setFinalMessage(message);

        v.setClickable(false);
        restart_button.setVisibility(View.VISIBLE);
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

}
