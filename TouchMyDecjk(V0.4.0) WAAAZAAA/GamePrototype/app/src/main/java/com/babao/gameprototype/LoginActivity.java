package com.babao.gameprototype;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    ImageView buttonLogin;
    FirebaseDatabase database;
    DatabaseReference reference;
    MediaPlayer press_sfx;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        // Initialize views
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        press_sfx = MediaPlayer.create(getApplicationContext(), R.raw.press_sfx);
        press_sfx.setVolume(0.1f, 0.1f);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                press_sfx.start();
                final String userName = editTextUsername.getText().toString();
                final String password = editTextPassword.getText().toString();

                if (userName.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter your Username", Toast.LENGTH_SHORT).show();
                    return;
                } else if (password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter your Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("Players");

                reference.child(userName).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            final String getPassword = dataSnapshot.child("password").getValue(String.class);

                            if (getPassword.equals(password)) {
                                Toast.makeText(LoginActivity.this, "Successful log in", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, HomePage.class);
                                intent.putExtra("username", userName);
                                intent.putExtra("password", password);
                                startActivity(intent);
                                overridePendingTransition(R.anim.fade_in_splash, R.anim.fade_out_splash);
                            } else {
                                Toast.makeText(LoginActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(LoginActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
