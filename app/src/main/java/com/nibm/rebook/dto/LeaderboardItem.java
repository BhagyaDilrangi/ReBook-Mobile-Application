package com.nibm.rebook.dto;

public class LeaderboardItem {
    private String name;
    private int score;

    public LeaderboardItem(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() { return name; }
    public int getScore() { return score; }
}