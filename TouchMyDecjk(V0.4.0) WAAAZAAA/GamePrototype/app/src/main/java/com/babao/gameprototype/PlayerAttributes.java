package com.babao.gameprototype;

public class PlayerAttributes {
    String email, username, password;
    int score;
    int lives;

    public PlayerAttributes() {

    }

    public PlayerAttributes(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.score = 0;
        this.lives = 3;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }
}
