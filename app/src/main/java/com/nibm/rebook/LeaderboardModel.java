package com.nibm.rebook;

public class LeaderboardModel {

    String name;
    int points;
    int rank;

    public LeaderboardModel(String name, int points, int rank) {
        this.name = name;
        this.points = points;
        this.rank = rank;
    }

    public String getName() { return name; }
    public int getPoints() { return points; }
    public int getRank() { return rank; }
}