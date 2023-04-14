package com.example.ics108_project;

public class Player {
    private static int score;

    public static void addScore(int score) {
        Player.score += score;
    }

    public static int getScore() {
        return score;
    }

    public static void resetScore() {
        score = 0;
    }
}