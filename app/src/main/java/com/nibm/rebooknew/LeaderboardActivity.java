package com.nibm.rebooknew;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LeaderboardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        getSupportActionBar().hide();

        RecyclerView rv = findViewById(R.id.rvLeaderboard);
        rv.setLayoutManager(new LinearLayoutManager(this));

        // Logic: Fetch students from Firebase ordered by 'points' descending
    }
}