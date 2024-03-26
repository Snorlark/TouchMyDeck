package com.babao.gameprototype;

import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.os.Vibrator;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    ImageView cardImageView, nextCardImageView, chip1, chip2, chip3, curtain_left,
            curtain_right, curtain_top, pokerButton, jokerButton;
    TextView timerTextView, scoreTextView, countdown_text;
    ArrayList<Integer> cards;
    Handler handler;
    MediaPlayer correct_sound, wrong_sound, main_game_music, countdown_music, countdown_sfx;
    Animation slide_left, slide_right, slide_top, fade;

    int currentCardIndex = 0;
    int lives = 3;
    int score = 0;
    CountDownTimer timer;
    Animation jokerAnimation, pokerAnimation, shakeAnimation;
    Boolean isCountdownActive = true;

    // Firebase
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        // Initialize FirebaseAuth and DatabaseReference
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Players");

        correct_sound = MediaPlayer.create(getApplicationContext(), R.raw.correct_sfx);
        wrong_sound = MediaPlayer.create(getApplicationContext(), R.raw.wrong_sfx);
        main_game_music = MediaPlayer.create(getApplicationContext(), R.raw.main_game_music);
        countdown_music = MediaPlayer.create(getApplicationContext(), R.raw.countdown_music);
        countdown_sfx = MediaPlayer.create(getApplicationContext(), R.raw.countdown_sfx);

        main_game_music.setVolume(0.09f, 0.09f);

        slide_left = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
        slide_right = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);
        slide_top = AnimationUtils.loadAnimation(this, R.anim.slide_out_top);
        fade = AnimationUtils.loadAnimation(this, R.anim.fade_out_splash);

        // Initialize views
        cardImageView = findViewById(R.id.cardImageView);
        nextCardImageView = findViewById(R.id.nextCardImageView);
        chip1 = findViewById(R.id.chip1);
        chip2 = findViewById(R.id.chip2);
        chip3 = findViewById(R.id.chip3);

        pokerButton = findViewById(R.id.pokerButton);
        jokerButton = findViewById(R.id.jokerButton);

        timerTextView = findViewById(R.id.timerTextView);
        scoreTextView = findViewById(R.id.scoreTextView);

        curtain_left = findViewById(R.id.curtain_left);
        curtain_right = findViewById(R.id.curtain_right);
        curtain_top = findViewById(R.id.curtain_top);

        countdown_text = findViewById(R.id.countdown_text);

        Intent serviceIntent = new Intent(this, HomePage_Music.class);
        stopService(serviceIntent);

        // Check if the username is provided
        String username = getIntent().getStringExtra("username");
        if (username != null) {
            // Username is provided, no need to handle login again
            // You can proceed with the rest of your MainActivity initialization
        } else {
            // Username is not provided, handle the login process
            // For example, you can start the LoginActivity to get the username
        }

        cards = new ArrayList<>();
        cards.add(R.drawable.card_j);
        cards.add(R.drawable.card_ck);
        cards.add(R.drawable.card_cq);
        cards.add(R.drawable.card_dk);
        cards.add(R.drawable.card_dq);
        cards.add(R.drawable.card_hk);
        cards.add(R.drawable.card_hq);
        cards.add(R.drawable.card_sk);
        cards.add(R.drawable.card_sq);

        for (int i = 0; i < 90; i++) {
            cards.add(R.drawable.card_j);
        }

        // Add 20 each of the other cards
        int[] otherCards = {
                R.drawable.card_ck, R.drawable.card_cq, R.drawable.card_dk, R.drawable.card_dq,
                R.drawable.card_hk, R.drawable.card_hq, R.drawable.card_sk, R.drawable.card_sq };

        for (int card : otherCards) {
            for (int i = 0; i < 20; i++) {
                cards.add(card);
            }
        }

        Collections.shuffle(cards); //To randomize the cards in the deck

        jokerAnimation = AnimationUtils.loadAnimation(this, R.anim.joker_animation);
        pokerAnimation = AnimationUtils.loadAnimation(this, R.anim.poker_animation);
        shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake_animation);

        updateLives();
        updateScore();
        countdown();

        updateCard();

        chip1.setVisibility(View.VISIBLE);
        chip2.setVisibility(View.VISIBLE);
        chip3.setVisibility(View.VISIBLE);

        pokerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if buttons can be clicked (not during countdown or game over)
                if (!isCountdownActive) {
                    checkButton(false);
                }
            }
        });

        jokerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if buttons can be clicked (not during countdown or game over)
                if (!isCountdownActive) {
                    checkButton(true);
                }
            }
        });
    }


    private void checkButton(boolean isJoker) {
        if ((isJoker && cards.get(currentCardIndex) == R.drawable.card_j) ||  (!isJoker && cards.get(currentCardIndex) != R.drawable.card_j)) {
            correct_sound.start();
            score += 10;
            updateScore(); // Updates score when pressed the correct button
            updateNextCard();
//          updateScoreAndLivesInDatabase(score, lives); // Update score and lives in Firebase database
            cardImageView.startAnimation(isJoker ? jokerAnimation : pokerAnimation); //Animation if joker/poker
            currentCardIndex++; // Moves to the next card
        }
        else {
            wrong_sound.start();
            lives--; //Decrement one life
            updateLives();

            cardImageView.startAnimation(shakeAnimation); //card shakes (shakira shakira) when wrong
            nextCardImageView.startAnimation(shakeAnimation);
            vibrate();

            if (lives == 0 || currentCardIndex == cards.size() - 1) {
                endGame();
                return;
            }
        }
        if (currentCardIndex < cards.size()) {
            updateCard(); // Update the displayed card
        }
        else {
            endGame();
        }
//        Update score and lives in Firebase database after each button click
//        updateScoreAndLivesInDatabase(score, lives);
    }


    private void updateNextCard() {
        if (currentCardIndex < cards.size() - 1) {
            int nextCard = cards.get(currentCardIndex + 1);
            nextCardImageView.setImageResource(nextCard);
        }
        else {
            // No more cards remaining
            nextCardImageView.setVisibility(View.GONE);
        }
    }

    private void vibrate() {
        // Get the vibrator service
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        // Check if the device has a vibrator
        if (vibrator != null && vibrator.hasVibrator()) {
            // Vibrate for 500 milliseconds
            vibrator.vibrate(500);
        }
    }

    private void updateCard() {
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                cardImageView.setImageResource(cards.get(currentCardIndex));
            }
        }, 400);
    }



    private void updateLives() {
        for (int i = 0; i < 3; i++) {
            int visibility = (i < lives) ? View.VISIBLE : View.INVISIBLE;

            switch (i) {
                case 0:
                    chip1.setVisibility(visibility);
                    break;
                case 1:
                    chip2.setVisibility(visibility);
                    break;
                case 2:
                    chip3.setVisibility(visibility);
                    break;
            }
        }
    }

    private void updateScore() {
        scoreTextView.setText(String.valueOf(score));
    }


    //Timer for the game

    private void countdown() {
        countdown_music.start();
        timerTextView.setText("3");

        pokerButton.setEnabled(false);
        jokerButton.setEnabled(false);

        countdown_text.setVisibility(View.VISIBLE);
        curtain_left.setVisibility(View.VISIBLE);
        curtain_right.setVisibility(View.VISIBLE);
        curtain_top.setVisibility(View.VISIBLE);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            int countdownSeconds = 3;

            @Override
            public void run() {
                if (countdownSeconds > 0) {
                    countdown_text.setText(String.valueOf(countdownSeconds));
                    timerTextView.setText(String.valueOf(countdownSeconds));
                    countdownSeconds--;
                    handler.postDelayed(this, 1000);
                }
                else {
                    countdown_sfx.start();

                    curtain_left.startAnimation(slide_left);
                    curtain_right.startAnimation(slide_right);
                    curtain_top.startAnimation(slide_top);
                    countdown_text.startAnimation(fade);

                    countdown_text.setVisibility(View.GONE);
                    curtain_left.setVisibility(View.GONE);
                    curtain_right.setVisibility(View.GONE);
                    curtain_top.setVisibility(View.GONE);

                    isCountdownActive = false;
                    main_game_music.seekTo(0);
                    main_game_music.start();
                    main_game_music.setLooping(true);// Countdown has ended, buttons can be clicked
                    startGameTimer();
                }
            }
        }, 1000);
    }

    // Start the actual game timer
    private void startGameTimer() {
        pokerButton.setEnabled(true);
        jokerButton.setEnabled(true);

        timer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerTextView.setText("" + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                endGame();
            }
        }.start();
    }

    //To end the game
    private void endGame() {
        main_game_music.pause();
        // Display congratulatory message or game over message
        pokerButton.setEnabled(false);
        jokerButton.setEnabled(false);

        // Cancel the game timer
        if (timer != null) {
            timer.cancel();
        }

        updateScoreAndLivesInDatabase(score, lives);

        // Update score and lives in Firebase database
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startLeaderboardsActivity();
            }
       }, 1000);
    }


    // Start the leaderboards activity



    //To reset the game
    private void resetGame() {
        // Retrieve the highest score and remaining lives passed from LeaderboardsActivity
        int highestScore = getIntent().getIntExtra("highestScore", 0);
        int remainingLives = getIntent().getIntExtra("remainingLives", 3);

        pokerButton.setEnabled(false);
        jokerButton.setEnabled(false);
        currentCardIndex = 0;

        lives = remainingLives;
        score = highestScore;

        updateLives();
        updateScore();
        countdown();
        Collections.shuffle(cards);
        updateCard();
    }

    private void startLeaderboardsActivity() {
        Intent intent = new Intent(MainActivity.this, PostGamePage.class);
        String username = getIntent().getStringExtra("username");
        Log.d("MainActivity", "Username passed to PostGamePage" + username);
        intent.putExtra("score", score);
        intent.putExtra("lives", lives);
        intent.putExtra("username", getIntent().getStringExtra("username")); // Pass the username to LeaderboardsActivity
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in_splash, R.anim.fade_out_splash);
        finish(); // Optional: finish the current activity
    }

    private void updateScoreAndLivesInDatabase(final int newScore, final int newLives) {
        String username = getIntent().getStringExtra("username");
        if (username != null) {
            final DatabaseReference userRef = reference.child(username);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Get the existing score and lives from the database
                        int currentScore = dataSnapshot.child("score").getValue(Integer.class);
                        int currentLives = dataSnapshot.child("lives").getValue(Integer.class);

                        // Check if the new score is equal to the existing score
                        if (newScore == currentScore) {
                            // Check if the new lives is higher than the existing lives
                            if (newLives > currentLives) {
                                // Update the lives in the database
                                userRef.child("lives").setValue(newLives);
                                Toast.makeText(MainActivity.this, "Lives updated successfully!", Toast.LENGTH_SHORT).show();
                            }
                        } else if (newScore > currentScore) {
                            // Update the score and lives in the database
                            userRef.child("score").setValue(newScore);
                            userRef.child("lives").setValue(newLives);
                            Toast.makeText(MainActivity.this, "Score and lives updated successfully!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // If the user data doesn't exist, create new data with the new score and lives
                        userRef.child("score").setValue(newScore);
                        userRef.child("lives").setValue(newLives);
                        Toast.makeText(MainActivity.this, "Score and lives saved for the first time!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(MainActivity.this, "Failed to update score and lives: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    //wag i-delete basta gumagana yan HAHHAHAHHA -snorlark
    @Override
    public void onBackPressed() {
        // Close the activity
    }

}


// Method to update score in Firebase database
//    private void updateScoreInDatabase(int newScore) {
//        // Get the logged-in username from the intent
//        String username = getIntent().getStringExtra("username");
//        if (username != null) {
//            // Get a reference to the database node where you want to store the score for the logged-in user
//            DatabaseReference userRef = reference.child(username);
//
//            // Set the new score value in the database
//            userRef.child("score").setValue(newScore)
//                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            // Score updated successfully
//                            Toast.makeText(MainActivity.this, "Score updated successfully!", Toast.LENGTH_SHORT).show();
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            // Score update failed
//                            Toast.makeText(MainActivity.this, "Failed to update score: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        }
//    }
