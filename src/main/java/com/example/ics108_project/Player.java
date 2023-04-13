package com.example.ics108_project;

public class Player {
    int score;

    public Player() {
        this.score = 0;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
