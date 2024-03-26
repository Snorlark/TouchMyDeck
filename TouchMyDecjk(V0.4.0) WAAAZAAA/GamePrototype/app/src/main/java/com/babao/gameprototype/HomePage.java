package com.babao.gameprototype;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomePage extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ImageView playButton, tutorialButton;
    MediaPlayer press_sfx;

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home_page);

        playButton = findViewById(R.id.playButton);
        tutorialButton = findViewById(R.id.tutorialButton);

        press_sfx = MediaPlayer.create(getApplicationContext(), R.raw.press_sfx);
        press_sfx.setVolume(0.1f, 0.1f);

        Intent i = new Intent(this, HomePage_Music.class);
        startService(i);

        Intent serviceIntent = new Intent(this, HomeLogMusic.class);
        stopService(serviceIntent);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        username = getIntent().getStringExtra("username");
        int score = getIntent().getIntExtra("score", 0);


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.leaderboards) {
                    press_sfx.start();
                    Intent intent = new Intent(getApplicationContext(), LeaderboardsActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("score", score);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    return true;
                }
                else if (item.getItemId() == R.id.home) {
                    press_sfx.start();
                    Intent intent = new Intent(getApplicationContext(), HomePage.class);
                    intent.putExtra("username", username);
                    intent.putExtra("score", score);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    return true;
                }

                else if (item.getItemId() == R.id.settings) {
                    press_sfx.start();
                    Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("score", score);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    return true;
                }
                return false;
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                press_sfx.start();
                String username = getIntent().getStringExtra("username");
                if (username != null) {
                    Intent i = new Intent(getApplicationContext(), TutorialPage.class);
                    i.putExtra("username", username);
                    startActivity(i);
                    overridePendingTransition(R.anim.fade_in_splash, R.anim.fade_out_splash);
                }
            }
        });

        tutorialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                press_sfx.start();
                String username = getIntent().getStringExtra("username");
                if (username != null) {
                    Intent i = new Intent(getApplicationContext(), TutorialHome.class);
                    i.putExtra("username", username);
                    startActivity(i);
                    overridePendingTransition(R.anim.fade_in_splash, R.anim.fade_out_splash);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}