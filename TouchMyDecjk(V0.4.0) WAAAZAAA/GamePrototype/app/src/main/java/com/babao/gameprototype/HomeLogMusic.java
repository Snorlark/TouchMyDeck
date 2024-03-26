package com.babao.gameprototype;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class HomeLogMusic extends Service {

    MediaPlayer mediaPlayer1;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer1 = MediaPlayer.create(this, R.raw.homelog_music);
        mediaPlayer1.setVolume(0.1f, 0.1f);
        mediaPlayer1.setLooping(true); // Set looping if needed
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer1.start(); // Start the MediaPlayer
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer1.stop(); // Stop the MediaPlayer
        mediaPlayer1.release(); // Release resources
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
