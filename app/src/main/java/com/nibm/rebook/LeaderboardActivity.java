package com.nibm.rebook;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LeaderboardActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LeaderboardAdapter adapter;
    ArrayList<LeaderboardModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        recyclerView = findViewById(R.id.recyclerViewLeaderboard);

        list = new ArrayList<>();

        // Dummy Data
        list.add(new LeaderboardModel("Kasun Perera", 980, 1));
        list.add(new LeaderboardModel("Nimal Silva", 870, 2));
        list.add(new LeaderboardModel("Amaya Fernando", 760, 3));
        list.add(new LeaderboardModel("Kavindi Jay", 650, 4));

        adapter = new LeaderboardAdapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}