package com.babao.gameprototype;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

public class HomeLogActivity extends AppCompatActivity {

    RelativeLayout splash_title;
    ImageView title_tmd, buttonLogin, buttonSignUp, tmd_title, img_leftCurt, img_rightCurt, img_topCurt,
            jack, textbox3;
    Handler handler;
    Animation fadein, fadeout, slide_left, slide_right, slide_top;
    MediaPlayer press_sfx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home_log);

        press_sfx = MediaPlayer.create(getApplicationContext(), R.raw.press_sfx);
        press_sfx.setVolume(0.1f, 0.1f);

        splash_title = findViewById(R.id.splash_title);
        title_tmd = findViewById(R.id.title_tmd);

        tmd_title = findViewById(R.id.tmd_title);
        tmd_title.setVisibility(View.VISIBLE);

        img_leftCurt = findViewById(R.id.img_leftCurt);
        img_leftCurt.setVisibility(View.VISIBLE);

        img_rightCurt = findViewById(R.id.img_rightCurt);
        img_rightCurt.setVisibility(View.VISIBLE);

        img_topCurt = findViewById(R.id.img_topCurt);
        img_topCurt.setVisibility(View.VISIBLE);

        jack = findViewById(R.id.jack);
        jack.setVisibility(View.VISIBLE);

        textbox3 = findViewById(R.id.textbox3);
        textbox3.setVisibility(View.VISIBLE);

        buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setVisibility(View.VISIBLE);

        buttonSignUp = findViewById(R.id.buttonSignUp);
        buttonSignUp.setVisibility(View.VISIBLE);

        fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in_splash);
        fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out_splash);
        slide_left = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
        slide_right = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);
        slide_top = AnimationUtils.loadAnimation(this, R.anim.slide_out_top);

        title_tmd.setAnimation(fadein);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                splash_title.setAnimation(slide_top);
                splash_title.setVisibility(View.GONE);
            }
        }, 2000);

        Intent serviceIntent = new Intent(this, HomeLogMusic.class);
        startService(serviceIntent);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                press_sfx.start();
                curtainAnimation();

                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(HomeLogActivity.this, LoginActivity.class));
                        overridePendingTransition(0,0);
                    }
                }, 1000);
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                press_sfx.start();
                curtainAnimation();

                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(HomeLogActivity.this, SignUpActivity.class));
                        overridePendingTransition(0,0);
                    }
                }, 1000);
            }
        });
    }

    private void curtainAnimation() {
        tmd_title.setAnimation(fadeout);
        tmd_title.setVisibility(View.GONE);

        textbox3.setAnimation(fadeout);
        textbox3.setVisibility(View.GONE);

        jack.setAnimation(fadeout);
        jack.setVisibility(View.GONE);

        buttonLogin.setAnimation(fadeout);
        buttonLogin.setVisibility(View.GONE);

        buttonSignUp.setAnimation(fadeout);
        buttonSignUp.setVisibility(View.GONE);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                img_topCurt.setAnimation(slide_top);
                img_topCurt.setVisibility(View.GONE);

                img_leftCurt.setAnimation(slide_left);
                img_leftCurt.setVisibility(View.GONE);

                img_rightCurt.setAnimation(slide_right);
                img_rightCurt.setVisibility(View.GONE);
            }
        }, 600);
    }

    @Override
    protected void onResume() {
        super.onResume();

        tmd_title.setVisibility(View.VISIBLE);
        textbox3.setVisibility(View.VISIBLE);
        jack.setVisibility(View.VISIBLE);
        buttonLogin.setVisibility(View.VISIBLE);
        buttonSignUp.setVisibility(View.VISIBLE);
        img_leftCurt.setVisibility(View.VISIBLE);
        img_rightCurt.setVisibility(View.VISIBLE);
        img_topCurt.setVisibility(View.VISIBLE);
    }
}