package com.babao.gameprototype;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class HomePage_Music extends Service {
    MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.homepage_music);
        mediaPlayer.setVolume(0.1f, 0.1f);
        mediaPlayer.setLooping(true); // Set looping if needed
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer.start(); // Start the MediaPlayer
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop(); // Stop the MediaPlayer
        mediaPlayer.release(); // Release resources
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
