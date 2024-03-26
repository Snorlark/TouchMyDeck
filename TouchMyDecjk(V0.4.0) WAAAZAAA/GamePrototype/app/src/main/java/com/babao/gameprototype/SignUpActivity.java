package com.babao.gameprototype;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextNewUsername, editTextNewPassword;
    ImageView buttonSignUp;

    FirebaseDatabase database;
    DatabaseReference reference;
    MediaPlayer press_sfx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sign_up);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextNewUsername = findViewById(R.id.editTextNewUsername);
        editTextNewPassword = findViewById(R.id.editTextNewPassword);
        buttonSignUp = findViewById(R.id.buttonSignUp);

        press_sfx = MediaPlayer.create(getApplicationContext(), R.raw.press_sfx);
        press_sfx.setVolume(0.1f, 0.1f);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                press_sfx.start();
                String email = editTextEmail.getText().toString();
                String username = editTextNewUsername.getText().toString();
                String password = editTextNewPassword.getText().toString();

                database = FirebaseDatabase.getInstance();
                reference = database.getReference("Players");

                if (isValid(email, username, password)) {
                    PlayerAttributes identity = new PlayerAttributes(email, username, password);
                    reference.child(username).setValue(identity);

                    Toast.makeText(SignUpActivity.this, "Sign up successful!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in_splash, R.anim.fade_out_splash);
                    finish();
                } else {

                    Toast.makeText(SignUpActivity.this, "Invalid. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isValid(String email, String username, String password) {
        if (!email.endsWith("@students.national-u.edu.ph")) {
            Toast.makeText(SignUpActivity.this, "Please use an email ending with '@students.national-u.edu.ph'", Toast.LENGTH_SHORT).show();
            return false;
        }

        return !username.isEmpty() && !password.isEmpty();
    }
}