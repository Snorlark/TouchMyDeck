package com.babao.gameprototype;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class TutorialPage extends AppCompatActivity {

    TextView skip;
    MediaPlayer press_sfx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_tutorial_page);

        press_sfx = MediaPlayer.create(getApplicationContext(), R.raw.press_sfx);
        press_sfx.setVolume(0.1f, 0.1f);

        skip = findViewById(R.id.skip);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                press_sfx.start();
                String username = getIntent().getStringExtra("username");
                if (username != null) {
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    i.putExtra("username", username);
                    startActivity(i);
                    overridePendingTransition(R.anim.fade_in_splash, R.anim.fade_out_splash);
                }
            }
        });
    }
}