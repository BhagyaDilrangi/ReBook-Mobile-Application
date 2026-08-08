package com.nibm.rebook.dto;

public class LeaderboardItem {
    private String name;
    private int score;

    // Required for Firebase
    public LeaderboardItem() {
    }

    public LeaderboardItem(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() { return name; }
    public int getScore() { return score; }

    public void setName(String name) { this.name = name; }
    public void setScore(int score) { this.score = score; }
}