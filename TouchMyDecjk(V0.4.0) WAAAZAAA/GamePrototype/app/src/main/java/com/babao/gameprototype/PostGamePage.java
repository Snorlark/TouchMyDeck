package com.babao.gameprototype;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import pl.droidsonroids.gif.GifImageView;

public class PostGamePage extends AppCompatActivity {

    TextView scoreTextView;
    ImageView playButton, leaderboardsButton, exitButton;
    MediaPlayer press_sfx, win_sfx, lose_sfx;
    GifImageView confetti;

    //Firebase
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_post_game_page);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Players");

        press_sfx = MediaPlayer.create(getApplicationContext(), R.raw.press_sfx);
        press_sfx.setVolume(0.1f, 0.1f);

        win_sfx = MediaPlayer.create(getApplicationContext(), R.raw.win_sfx);
        lose_sfx = MediaPlayer.create(getApplicationContext(), R.raw.lose_sfx);

        confetti = findViewById(R.id.confetti);

        scoreTextView = findViewById(R.id.scoreTextView);

        playButton = findViewById(R.id.playButton);
        leaderboardsButton = findViewById(R.id.leaderboards_button);
        exitButton = findViewById(R.id.exit_button);

        String username = getIntent().getStringExtra("username");
        int lives = getIntent().getIntExtra("score", 0);
        int score = getIntent().getIntExtra("score", 0);

        confetti.setVisibility(View.INVISIBLE);

        if (score >= 300) {
            win_sfx.start();

            confetti.setVisibility(View.VISIBLE);
        }
        else {
            lose_sfx.start();
        };

        scoreTextView.setText(String.valueOf(score));


        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                press_sfx.start();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("username", username);
                i.putExtra("score", score);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in_splash, R.anim.fade_out_splash);
            }
        });

        leaderboardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                press_sfx.start();

                Intent i = new Intent(getApplicationContext(), LeaderboardsActivity.class);
                i.putExtra("username", username);
                i.putExtra("score", score);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in_splash, R.anim.fade_out_splash);
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                press_sfx.start();
                finishAffinity();
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Close the activity
    }
}