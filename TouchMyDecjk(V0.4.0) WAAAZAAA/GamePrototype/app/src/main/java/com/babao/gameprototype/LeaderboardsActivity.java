package com.babao.gameprototype;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class LeaderboardsActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    TextView firstPlace_Podium, secondPlace_Podium, thirdPlace_Podium;
    MediaPlayer press_sfx;
    GifImageView present;
    Animation floatup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_leaderboards);

        floatup = AnimationUtils.loadAnimation(this, R.anim.present_slideup);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.leaderboards);

        present = findViewById(R.id.present);
        present.setVisibility(View.INVISIBLE);


        firstPlace_Podium = findViewById(R.id.firstPlace_Podium);
        secondPlace_Podium = findViewById(R.id.secondPlace_Podium);
        thirdPlace_Podium = findViewById(R.id.thirdPlace_Podium);

        String username = getIntent().getStringExtra("username");
        int score = getIntent().getIntExtra("score", 0);

        press_sfx = MediaPlayer.create(getApplicationContext(), R.raw.press_sfx);
        press_sfx.setVolume(0.1f, 0.1f);

        TextView scoreTextView = findViewById(R.id.leaderboards_scoreTextView);
        scoreTextView.setText("" + score + "");

        displayPlaces();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;

                if (item.getItemId() == R.id.leaderboards) {
                    intent = new Intent(getApplicationContext(), LeaderboardsActivity.class);
                    overridePendingTransition(0, 0);
                }
                else if (item.getItemId() == R.id.home) {
                    press_sfx.start();
                    intent = new Intent(getApplicationContext(), HomePage.class);
                    intent.putExtra("username", username);
                    intent.putExtra("score", score);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                }
                else if (item.getItemId() == R.id.settings) {
                    press_sfx.start();
                    intent = new Intent(getApplicationContext(), SettingsActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("score", score);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                } else {
                    return false;
                }

                // Add username extra to the intent (for all the buttons)
                intent.putExtra("username", username);
                intent.putExtra("score", score);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
        });

    }

    private void displayPlaces() {
        DatabaseReference playersRef = FirebaseDatabase.getInstance().getReference("Players");

        playersRef.orderByChild("score").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<PlayerAttributes> players = new ArrayList<>();

                for (DataSnapshot playerSnapshot : dataSnapshot.getChildren()) {
                    PlayerAttributes player = playerSnapshot.getValue(PlayerAttributes.class);
                    if (player != null) {
                        players.add(player);
                    }
                }

                Collections.sort(players, new Comparator<PlayerAttributes>() {
                    @Override
                    public int compare(PlayerAttributes p1, PlayerAttributes p2) {
                        if (p1.getScore() == p2.getScore()) {
                            // In case scores of players are the same, compare their lives.
                            return p2.getLives() - p1.getLives();
                        } else {
                            return p2.getScore() - p1.getScore();
                        }
                    }
                });

                for (int i = 0; i < Math.min(players.size(), 3); i++) {
                    PlayerAttributes player = players.get(i);
                    displayPlace(i + 1, player.getUsername(), player.getScore(), player.getLives());
                }

                // Check if the current player is in the top 3 positions
                String username = getIntent().getStringExtra("username");
                int score = getIntent().getIntExtra("score", 0);

                for (int i = 0; i < Math.min(players.size(), 3); i++) {
                    PlayerAttributes player = players.get(i);
                    if (player.getUsername().equals(username) && player.getScore() == score) {
                        // Current player is in the top 3
                        present.setVisibility(View.VISIBLE);
                        present.setAnimation(floatup);
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });
    }

    private void displayPlace(int place, String username, int score, int lives) {
        TextView placeTextView, usernameTextView, scoreTextView, livesTextView;
        String placeSuffix;
        switch (place) {
            case 1:
                placeSuffix = "st";
                placeTextView = findViewById(R.id.firstPlaceTextView);
                usernameTextView = findViewById(R.id.firstPlaceUsernameTextView);
                scoreTextView = findViewById(R.id.firstPlaceScoreTextView);
                livesTextView = findViewById(R.id.firstPlaceLivesTextView);
                firstPlace_Podium.setText(username);
                break;
            case 2:
                placeSuffix = "nd";
                placeTextView = findViewById(R.id.secondPlaceTextView);
                usernameTextView = findViewById(R.id.secondPlaceUsernameTextView);
                scoreTextView = findViewById(R.id.secondPlaceScoreTextView);
                livesTextView = findViewById(R.id.secondPlaceLivesTextView);
                secondPlace_Podium.setText(username);
                break;
            case 3:
                placeSuffix = "rd";
                placeTextView = findViewById(R.id.thirdPlaceTextView);
                usernameTextView = findViewById(R.id.thirdPlaceUsernameTextView);
                scoreTextView = findViewById(R.id.thirdPlaceScoreTextView);
                livesTextView = findViewById(R.id.thirdPlaceLivesTextView);
                thirdPlace_Podium.setText(username);
                break;
            default:
                return;
        }

        placeTextView.setText(place + placeSuffix);
        usernameTextView.setText("" + username + "");
        scoreTextView.setText("" + score + "");
        livesTextView.setText("" + lives + "");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

}
