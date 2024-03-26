package com.babao.gameprototype;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class SettingsActivity extends AppCompatActivity {

    TextView textView;
    ImageView logout;
    BottomNavigationView bottomNavigationView;
    MediaPlayer press_sfx;
    SeekBar seekbar;
    AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_settings);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.settings);

        seekbar = findViewById(R.id.seekbar);
        audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        seekbar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        seekbar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

        textView = findViewById(R.id.textView2);
        logout = findViewById(R.id.logoutButton);

        press_sfx = MediaPlayer.create(getApplicationContext(), R.raw.press_sfx);
        press_sfx.setVolume(0.1f, 0.1f);

        String username = getIntent().getStringExtra("username");
        int score = getIntent().getIntExtra("score", 0);

        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        // Set the maximum volume of the SeekBar to the maximum volume of the MediaPlayer:
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        seekbar.setMax(maxVolume);

        // Set the current volume of the SeekBar to the current volume of the MediaPlayer:
        int currVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        seekbar.setProgress(currVolume);

        TextView usernameTextView = findViewById(R.id.username_textView);
        usernameTextView.setText("ID: " + username);

        // Add a SeekBar.OnSeekBarChangeListener to the SeekBar:
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do Nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Do Nothing
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingsActivity.this, Credits_About.class);
                i.putExtra("username", username);
                i.putExtra("score", score);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in_splash, R.anim.fade_out_splash);
            }
        });

        logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent serviceIntent = new Intent(SettingsActivity.this, HomePage_Music.class);
                stopService(serviceIntent);
                Intent i = new Intent(SettingsActivity.this, HomeLogActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in_splash, R.anim.fade_out_splash);
            }
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                if (item.getItemId() == R.id.leaderboards) {
                    press_sfx.start();
                    intent = new Intent(getApplicationContext(), LeaderboardsActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("score", score);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    return true;
                }
                else if (item.getItemId() == R.id.home) {
                    press_sfx.start();
                    intent = new Intent(getApplicationContext(), HomePage.class);
                    intent.putExtra("username", username);
                    intent.putExtra("score", score);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    return true;
                }
                else if (item.getItemId() == R.id.settings){
                    press_sfx.start();
                    intent = new Intent(getApplicationContext(), SettingsActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("score", score);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    return true;
                }

                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}