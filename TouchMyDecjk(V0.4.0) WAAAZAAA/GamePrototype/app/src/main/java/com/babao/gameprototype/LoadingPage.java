package com.babao.gameprototype;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoadingPage extends AppCompatActivity {

    Animation fadein, fadeout;
    Handler handler;
    ImageView jack_icon, star, stand;
    TextView gameby, amanpulo;
    MediaPlayer media;
    Boolean flip = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_loading_page);

        jack_icon = findViewById(R.id.jack_icon);
        star = findViewById(R.id.star);
        stand = findViewById(R.id.stand);
        gameby = findViewById(R.id.gameby);
        amanpulo = findViewById(R.id.amanpulo);

        media = MediaPlayer.create(this, R.raw.splash_music);
        media.start();

        fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in_splash);
        fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out_splash);

        gameby.setAlpha(0f);
        amanpulo.setAlpha(0f);
        star.setAlpha(0f);

        stand.startAnimation(fadein);
        jack_icon.setAnimation(fadein);
        startRotationAnimation();

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                flip = false;

                gameby.animate().alpha(1f).setDuration(1000); // Fade in gameby
                amanpulo.animate().alpha(1f).setDuration(1000); // Fade in amanpulo
                star.animate().alpha(1f).setDuration(1000); // Fade in star

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        jack_icon.clearAnimation(); // Stop the animation

                        gameby.setAnimation(fadeout);
                        amanpulo.setAnimation(fadeout);
                        jack_icon.setAnimation(fadeout);
                        star.setAnimation(fadeout);
                        stand.setAnimation(fadeout);

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Start the next activity after stopping the animation
                                Intent i = new Intent(LoadingPage.this, HomeLogActivity.class);
                                startActivity(i);
                                finish();
                                overridePendingTransition(0, 0);
                            }
                        }, 500);

                    }
                }, 2500);
            }
        }, 2000);
    }

    private void startRotationAnimation() {
        jack_icon.animate().rotationY(360).setDuration(2000) // Set initial flip
                .setInterpolator(AnimationUtils.loadInterpolator(LoadingPage.this, android.R.anim.linear_interpolator))
                .setStartDelay(0)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {

                        if (flip) {
                            // Reset rotation to 0 degrees
                            jack_icon.setRotationY(0);

                            // Start the next flip immediately
                            startRotationAnimation();

                        }
                    }
                })
                .start();
    }
}
