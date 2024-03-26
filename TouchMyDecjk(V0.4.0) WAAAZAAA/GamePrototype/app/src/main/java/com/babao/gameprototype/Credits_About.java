package com.babao.gameprototype;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class Credits_About extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_credits_about);

        imageView = findViewById(R.id.back);

        String username = getIntent().getStringExtra("username");
        int score = getIntent().getIntExtra("score", 0);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Credits_About.this, SettingsActivity.class);
                i.putExtra("username", username);
                i.putExtra("score", score);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in_splash, R.anim.fade_out_splash);
            }
        });
    }
}