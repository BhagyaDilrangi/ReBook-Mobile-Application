package com.nibm.rebook;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nibm.rebook.CustomAdapter.LeaderboardAdapter;
import com.nibm.rebook.dto.LeaderboardItem;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard); // Ensure XML name matches

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        RecyclerView rvLeaderboard = findViewById(R.id.rvLeaderboard);
        rvLeaderboard.setLayoutManager(new LinearLayoutManager(this));

        // Create Dummy Data
        List<LeaderboardItem> leaderboardList = new ArrayList<>();
        leaderboardList.add(new LeaderboardItem("Alice Johnson", 1250));
        leaderboardList.add(new LeaderboardItem("Bob Smith", 1120));
        leaderboardList.add(new LeaderboardItem("Charlie Davis", 980));
        leaderboardList.add(new LeaderboardItem("Diana Prince", 850));

        // Set Adapter
        LeaderboardAdapter adapter = new LeaderboardAdapter(leaderboardList);
        rvLeaderboard.setAdapter(adapter);
    }
}